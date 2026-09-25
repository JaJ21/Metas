package br.com.empresa.metas.meta.core.domain.gateway;

import br.com.empresa.metas.meta.core.domain.model.UltimoUpload;

import java.util.Optional;

public interface UltimoUploadGateway {

    Optional<UltimoUpload> obter(String cpf);

    /** Substitui o cache anterior desse CPF pelo novo resumo (mesma semântica do ultimos_uploads.py original). */
    void registrar(UltimoUpload ultimoUpload);
}
