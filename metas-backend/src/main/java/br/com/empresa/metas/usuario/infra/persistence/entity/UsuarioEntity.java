package br.com.empresa.metas.usuario.infra.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * Entidade JPA — o "espelho" da tabela "usuario" do PostgreSQL. Só essa
 * classe (camada infra) tem anotações do JPA/Hibernate; a classe de
 * domínio (Usuario, no core) fica limpa.
 *
 * O CPF é a chave primária (@Id) — não usamos um ID numérico separado
 * porque o CPF já É o identificador natural e único de cada usuário
 * neste sistema.
 */
@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "senha_hash")
    private String senhaHash;

    /**
     * Guardado como texto simples separado por vírgula (ex: "ADMIN") em
     * vez de uma tabela à parte — são só uns poucos papéis possíveis,
     * não justifica o custo de mais uma tabela/join agora. Se a lista de
     * papéis crescer bastante, vale normalizar numa tabela
     * "usuario_role" no futuro.
     */
    @Column(name = "roles")
    private String roles;

    @Column(name = "data_cadastro", nullable = false)
    private Instant dataCadastro;

    protected UsuarioEntity() {
        // construtor vazio exigido pelo JPA/Hibernate (ele usa reflection pra criar o objeto)
    }

    public UsuarioEntity(String cpf, String nome, String cargo, String senhaHash, String roles, Instant dataCadastro) {
        this.cpf = cpf;
        this.nome = nome;
        this.cargo = cargo;
        this.senhaHash = senhaHash;
        this.roles = roles;
        this.dataCadastro = dataCadastro;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
    public Instant getDataCadastro() { return dataCadastro; }
}
