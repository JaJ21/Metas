package br.com.empresa.metas.auth.infra.controller.dto.response;

import java.util.List;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresInMinutes,
        String cpf,
        String nome,
        List<String> roles
) {
}
