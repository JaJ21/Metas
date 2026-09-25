package br.com.empresa.metas.auth.infra.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String cpf,
        @NotBlank String senha
) {
}
