import { describe, expect, it } from "vitest";
import { somar, paraLinhaEditavel } from "./linha-editavel";
import type { LinhaMeta } from "../../domain/entities/linha-meta";

// Teste de unidade puro (seção 17.1 do documento) — sem React, sem
// servidor, só a função de negócio isolada.
describe("somar", () => {
  it("soma valores válidos e ignora entradas inválidas", () => {
    expect(somar({ 1: "10", 2: "20,5", 3: "abc" })).toBeCloseTo(30.5);
  });

  it("retorna zero para um objeto vazio", () => {
    expect(somar({})).toBe(0);
  });
});

describe("paraLinhaEditavel", () => {
  it("converte os valores numéricos de forecast/orcado para string editável", () => {
    const linha: LinhaMeta = {
      centroCusto: "1001",
      codConta: "30001",
      real: { 1: 100 },
      forecast: { 8: 200 },
      orcado: { 1: 300 },
      totalForecast: 200,
      totalOrcado: 300,
      justificativa: "",
      totaisAtuais: null,
    };

    const editavel = paraLinhaEditavel(linha);

    expect(editavel.forecast[8]).toBe("200");
    expect(editavel.orcado[1]).toBe("300");
    expect(editavel.real[1]).toBe(100); // REAL continua numérico, não é editável
  });
});
