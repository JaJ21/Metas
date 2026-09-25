package br.com.empresa.metas.meta.infra.controller.dto.response;

import br.com.empresa.metas.meta.core.domain.model.UltimoUpload;

import java.time.Instant;

public record UltimoUploadResponse(
        String nomeArquivoOriginal, Instant data, int linhas, int substituidas, int novas
) {
    public static UltimoUploadResponse from(UltimoUpload u) {
        return new UltimoUploadResponse(u.nomeArquivoOriginal(), u.data(), u.linhas(), u.substituidas(), u.novas());
    }
}
