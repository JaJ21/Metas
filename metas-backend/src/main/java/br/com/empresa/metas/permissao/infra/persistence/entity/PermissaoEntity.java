package br.com.empresa.metas.permissao.infra.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "permissao", uniqueConstraints = {
        @UniqueConstraint(name = "uk_permissao_cpf_centro_conta", columnNames = {"cpf", "centro_custo", "cod_conta"})
})
public class PermissaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @Column(name = "centro_custo", nullable = false)
    private String centroCusto;

    @Column(name = "cod_conta", nullable = false)
    private String codConta;

    @Column(name = "leitura", nullable = false)
    private boolean leitura;

    @Column(name = "escrita", nullable = false)
    private boolean escrita;

    @Column(name = "exclusao", nullable = false)
    private boolean exclusao;

    protected PermissaoEntity() {}

    public PermissaoEntity(Long id, String cpf, String centroCusto, String codConta, boolean leitura, boolean escrita, boolean exclusao) {
        this.id = id;
        this.cpf = cpf;
        this.centroCusto = centroCusto;
        this.codConta = codConta;
        this.leitura = leitura;
        this.escrita = escrita;
        this.exclusao = exclusao;
    }

    public Long getId() { return id; }
    public String getCpf() { return cpf; }
    public String getCentroCusto() { return centroCusto; }
    public String getCodConta() { return codConta; }
    public boolean isLeitura() { return leitura; }
    public void setLeitura(boolean leitura) { this.leitura = leitura; }
    public boolean isEscrita() { return escrita; }
    public void setEscrita(boolean escrita) { this.escrita = escrita; }
    public boolean isExclusao() { return exclusao; }
    public void setExclusao(boolean exclusao) { this.exclusao = exclusao; }
}
