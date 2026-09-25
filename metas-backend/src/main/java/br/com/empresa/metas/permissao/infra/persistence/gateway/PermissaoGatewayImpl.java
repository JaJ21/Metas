package br.com.empresa.metas.permissao.infra.persistence.gateway;

import br.com.empresa.metas.permissao.core.domain.gateway.PermissaoGateway;
import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import br.com.empresa.metas.permissao.core.domain.model.Permissao;
import br.com.empresa.metas.permissao.infra.persistence.entity.PermissaoEntity;
import br.com.empresa.metas.permissao.infra.persistence.mapper.PermissaoMapper;
import br.com.empresa.metas.permissao.infra.persistence.repository.PermissaoJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
public class PermissaoGatewayImpl implements PermissaoGateway {

    private final PermissaoJpaRepository repository;

    public PermissaoGatewayImpl(PermissaoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permissao> listarTodas() {
        return repository.findAll().stream().map(PermissaoMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permissao> buscar(String cpf, ChaveCentroConta chave) {
        return repository.findByCpfAndCentroCustoAndCodConta(cpf, chave.centroCusto(), chave.codConta())
                .map(PermissaoMapper::toDomain);
    }

    @Override
    public Permissao salvar(Permissao permissao) {
        Long idExistente = repository
                .findByCpfAndCentroCustoAndCodConta(permissao.getCpf(), permissao.getChave().centroCusto(), permissao.getChave().codConta())
                .map(PermissaoEntity::getId)
                .orElse(null);

        PermissaoEntity salvo = repository.save(PermissaoMapper.toEntity(permissao, idExistente));
        return PermissaoMapper.toDomain(salvo);
    }

    @Override
    public boolean remover(String cpf, ChaveCentroConta chave) {
        boolean existia = repository.existsByCpfAndCentroCustoAndCodConta(cpf, chave.centroCusto(), chave.codConta());
        if (existia) {
            repository.deleteByCpfAndCentroCustoAndCodConta(cpf, chave.centroCusto(), chave.codConta());
        }
        return existia;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ChaveCentroConta> paresComPermissaoDeEscrita(String cpf) {
        return repository.findByCpfAndEscritaTrue(cpf).stream()
                .map(e -> new ChaveCentroConta(e.getCentroCusto(), e.getCodConta()))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ChaveCentroConta> paresComPermissaoDeLeitura(String cpf) {
        return repository.findByCpfAndLeituraTrue(cpf).stream()
                .map(e -> new ChaveCentroConta(e.getCentroCusto(), e.getCodConta()))
                .collect(Collectors.toSet());
    }

    @Override
    public void substituirTudo(List<Permissao> novasPermissoes) {
        // Apaga tudo e grava de novo — comportamento intencionalmente
        // destrutivo (mesmo do sistema em Python), documentado no
        // endpoint de importação.
        repository.deleteAll();
        List<PermissaoEntity> entidades = novasPermissoes.stream()
                .map(p -> PermissaoMapper.toEntity(p, null))
                .toList();
        repository.saveAll(entidades);
    }
}
