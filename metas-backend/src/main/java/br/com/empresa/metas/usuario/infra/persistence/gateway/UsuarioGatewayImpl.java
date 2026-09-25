package br.com.empresa.metas.usuario.infra.persistence.gateway;

import br.com.empresa.metas.usuario.core.domain.gateway.UsuarioGateway;
import br.com.empresa.metas.usuario.core.domain.model.Usuario;
import br.com.empresa.metas.usuario.infra.persistence.entity.UsuarioEntity;
import br.com.empresa.metas.usuario.infra.persistence.mapper.UsuarioMapper;
import br.com.empresa.metas.usuario.infra.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação REAL da porta UsuarioGateway, usando PostgreSQL via
 * Spring Data JPA. É essa classe que "pluga" o core na infraestrutura —
 * o core só conhece a interface (UsuarioGateway); é o Spring quem
 * injeta ESTA implementação onde a interface é pedida (ver
 * infra/config/UsuarioUseCaseConfig.java).
 */
@Component
@Transactional
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioJpaRepository repository;

    public UsuarioGatewayImpl(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return repository.findById(cpf).map(UsuarioMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return repository.findAll().stream().map(UsuarioMapper::toDomain).toList();
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity salvo = repository.save(UsuarioMapper.toEntity(usuario));
        return UsuarioMapper.toDomain(salvo);
    }

    @Override
    public boolean remover(String cpf) {
        if (!repository.existsById(cpf)) {
            return false;
        }
        repository.deleteById(cpf);
        return true;
    }
}
