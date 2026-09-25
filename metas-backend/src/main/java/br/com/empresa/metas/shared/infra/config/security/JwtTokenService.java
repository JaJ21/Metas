package br.com.empresa.metas.shared.infra.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Serviço de infraestrutura (não é usado pelo core — só pelo
 * auth/infra/controller na hora de gerar o token de resposta do login).
 *
 * Monta um JWT com:
 *   - subject (sub)  -> o CPF do usuário logado
 *   - claim "nome"   -> nome de exibição
 *   - claim "roles"  -> lista de papéis (ex: ["ADMIN"] ou [])
 *   - issuer, issuedAt, expiresAt
 */
@Component
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationMinutes;

    public JwtTokenService(
            JwtEncoder jwtEncoder,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.expiration-minutes}") long expirationMinutes
    ) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    public String gerarToken(String cpf, String nome, List<String> roles) {
        Instant agora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(agora)
                .expiresAt(agora.plus(expirationMinutes, ChronoUnit.MINUTES))
                .subject(cpf)
                .claim("nome", nome)
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
