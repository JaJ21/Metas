package br.com.empresa.metas.shared.infra.config.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Configura como o JWT é GERADO (JwtEncoder) e VALIDADO (JwtDecoder).
 *
 * Como o padrão pede "OAuth2 Resource Server com JWT", mas ainda não
 * existe um provedor de identidade (IdP) externo na empresa, o próprio
 * backend assume os dois papéis: ele MESMO assina o token no login
 * (auth/infra/AuthController) e MESMO valida esse token nas próximas
 * requisições (via Spring Security, configurado como Resource Server
 * "normal" — só que a chave de validação é a mesma chave secreta usada
 * pra assinar, em vez de vir de um IdP externo por HTTPS).
 *
 * Quando a empresa tiver um IdP de verdade (Keycloak, Azure AD, etc),
 * essa classe é o único lugar que precisa mudar: troca-se o
 * JwtEncoder/JwtDecoder simétrico por um decoder que busca a chave
 * pública do IdP (issuer-uri), sem tocar em mais nada do sistema.
 */
@Configuration
public class JwtConfig {

    private final SecretKeySpec chaveSecreta;

    public JwtConfig(@Value("${security.jwt.secret}") String secret) {
        this.chaveSecreta = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(chaveSecreta));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(chaveSecreta)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * BCrypt é o algoritmo padrão pra guardar senha com hash (nunca em
     * texto puro) — usado no cadastro/login de usuário.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
