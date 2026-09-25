package br.com.empresa.metas.usuario.infra.persistence.repository;

import br.com.empresa.metas.usuario.infra.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA — só uma interface, o Spring gera a
 * implementação sozinho em tempo de execução, com base no nome dos
 * métodos herdados de JpaRepository (findById, save, deleteById, etc).
 */
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, String> {
}
