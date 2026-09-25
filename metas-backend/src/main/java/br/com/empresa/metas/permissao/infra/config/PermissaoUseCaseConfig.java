package br.com.empresa.metas.permissao.infra.config;

import br.com.empresa.metas.permissao.core.domain.gateway.PermissaoGateway;
import br.com.empresa.metas.permissao.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermissaoUseCaseConfig {

    @Bean
    public SalvarPermissaoUseCase salvarPermissaoUseCase(PermissaoGateway gateway) {
        return new SalvarPermissaoUseCase(gateway);
    }

    @Bean
    public RemoverPermissaoUseCase removerPermissaoUseCase(PermissaoGateway gateway) {
        return new RemoverPermissaoUseCase(gateway);
    }

    @Bean
    public ListarPermissoesUseCase listarPermissoesUseCase(PermissaoGateway gateway) {
        return new ListarPermissoesUseCase(gateway);
    }

    @Bean
    public VerificarPermissaoDeEscritaUseCase verificarPermissaoDeEscritaUseCase(PermissaoGateway gateway) {
        return new VerificarPermissaoDeEscritaUseCase(gateway);
    }

    @Bean
    public ObterPermissaoUseCase obterPermissaoUseCase(PermissaoGateway gateway) {
        return new ObterPermissaoUseCase(gateway);
    }

    @Bean
    public ImportarPermissoesUseCase importarPermissoesUseCase(PermissaoGateway gateway) {
        return new ImportarPermissoesUseCase(gateway);
    }
}
