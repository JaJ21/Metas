package br.com.empresa.metas.meta.infra.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Tabela "meta" — só o cabeçalho da linha (Centro de Custo, Cod Conta,
 * Justificativa, quem alterou). Os valores mensais ficam numa tabela
 * FILHA (MetaValorEntity), normalizada — em vez de uma coluna por mês
 * (que exigiria alterar o schema todo ano quando o ciclo orçamentário
 * mudasse), cada mês é uma LINHA na tabela meta_valor. Isso deixa o
 * schema estável ano após ano.
 */
@Entity
@Table(name = "meta", uniqueConstraints = {
        @UniqueConstraint(name = "uk_meta_centro_custo_cod_conta", columnNames = {"centro_custo", "cod_conta"})
})
public class MetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "centro_custo", nullable = false)
    private String centroCusto;

    @Column(name = "cod_conta", nullable = false)
    private String codConta;

    @Column(name = "justificativa")
    private String justificativa;

    @Column(name = "cpf_ultima_alteracao", length = 11)
    private String cpfUltimaAlteracao;

    @Column(name = "data_ultima_alteracao")
    private Instant dataUltimaAlteracao;

    @OneToMany(mappedBy = "meta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MetaValorEntity> valores = new ArrayList<>();

    protected MetaEntity() {}

    public MetaEntity(Long id, String centroCusto, String codConta, String justificativa,
                       String cpfUltimaAlteracao, Instant dataUltimaAlteracao) {
        this.id = id;
        this.centroCusto = centroCusto;
        this.codConta = codConta;
        this.justificativa = justificativa;
        this.cpfUltimaAlteracao = cpfUltimaAlteracao;
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    public Long getId() { return id; }
    public String getCentroCusto() { return centroCusto; }
    public String getCodConta() { return codConta; }
    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }
    public String getCpfUltimaAlteracao() { return cpfUltimaAlteracao; }
    public void setCpfUltimaAlteracao(String cpf) { this.cpfUltimaAlteracao = cpf; }
    public Instant getDataUltimaAlteracao() { return dataUltimaAlteracao; }
    public void setDataUltimaAlteracao(Instant data) { this.dataUltimaAlteracao = data; }
    public List<MetaValorEntity> getValores() { return valores; }
}
