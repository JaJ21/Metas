package br.com.empresa.metas.meta.infra.persistence.repository;

import br.com.empresa.metas.meta.infra.persistence.entity.MetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetaJpaRepository extends JpaRepository<MetaEntity, Long> {
    Optional<MetaEntity> findByCentroCustoAndCodConta(String centroCusto, String codConta);
}
