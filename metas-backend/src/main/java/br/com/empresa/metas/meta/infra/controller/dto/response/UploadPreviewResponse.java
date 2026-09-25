package br.com.empresa.metas.meta.infra.controller.dto.response;

import java.util.List;

public record UploadPreviewResponse(CicloOrcamentarioResponse ciclo, List<MetaPreviewResponse> linhas) {
}
