package br.com.empresa.metas.meta.infra.persistence.repository;

import br.com.empresa.metas.meta.infra.persistence.entity.MetaValorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaValorJpaRepository extends JpaRepository<MetaValorEntity, Long> {
    List<MetaValorEntity> findByMeta_Id(Long metaId);
    void deleteByMeta_Id(Long metaId);
}
