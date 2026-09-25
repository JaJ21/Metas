package br.com.empresa.metas.meta.core.usecase;

import br.com.empresa.metas.meta.core.domain.gateway.UltimoUploadGateway;
import br.com.empresa.metas.meta.core.domain.model.UltimoUpload;

import java.util.Optional;

public class ObterUltimoUploadUseCase {

    private final UltimoUploadGateway ultimoUploadGateway;

    public ObterUltimoUploadUseCase(UltimoUploadGateway ultimoUploadGateway) {
        this.ultimoUploadGateway = ultimoUploadGateway;
    }

    public Optional<UltimoUpload> executar(String cpf) {
        return ultimoUploadGateway.obter(cpf);
    }
}
