package br.com.empresa.metas.meta.infra.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

/**
 * Uma linha da planilha, como o front-end manda (seja no preview inicial
 * gerado a partir do .xlsx, seja depois de editada na tela). Os mapas
 * são chave = número do mês (1-12), valor = texto bruto (pode ter "-",
 * vírgula decimal, etc — a validação de verdade é feita no core).
 */
public record LinhaMetaRequest(
        @NotBlank String centroCusto,
        @NotBlank String codConta,
        Map<Integer, String> real,
        Map<Integer, String> forecast,
        Map<Integer, String> orcado,
        String justificativa
) {
}
