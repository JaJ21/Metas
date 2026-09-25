package br.com.empresa.metas.permissao.core.domain.model;

public class Permissao {

    private final String cpf;
    private final ChaveCentroConta chave;
    private boolean read;
    private boolean write;
    private boolean delete;

    public Permissao(String cpf, ChaveCentroConta chave, boolean read, boolean write, boolean delete) {
        this.cpf = cpf;
        this.chave = chave;
        this.read = read;
        this.write = write;
        this.delete = delete;
    }

    public void atualizar(boolean read, boolean write, boolean delete) {
        this.read = read;
        this.write = write;
        this.delete = delete;
    }

    public String getCpf() { return cpf; }
    public ChaveCentroConta getChave() { return chave; }
    public boolean isRead() { return read; }
    public boolean isWrite() { return write; }
    public boolean isDelete() { return delete; }
}
