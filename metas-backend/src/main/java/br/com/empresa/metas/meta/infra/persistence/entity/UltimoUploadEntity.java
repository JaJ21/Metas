package br.com.empresa.metas.meta.infra.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ultimo_upload")
public class UltimoUploadEntity {

    @Id
    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @Column(name = "nome_arquivo_original")
    private String nomeArquivoOriginal;

    @Column(name = "data_upload", nullable = false)
    private Instant data;

    @Column(name = "linhas", nullable = false)
    private int linhas;

    @Column(name = "substituidas", nullable = false)
    private int substituidas;

    @Column(name = "novas", nullable = false)
    private int novas;

    @OneToMany(mappedBy = "ultimoUpload", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UltimoUploadChaveEntity> chaves = new ArrayList<>();

    protected UltimoUploadEntity() {}

    public UltimoUploadEntity(String cpf, String nomeArquivoOriginal, Instant data, int linhas, int substituidas, int novas) {
        this.cpf = cpf;
        this.nomeArquivoOriginal = nomeArquivoOriginal;
        this.data = data;
        this.linhas = linhas;
        this.substituidas = substituidas;
        this.novas = novas;
    }

    public String getCpf() { return cpf; }
    public String getNomeArquivoOriginal() { return nomeArquivoOriginal; }
    public Instant getData() { return data; }
    public int getLinhas() { return linhas; }
    public int getSubstituidas() { return substituidas; }
    public int getNovas() { return novas; }
    public List<UltimoUploadChaveEntity> getChaves() { return chaves; }
}
