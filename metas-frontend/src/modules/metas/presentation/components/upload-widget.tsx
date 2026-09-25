"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button/button";
import { Input } from "@/components/ui/input/input";
import { ErrorState } from "@/components/ui/error-state/error-state";
import { Table } from "@/components/ui/table/table";
import type { PreviewPlanilha } from "../../domain/entities/linha-meta";
import { mesesForecast, mesesOrcado, mesesReal } from "../../domain/value-objects/ciclo-orcamentario";
import { paraLinhaEditavel, somar, type LinhaEditavel } from "./linha-editavel";

/**
 * A "ilha cliente" do fluxo de upload — igual o documento de
 * arquitetura descreve na seção 6.6 ("Editor interativo: shell
 * server-side e ilha cliente para edição"). A página em volta
 * (page.tsx) é Server Component; só este widget, que precisa de estado
 * local pra edição e requisições disparadas por evento, é 'use client'.
 */
type Fase = "ocioso" | "enviando" | "preview" | "confirmando" | "sucesso";

export function UploadWidget() {
  const [fase, setFase] = useState<Fase>("ocioso");
  const [preview, setPreview] = useState<PreviewPlanilha | null>(null);
  const [linhas, setLinhas] = useState<LinhaEditavel[]>([]);
  const [nomeArquivo, setNomeArquivo] = useState("");
  const [erro, setErro] = useState<string | null>(null);
  const [resumo, setResumo] = useState<{ substituidas: number; novas: number } | null>(null);

  async function handleUpload(arquivo: File) {
    setErro(null);
    setFase("enviando");
    setNomeArquivo(arquivo.name);

    const formData = new FormData();
    formData.append("arquivo", arquivo);

    const resposta = await fetch("/api/metas/upload", { method: "POST", body: formData });
    const corpo = await resposta.json();

    if (!resposta.ok) {
      setErro(corpo.message ?? "Não foi possível processar a planilha.");
      setFase("ocioso");
      return;
    }

    const dados = corpo as PreviewPlanilha;
    setPreview(dados);
    setLinhas(dados.linhas.map(paraLinhaEditavel));
    setFase("preview");
  }

  function atualizarValor(indice: number, bloco: "forecast" | "orcado", mes: number, valor: string) {
    setLinhas((atual) =>
      atual.map((linha, i) => (i === indice ? { ...linha, [bloco]: { ...linha[bloco], [mes]: valor } } : linha))
    );
  }

  function atualizarTexto(indice: number, campo: "centroCusto" | "codConta" | "justificativa", valor: string) {
    setLinhas((atual) => atual.map((linha, i) => (i === indice ? { ...linha, [campo]: valor } : linha)));
  }

  async function handleConfirmar() {
    setErro(null);
    setFase("confirmando");

    const payload = {
      nomeArquivoOriginal: nomeArquivo,
      linhas: linhas.map((linha) => ({
        centroCusto: linha.centroCusto,
        codConta: linha.codConta,
        forecast: linha.forecast,
        orcado: linha.orcado,
        justificativa: linha.justificativa,
      })),
    };

    const resposta = await fetch("/api/metas/confirmar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const corpo = await resposta.json();

    if (!resposta.ok) {
      setErro(corpo.message ?? "Não foi possível confirmar o upload.");
      setFase("preview");
      return;
    }

    setResumo(corpo);
    setFase("sucesso");
  }

  function reiniciar() {
    setFase("ocioso");
    setPreview(null);
    setLinhas([]);
    setResumo(null);
    setErro(null);
  }

  if (fase === "sucesso" && resumo) {
    return (
      <div className="rounded-md border border-success bg-green-50 p-4">
        <p className="text-sm text-gray-800">
          Upload aplicado com sucesso: <strong>{resumo.substituidas}</strong> linha(s) substituída(s) e{" "}
          <strong>{resumo.novas}</strong> nova(s).
        </p>
        <Button variante="secundaria" className="mt-3" onClick={reiniciar}>
          Enviar outra planilha
        </Button>
      </div>
    );
  }

  // "confirmando" é uma continuação visual do preview: a tabela precisa
  // continuar na tela (só com o botão em estado de carregando) enquanto a
  // requisição de confirmar está em andamento. Por isso as duas fases
  // renderizam este mesmo bloco.
  if ((fase === "preview" || fase === "confirmando") && preview) {
    return (
      <div className="flex flex-col gap-4">
        {erro && <ErrorState mensagem={erro} />}
        <p className="text-sm text-muted">
          {linhas.length} linha(s). O bloco REAL é só referência. Edite FORECAST e ORÇADO antes de confirmar.
        </p>
        <Table>
          <thead>
            <tr className="bg-gray-50 text-left">
              <th className="p-2">Centro de Custo</th>
              <th className="p-2">Cod Conta</th>
              {mesesReal(preview.ciclo).map((m) => (
                <th key={m.rotulo} className="bg-gray-100 p-2 text-xs text-muted">{m.rotulo}</th>
              ))}
              {mesesForecast(preview.ciclo).map((m) => (
                <th key={m.rotulo} className="bg-blue-50 p-2 text-xs">{m.rotulo}</th>
              ))}
              <th className="bg-blue-50 p-2 text-xs font-semibold">Total FORECAST</th>
              {mesesOrcado(preview.ciclo).map((m) => (
                <th key={m.rotulo} className="bg-green-50 p-2 text-xs">{m.rotulo}</th>
              ))}
              <th className="bg-green-50 p-2 text-xs font-semibold">Total ORÇADO</th>
              <th className="p-2">Justificativa</th>
              <th className="p-2">Valores atuais</th>
            </tr>
          </thead>
          <tbody>
            {linhas.map((linha, indice) => (
              <tr key={indice} className="border-t border-gray-100">
                <td className="p-1">
                  <Input value={linha.centroCusto} onChange={(e) => atualizarTexto(indice, "centroCusto", e.target.value)} />
                </td>
                <td className="p-1">
                  <Input value={linha.codConta} onChange={(e) => atualizarTexto(indice, "codConta", e.target.value)} />
                </td>
                {mesesReal(preview.ciclo).map((m) => (
                  <td key={m.mes} className="bg-gray-50 p-2 text-right text-xs text-muted">
                    {(linha.real[m.mes] ?? 0).toFixed(2)}
                  </td>
                ))}
                {mesesForecast(preview.ciclo).map((m) => (
                  <td key={m.mes} className="bg-blue-50 p-1">
                    <Input
                      type="number"
                      step="0.01"
                      value={linha.forecast[m.mes] ?? "0"}
                      onChange={(e) => atualizarValor(indice, "forecast", m.mes, e.target.value)}
                    />
                  </td>
                ))}
                <td className="bg-blue-50 p-2 text-right text-xs font-semibold">{somar(linha.forecast).toFixed(2)}</td>
                {mesesOrcado(preview.ciclo).map((m) => (
                  <td key={m.mes} className="bg-green-50 p-1">
                    <Input
                      type="number"
                      step="0.01"
                      value={linha.orcado[m.mes] ?? "0"}
                      onChange={(e) => atualizarValor(indice, "orcado", m.mes, e.target.value)}
                    />
                  </td>
                ))}
                <td className="bg-green-50 p-2 text-right text-xs font-semibold">{somar(linha.orcado).toFixed(2)}</td>
                <td className="p-1">
                  <Input value={linha.justificativa} onChange={(e) => atualizarTexto(indice, "justificativa", e.target.value)} />
                </td>
                <td className="p-2 text-xs text-muted">
                  {linha.totaisAtuais === null
                    ? "sem permissão de leitura"
                    : !linha.totaisAtuais.existiaAntes
                      ? "(ainda não existe)"
                      : `REAL ${linha.totaisAtuais.totalReal.toFixed(2)} · FORECAST ${linha.totaisAtuais.totalForecast.toFixed(2)} · ORÇADO ${linha.totaisAtuais.totalOrcado.toFixed(2)}`}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
        <div className="flex gap-2">
          <Button carregando={fase === "confirmando"} onClick={handleConfirmar}>
            Confirmar e salvar
          </Button>
          <Button variante="secundaria" onClick={reiniciar}>
            Cancelar
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="flex flex-col gap-3">
      {erro && <ErrorState mensagem={erro} />}
      <input
        type="file"
        accept=".xlsx,.xls"
        disabled={fase === "enviando"}
        onChange={(e) => {
          const arquivo = e.target.files?.[0];
          if (arquivo) void handleUpload(arquivo);
        }}
        className="text-sm"
      />
      {fase === "enviando" && <p className="text-sm text-muted">Processando planilha...</p>}
    </div>
  );
}
