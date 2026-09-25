import type { UltimoUploadDto } from "../../infrastructure/api/metas-api";

/**
 * Server Component (sem "use client") — só exibe dados que já vieram
 * prontos da consulta feita na page.tsx. Não tem estado, não tem
 * interação, então não precisa (e não deve) virar Client Component.
 */
export function UltimoUploadCard({ ultimo }: { ultimo: UltimoUploadDto | null }) {
  if (!ultimo) return null;

  const dataFormatada = new Date(ultimo.data).toLocaleString("pt-BR");

  return (
    <div className="mb-6 rounded-md border border-gray-200 bg-gray-50 p-4">
      <h2 className="text-sm font-semibold text-gray-800">Seu último upload</h2>
      <p className="mt-1 text-sm text-muted">
        Arquivo: <strong>{ultimo.nomeArquivoOriginal}</strong> — enviado em {dataFormatada}
        <br />
        {ultimo.linhas} linha(s) — {ultimo.substituidas} substituída(s), {ultimo.novas} nova(s)
      </p>
      <a href="/api/metas/ultimo-upload/download" className="mt-2 inline-block text-sm text-primary underline">
        Baixar essa planilha novamente
      </a>
    </div>
  );
}
