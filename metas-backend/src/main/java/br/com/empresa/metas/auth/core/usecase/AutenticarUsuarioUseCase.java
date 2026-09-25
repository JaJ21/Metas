package br.com.empresa.metas.auth.core.usecase;

import br.com.empresa.metas.auth.core.exception.CredenciaisInvalidasException;
import br.com.empresa.metas.usuario.core.domain.gateway.UsuarioGateway;
import br.com.empresa.metas.usuario.core.domain.model.Usuario;

import java.util.function.BiPredicate;

/**
 * Caso de uso de login: confere CPF + senha e devolve o Usuario se
 * bater. É o módulo "auth" que orquestra a autenticação, mas ele usa o
 * GATEWAY do módulo "usuario" pra isso (UsuarioGateway) — módulos de
 * domínio podem depender uns dos outros pela camada core (interfaces e
 * models), só nunca pela camada infra de outro módulo.
 *
 * "senhaConfere" é recebido de fora (uma função "(senhaDigitada,
 * hashSalvo) -> true/false") em vez de importar Spring Security aqui —
 * de novo, pra manter o core livre de dependência de framework. Quem
 * "pluga" o BCrypt de verdade é a camada infra (ver
 * auth/infra/config/AuthUseCaseConfig.java).
 */
public class AutenticarUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final BiPredicate<String, String> senhaConfere;

    public AutenticarUsuarioUseCase(UsuarioGateway usuarioGateway, BiPredicate<String, String> senhaConfere) {
        this.usuarioGateway = usuarioGateway;
        this.senhaConfere = senhaConfere;
    }

    public Usuario executar(String cpf, String senha) {
        Usuario usuario = usuarioGateway.buscarPorCpf(cpf)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (usuario.getSenhaHash() == null || !senhaConfere.test(senha, usuario.getSenhaHash())) {
            throw new CredenciaisInvalidasException();
        }

        return usuario;
    }
}
