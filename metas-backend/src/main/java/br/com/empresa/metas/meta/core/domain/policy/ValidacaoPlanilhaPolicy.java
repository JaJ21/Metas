package br.com.empresa.metas.meta.core.domain.policy;

import br.com.empresa.metas.meta.core.domain.model.*;
import br.com.empresa.metas.shared.core.exception.ValidationException;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Regras de validação da planilha de metas — equivalente a
 * validar_e_normalizar() do excel_utils.py original. Fica em
 * "core/domain/policy" porque é uma REGRA DE NEGÓCIO pura (sem
 * framework): nenhuma letra fora da Justificativa, "-" e vazio contam
 * como zero, vírgula decimal é aceita, e não pode haver duas linhas para
 * o mesmo Centro de Custo + Cod Conta na mesma planilha.
 */
public final class ValidacaoPlanilhaPolicy {

    private static final Pattern PADRAO_LETRA = Pattern.compile("[A-Za-zÀ-ÿ]");

    private ValidacaoPlanilhaPolicy() {}

    public static Meta validarENormalizar(LinhaMetaInput input, CicloOrcamentario ciclo) {
        if (input.centroCusto() == null || input.centroCusto().isBlank()) {
            throw new ValidationException("META_INVALIDA", "Centro de Custo é obrigatório.");
        }
        if (input.codConta() == null || input.codConta().isBlank()) {
            throw new ValidationException("META_INVALIDA", "Cod Conta é obrigatório.");
        }
        validarSemLetras("Centro de Custo", input.centroCusto());
        validarSemLetras("Cod Conta", input.codConta());

        List<ValorMensal> valores = new ArrayList<>();
        for (ValorMesInput valorInput : input.valores()) {
            String coluna = valorInput.mes() + "/" + valorInput.ano();
            validarSemLetras(coluna, valorInput.valorBruto());
            BigDecimal valor = normalizarValorNumerico(valorInput.valorBruto());
            var tipo = ciclo.classificar(valorInput.ano(), valorInput.mes());
            valores.add(new ValorMensal(valorInput.ano(), valorInput.mes(), tipo, valor));
        }

        String justificativa = input.justificativaBruta() == null ? "" : input.justificativaBruta().trim();

        return new Meta(input.centroCusto().trim(), input.codConta().trim(), valores, justificativa, null, null);
    }

    /** Confere que não há duas linhas para o mesmo par Centro de Custo + Cod Conta na mesma planilha enviada. */
    public static void validarSemDuplicados(List<LinhaMetaInput> linhas) {
        Set<String> vistos = new HashSet<>();
        List<String> duplicados = new ArrayList<>();

        for (LinhaMetaInput linha : linhas) {
            String chave = linha.centroCusto() + "|" + linha.codConta();
            if (!vistos.add(chave)) {
                duplicados.add(linha.centroCusto() + "/" + linha.codConta());
            }
        }

        if (!duplicados.isEmpty()) {
            throw new ValidationException("META_DUPLICADA",
                    "A planilha tem linhas duplicadas para: " + String.join(", ", duplicados));
        }
    }

    private static void validarSemLetras(String coluna, String valor) {
        if (valor != null && PADRAO_LETRA.matcher(valor).find()) {
            throw new ValidationException("META_INVALIDA",
                    "Coluna '" + coluna + "': valor '" + valor + "' contém letras.");
        }
    }

    /**
     * Trata "-" (usado pela planilha da empresa pra representar zero),
     * célula vazia, e vírgula decimal — mesmo comportamento do
     * excel_utils.py original.
     */
    private static BigDecimal normalizarValorNumerico(String valorBruto) {
        if (valorBruto == null) return BigDecimal.ZERO;
        String limpo = valorBruto.trim();
        if (limpo.isEmpty() || limpo.equals("-")) return BigDecimal.ZERO;
        limpo = limpo.replace(",", ".");
        try {
            return new BigDecimal(limpo);
        } catch (NumberFormatException e) {
            throw new ValidationException("META_INVALIDA", "Valor numérico inválido: '" + valorBruto + "'.");
        }
    }
}
