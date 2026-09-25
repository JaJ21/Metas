package br.com.empresa.metas.usuario.core.domain.model;

import java.time.Instant;
import java.util.List;

/**
 * Entidade de domínio "Usuário" — a representação de negócio, sem
 * NENHUMA anotação de framework (nem @Entity, nem @Data do Lombok). É
 * isso que o padrão chama de "core sem framework": essa classe podia
 * ser copiada pra qualquer outro projeto Java, mesmo um que não usasse
 * Spring nem JPA, e continuaria funcionando.
 *
 * "senhaHash" nunca é a senha em texto puro — é sempre o resultado do
 * BCrypt (ver JwtConfig.passwordEncoder()).
 */
public class Usuario {

    private final String cpf;
    private String nome;
    private String cargo;
    private String senhaHash;
    private List<String> roles;
    private final Instant dataCadastro;

    public Usuario(String cpf, String nome, String cargo, String senhaHash, List<String> roles, Instant dataCadastro) {
        this.cpf = cpf;
        this.nome = nome;
        this.cargo = cargo;
        this.senhaHash = senhaHash;
        this.roles = roles;
        this.dataCadastro = dataCadastro;
    }

    public boolean isAdmin() {
        return roles != null && roles.contains("ADMIN");
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public String getSenhaHash() { return senhaHash; }
    public List<String> getRoles() { return roles; }
    public Instant getDataCadastro() { return dataCadastro; }

    public void atualizarDados(String nome, String cargo) {
        this.nome = nome;
        this.cargo = cargo;
    }

    public void atualizarSenha(String novaSenhaHash) {
        this.senhaHash = novaSenhaHash;
    }

    public void atualizarRoles(List<String> roles) {
        this.roles = roles;
    }
}
