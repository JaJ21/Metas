package br.com.empresa.metas.meta.infra.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ultimo_upload_chave")
public class UltimoUploadChaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf", nullable = false, foreignKey = @ForeignKey(name = "fk_ultimo_upload_chave_upload"))
    private UltimoUploadEntity ultimoUpload;

    @Column(name = "centro_custo", nullable = false)
    private String centroCusto;

    @Column(name = "cod_conta", nullable = false)
    private String codConta;

    protected UltimoUploadChaveEntity() {}

    public UltimoUploadChaveEntity(Long id, UltimoUploadEntity ultimoUpload, String centroCusto, String codConta) {
        this.id = id;
        this.ultimoUpload = ultimoUpload;
        this.centroCusto = centroCusto;
        this.codConta = codConta;
    }

    public Long getId() { return id; }
    public UltimoUploadEntity getUltimoUpload() { return ultimoUpload; }
    public String getCentroCusto() { return centroCusto; }
    public String getCodConta() { return codConta; }
}
