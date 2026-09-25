package br.com.empresa.metas.meta.infra.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ConfirmarUploadRequest(
        String nomeArquivoOriginal,
        @NotEmpty @Valid List<LinhaMetaRequest> linhas
) {
}
