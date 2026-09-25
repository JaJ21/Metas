package br.com.empresa.metas.auth.infra.controller;

import br.com.empresa.metas.auth.core.usecase.AutenticarUsuarioUseCase;
import br.com.empresa.metas.auth.infra.controller.dto.request.LoginRequest;
import br.com.empresa.metas.auth.infra.controller.dto.response.LoginResponse;
import br.com.empresa.metas.shared.infra.config.security.JwtTokenService;
import br.com.empresa.metas.usuario.core.domain.model.Usuario;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Único endpoint público do sistema (ver SecurityConfig — "/auth/login"
 * está na lista de rotas sem exigência de JWT, já que é aqui que o
 * token é obtido). @SecurityRequirements() vazio tira a exigência de
 * "cadeado" desse endpoint na telinha do Swagger também.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final JwtTokenService jwtTokenService;
    private final long expirationMinutes;

    public AuthController(
            AutenticarUsuarioUseCase autenticarUsuarioUseCase,
            JwtTokenService jwtTokenService,
            @Value("${security.jwt.expiration-minutes}") long expirationMinutes
    ) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.jwtTokenService = jwtTokenService;
        this.expirationMinutes = expirationMinutes;
    }

    @PostMapping("/login")
    @SecurityRequirements()
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = autenticarUsuarioUseCase.executar(request.cpf(), request.senha());
        String token = jwtTokenService.gerarToken(usuario.getCpf(), usuario.getNome(), usuario.getRoles());

        return new LoginResponse(
                token, "Bearer", expirationMinutes,
                usuario.getCpf(), usuario.getNome(), usuario.getRoles()
        );
    }
}
