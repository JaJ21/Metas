package br.com.empresa.metas.shared.infra.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Configuração central de segurança HTTP. É aqui que decidimos:
 *   1. Quais rotas são públicas (login, documentação) e quais exigem JWT
 *   2. Como o JWT é validado (usando o JwtDecoder de JwtConfig)
 *   3. Como os "claims" do token viram permissões do Spring Security
 *      (GrantedAuthority) — usamos a claim "roles" do token, prefixando
 *      cada valor com "ROLE_" (convenção do Spring Security).
 *
 * A API é totalmente "stateless" (sem sessão de servidor) — cada
 * requisição carrega seu próprio JWT no header Authorization, então
 * desligamos CSRF (que só faz sentido pra autenticação baseada em
 * cookie/sessão) e a criação de sessão HTTP.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Login e documentação ficam abertos, sem exigir token.
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/docs/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        // Todo o resto da API exige JWT válido.
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * Diz ao Spring Security onde, dentro do token, estão os "papéis" do
     * usuário (a claim "roles", que colocamos lá no JwtTokenService), e
     * que cada papel deve virar uma autoridade "ROLE_<papel>". Isso é o
     * que permite usar @PreAuthorize("hasRole('ADMIN')") nos controllers.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        // O "principal" (quem é o usuário logado) passa a ser o CPF, que
        // é o "subject" (sub) do token.
        converter.setPrincipalClaimName("sub");
        return converter;
    }

    /**
     * CORS liberado para qualquer origem por padrão — como o front-end
     * ainda não foi definido (o padrão de front vai chegar depois),
     * deixamos permissivo aqui. Quando o front for definido, troque
     * "*" pela URL real dele.
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
