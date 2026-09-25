package br.com.empresa.metas.permissao.infra.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PermissaoRequest(
        @NotBlank String cpf,
        @NotBlank String centroCusto,
        @NotBlank String codConta,
        boolean read,
        boolean write,
        boolean delete
) {
}
