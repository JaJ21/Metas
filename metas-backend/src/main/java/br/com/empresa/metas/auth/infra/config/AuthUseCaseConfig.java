package br.com.empresa.metas.auth.infra.config;

import br.com.empresa.metas.auth.core.usecase.AutenticarUsuarioUseCase;
import br.com.empresa.metas.usuario.core.domain.gateway.UsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthUseCaseConfig {

    @Bean
    public AutenticarUsuarioUseCase autenticarUsuarioUseCase(UsuarioGateway usuarioGateway, PasswordEncoder passwordEncoder) {
        return new AutenticarUsuarioUseCase(usuarioGateway, passwordEncoder::matches);
    }
}
