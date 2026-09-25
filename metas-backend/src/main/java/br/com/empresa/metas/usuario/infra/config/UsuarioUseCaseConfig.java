package br.com.empresa.metas.usuario.infra.config;

import br.com.empresa.metas.usuario.core.domain.gateway.UsuarioGateway;
import br.com.empresa.metas.usuario.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * "Fio elétrico" que liga os casos de uso (core) às implementações reais
 * (infra) — é aqui, e SÓ aqui, que o Spring entra em contato com as
 * classes do core. Cada caso de uso vira um @Bean, recebendo a
 * implementação do gateway que o Spring já sabe fabricar (via
 * @Component em UsuarioGatewayImpl).
 */
@Configuration
public class UsuarioUseCaseConfig {

    @Bean
    public CadastrarOuAtualizarUsuarioUseCase cadastrarOuAtualizarUsuarioUseCase(
            UsuarioGateway usuarioGateway, PasswordEncoder passwordEncoder
    ) {
        return new CadastrarOuAtualizarUsuarioUseCase(usuarioGateway, passwordEncoder::encode);
    }

    @Bean
    public RemoverUsuarioUseCase removerUsuarioUseCase(UsuarioGateway usuarioGateway) {
        return new RemoverUsuarioUseCase(usuarioGateway);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioGateway usuarioGateway) {
        return new ListarUsuariosUseCase(usuarioGateway);
    }

    @Bean
    public BuscarUsuarioUseCase buscarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        return new BuscarUsuarioUseCase(usuarioGateway);
    }
}
