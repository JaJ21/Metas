package br.com.empresa.metas.usuario.core.usecase;

import br.com.empresa.metas.usuario.core.domain.gateway.UsuarioGateway;
import br.com.empresa.metas.usuario.core.domain.model.Usuario;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

/**
 * Caso de uso: cadastra um usuário novo, ou ATUALIZA se o CPF já
 * existir (mesmo comportamento de "upsert" do usuarios.py original).
 *
 * "Caso de uso" (use case) é uma classe que representa UMA ação de
 * negócio, com um método só de entrada (geralmente "executar"). Fica no
 * core, não depende de Spring — por isso o construtor recebe a
 * dependência (UsuarioGateway) "na mão", em vez de usar @Autowired
 * (essa injeção "de fora" é feita na camada infra, no arquivo
 * infra/config/UsuarioUseCaseConfig.java).
 *
 * O parâmetro "hashDeSenha" é uma Function (uma função recebida como
 * parâmetro) que sabe transformar senha em texto puro pro hash BCrypt —
 * repare que o core não importa nada do Spring Security aqui, só recebe
 * "uma função que faz isso" de fora, mantendo o core limpo.
 */
public class CadastrarOuAtualizarUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final Function<String, String> hashDeSenha;

    public CadastrarOuAtualizarUsuarioUseCase(UsuarioGateway usuarioGateway, Function<String, String> hashDeSenha) {
        this.usuarioGateway = usuarioGateway;
        this.hashDeSenha = hashDeSenha;
    }

    public Usuario executar(String cpf, String nome, String cargo, String senhaEmTextoPuro, List<String> roles) {
        return usuarioGateway.buscarPorCpf(cpf)
                .map(existente -> {
                    existente.atualizarDados(nome, cargo);
                    if (senhaEmTextoPuro != null && !senhaEmTextoPuro.isBlank()) {
                        existente.atualizarSenha(hashDeSenha.apply(senhaEmTextoPuro));
                    }
                    if (roles != null) {
                        existente.atualizarRoles(roles);
                    }
                    return usuarioGateway.salvar(existente);
                })
                .orElseGet(() -> {
                    String senhaHash = senhaEmTextoPuro != null ? hashDeSenha.apply(senhaEmTextoPuro) : null;
                    Usuario novo = new Usuario(cpf, nome, cargo, senhaHash, roles, Instant.now());
                    return usuarioGateway.salvar(novo);
                });
    }
}
