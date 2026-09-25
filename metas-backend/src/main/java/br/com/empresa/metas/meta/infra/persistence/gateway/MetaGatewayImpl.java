package br.com.empresa.metas.meta.infra.persistence.gateway;

import br.com.empresa.metas.meta.core.domain.gateway.MetaGateway;
import br.com.empresa.metas.meta.core.domain.model.Meta;
import br.com.empresa.metas.meta.core.domain.model.ValorMensal;
import br.com.empresa.metas.meta.infra.persistence.entity.MetaEntity;
import br.com.empresa.metas.meta.infra.persistence.entity.MetaValorEntity;
import br.com.empresa.metas.meta.infra.persistence.mapper.MetaMapper;
import br.com.empresa.metas.meta.infra.persistence.repository.MetaJpaRepository;
import br.com.empresa.metas.meta.infra.persistence.repository.MetaValorJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementação real do upsert de metas. Equivalente a aplicar_upsert()
 * do excel_utils.py: pra cada Meta da lista, busca se já existe uma
 * MetaEntity pra aquele par (Centro de Custo + Cod Conta) — se sim, é
 * uma SUBSTITUIÇÃO (apaga os valores mensais antigos e grava os novos);
 * se não, é uma linha NOVA.
 */
@Component
@Transactional
public class MetaGatewayImpl implements MetaGateway {

    private final MetaJpaRepository metaRepository;
    private final MetaValorJpaRepository valorRepository;

    public MetaGatewayImpl(MetaJpaRepository metaRepository, MetaValorJpaRepository valorRepository) {
        this.metaRepository = metaRepository;
        this.valorRepository = valorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Meta> buscar(String centroCusto, String codConta) {
        return metaRepository.findByCentroCustoAndCodConta(centroCusto, codConta)
                .map(entity -> MetaMapper.toDomain(entity, valorRepository.findByMeta_Id(entity.getId())));
    }

    @Override
    public ResultadoUpsert salvarOuAtualizar(List<Meta> metas, String cpf) {
        int substituidas = 0;
        int novas = 0;
        Instant agora = Instant.now();

        for (Meta meta : metas) {
            Optional<MetaEntity> existente = metaRepository.findByCentroCustoAndCodConta(meta.getCentroCusto(), meta.getCodConta());

            MetaEntity entity;
            if (existente.isPresent()) {
                entity = existente.get();
                entity.setJustificativa(meta.getJustificativa());
                entity.setCpfUltimaAlteracao(cpf);
                entity.setDataUltimaAlteracao(agora);
                metaRepository.save(entity);
                // Apaga os valores mensais antigos e grava os novos por completo — mais simples e seguro
                // do que tentar calcular diffs mês a mês, e o volume de meses é pequeno (no máximo ~24).
                valorRepository.deleteByMeta_Id(entity.getId());
                substituidas++;
            } else {
                entity = metaRepository.save(new MetaEntity(
                        null, meta.getCentroCusto(), meta.getCodConta(), meta.getJustificativa(), cpf, agora
                ));
                novas++;
            }

            List<MetaValorEntity> novosValores = meta.getValores().stream()
                    // ano/mes são int no domínio, mas short na entidade (coluna SMALLINT).
                    .map(v -> new MetaValorEntity(null, entity, (short) v.ano(), (short) v.mes(), v.tipo().name(), v.valor()))
                    .toList();
            valorRepository.saveAll(novosValores);
        }

        return new ResultadoUpsert(substituidas, novas);
    }
}
