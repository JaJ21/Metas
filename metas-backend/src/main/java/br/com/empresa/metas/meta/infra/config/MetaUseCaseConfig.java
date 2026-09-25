package br.com.empresa.metas.meta.infra.config;

import br.com.empresa.metas.meta.core.domain.gateway.MetaGateway;
import br.com.empresa.metas.meta.core.domain.gateway.UltimoUploadGateway;
import br.com.empresa.metas.meta.core.domain.model.CicloOrcamentario;
import br.com.empresa.metas.meta.core.usecase.*;
import br.com.empresa.metas.permissao.core.usecase.ObterPermissaoUseCase;
import br.com.empresa.metas.permissao.core.usecase.VerificarPermissaoDeEscritaUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetaUseCaseConfig {

    /**
     * O único lugar do sistema em que os valores de application.yml
     * (metas.ano-realizado, etc) são lidos e transformados no objeto de
     * domínio CicloOrcamentario — que dali pra frente é só passado por
     * parâmetro/construtor, sem ninguém mais precisar saber que isso
     * vem de configuração.
     */
    @Bean
    public CicloOrcamentario cicloOrcamentario(
            @Value("${metas.ano-realizado}") int anoRealizado,
            @Value("${metas.ano-orcamento}") int anoOrcamento,
            @Value("${metas.mes-corte-real}") int mesCorteReal
    ) {
        return new CicloOrcamentario(anoRealizado, anoOrcamento, mesCorteReal);
    }

    @Bean
    public ImportarPlanilhaUseCase importarPlanilhaUseCase(
            MetaGateway metaGateway,
            VerificarPermissaoDeEscritaUseCase verificarPermissaoDeEscritaUseCase,
            ObterPermissaoUseCase obterPermissaoUseCase
    ) {
        return new ImportarPlanilhaUseCase(metaGateway, verificarPermissaoDeEscritaUseCase, obterPermissaoUseCase);
    }

    @Bean
    public ConfirmarUploadUseCase confirmarUploadUseCase(
            MetaGateway metaGateway,
            UltimoUploadGateway ultimoUploadGateway,
            VerificarPermissaoDeEscritaUseCase verificarPermissaoDeEscritaUseCase
    ) {
        return new ConfirmarUploadUseCase(metaGateway, ultimoUploadGateway, verificarPermissaoDeEscritaUseCase);
    }

    @Bean
    public ObterUltimoUploadUseCase obterUltimoUploadUseCase(UltimoUploadGateway ultimoUploadGateway) {
        return new ObterUltimoUploadUseCase(ultimoUploadGateway);
    }

    @Bean
    public ObterMetaAtualUseCase obterMetaAtualUseCase(MetaGateway metaGateway) {
        return new ObterMetaAtualUseCase(metaGateway);
    }
}
