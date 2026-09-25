package br.com.empresa.metas.meta.infra.persistence.repository;

import br.com.empresa.metas.meta.infra.persistence.entity.UltimoUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UltimoUploadJpaRepository extends JpaRepository<UltimoUploadEntity, String> {
}
