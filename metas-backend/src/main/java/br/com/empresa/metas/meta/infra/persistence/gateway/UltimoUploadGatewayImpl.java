package br.com.empresa.metas.meta.infra.persistence.gateway;

import br.com.empresa.metas.meta.core.domain.gateway.UltimoUploadGateway;
import br.com.empresa.metas.meta.core.domain.model.UltimoUpload;
import br.com.empresa.metas.meta.infra.persistence.entity.UltimoUploadChaveEntity;
import br.com.empresa.metas.meta.infra.persistence.entity.UltimoUploadEntity;
import br.com.empresa.metas.meta.infra.persistence.repository.UltimoUploadChaveJpaRepository;
import br.com.empresa.metas.meta.infra.persistence.repository.UltimoUploadJpaRepository;
import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class UltimoUploadGatewayImpl implements UltimoUploadGateway {

    private final UltimoUploadJpaRepository uploadRepository;
    private final UltimoUploadChaveJpaRepository chaveRepository;

    public UltimoUploadGatewayImpl(UltimoUploadJpaRepository uploadRepository, UltimoUploadChaveJpaRepository chaveRepository) {
        this.uploadRepository = uploadRepository;
        this.chaveRepository = chaveRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UltimoUpload> obter(String cpf) {
        return uploadRepository.findById(cpf).map(entity -> new UltimoUpload(
                entity.getCpf(),
                entity.getNomeArquivoOriginal(),
                entity.getData(),
                entity.getLinhas(),
                entity.getSubstituidas(),
                entity.getNovas(),
                chaveRepository.findByUltimoUpload_Cpf(cpf).stream()
                        .map(c -> new ChaveCentroConta(c.getCentroCusto(), c.getCodConta()))
                        .toList()
        ));
    }

    @Override
    public void registrar(UltimoUpload ultimoUpload) {
        // Substitui o registro anterior desse CPF por completo — não
        // acumula histórico, só o mais recente (mesmo comportamento do
        // ultimos_uploads.py original).
        uploadRepository.deleteById(ultimoUpload.cpf());
        chaveRepository.deleteByUltimoUpload_Cpf(ultimoUpload.cpf());

        UltimoUploadEntity entity = uploadRepository.save(new UltimoUploadEntity(
                ultimoUpload.cpf(), ultimoUpload.nomeArquivoOriginal(), ultimoUpload.data(),
                ultimoUpload.linhas(), ultimoUpload.substituidas(), ultimoUpload.novas()
        ));

        var chaves = ultimoUpload.chaves().stream()
                .map(c -> new UltimoUploadChaveEntity(null, entity, c.centroCusto(), c.codConta()))
                .toList();
        chaveRepository.saveAll(chaves);
    }
}
