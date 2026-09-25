package br.com.empresa.metas.permissao.infra.persistence.repository;

import br.com.empresa.metas.permissao.infra.persistence.entity.PermissaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissaoJpaRepository extends JpaRepository<PermissaoEntity, Long> {

    Optional<PermissaoEntity> findByCpfAndCentroCustoAndCodConta(String cpf, String centroCusto, String codConta);

    List<PermissaoEntity> findByCpfAndEscritaTrue(String cpf);

    List<PermissaoEntity> findByCpfAndLeituraTrue(String cpf);

    void deleteByCpfAndCentroCustoAndCodConta(String cpf, String centroCusto, String codConta);

    boolean existsByCpfAndCentroCustoAndCodConta(String cpf, String centroCusto, String codConta);
}
