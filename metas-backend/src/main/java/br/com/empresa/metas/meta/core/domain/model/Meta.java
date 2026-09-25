package br.com.empresa.metas.meta.core.domain.model;

import br.com.empresa.metas.meta.core.domain.enums.TipoValorMes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * A entidade central do sistema: uma linha de meta, identificada por
 * Centro de Custo + Cod Conta, com os valores dos três blocos de mês e
 * a Justificativa. Equivalente a uma linha do metas_master.xlsx do
 * sistema em Python.
 */
public class Meta {

    private final String centroCusto;
    private final String codConta;
    private final List<ValorMensal> valores;
    private final String justificativa;
    private String cpfUltimaAlteracao;
    private Instant dataUltimaAlteracao;

    public Meta(String centroCusto, String codConta, List<ValorMensal> valores, String justificativa,
                String cpfUltimaAlteracao, Instant dataUltimaAlteracao) {
        this.centroCusto = centroCusto;
        this.codConta = codConta;
        this.valores = valores;
        this.justificativa = justificativa;
        this.cpfUltimaAlteracao = cpfUltimaAlteracao;
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    /** Marca quem alterou e quando — chamado pelo gateway na hora de salvar. */
    public void registrarAlteracao(String cpf, Instant momento) {
        this.cpfUltimaAlteracao = cpf;
        this.dataUltimaAlteracao = momento;
    }

    public BigDecimal totalPorTipo(TipoValorMes tipo) {
        return valores.stream()
                .filter(v -> v.tipo() == tipo)
                .map(ValorMensal::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getCentroCusto() { return centroCusto; }
    public String getCodConta() { return codConta; }
    public List<ValorMensal> getValores() { return valores; }
    public String getJustificativa() { return justificativa; }
    public String getCpfUltimaAlteracao() { return cpfUltimaAlteracao; }
    public Instant getDataUltimaAlteracao() { return dataUltimaAlteracao; }
}
