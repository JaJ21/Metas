package br.com.empresa.metas.meta.infra.persistence.repository;

import br.com.empresa.metas.meta.infra.persistence.entity.UltimoUploadChaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UltimoUploadChaveJpaRepository extends JpaRepository<UltimoUploadChaveEntity, Long> {
    List<UltimoUploadChaveEntity> findByUltimoUpload_Cpf(String cpf);
    void deleteByUltimoUpload_Cpf(String cpf);
}
