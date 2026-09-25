package br.com.empresa.metas.usuario.infra.persistence.mapper;

import br.com.empresa.metas.usuario.core.domain.model.Usuario;
import br.com.empresa.metas.usuario.infra.persistence.entity.UsuarioEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converte entre a Entity (formato do banco) e o Model de domínio
 * (formato que o core entende). Feito manualmente (métodos estáticos
 * simples), sem MapStruct — o padrão pede que o core não dependa de
 * bibliotecas de mapeamento "mágicas"; como são poucos campos, escrever
 * na mão é simples e fácil de debugar.
 */
public final class UsuarioMapper {

    private UsuarioMapper() {}

    public static Usuario toDomain(UsuarioEntity entity) {
        List<String> roles = (entity.getRoles() == null || entity.getRoles().isBlank())
                ? Collections.emptyList()
                : Arrays.stream(entity.getRoles().split(",")).map(String::trim).collect(Collectors.toList());

        return new Usuario(
                entity.getCpf(),
                entity.getNome(),
                entity.getCargo(),
                entity.getSenhaHash(),
                roles,
                entity.getDataCadastro()
        );
    }

    public static UsuarioEntity toEntity(Usuario domain) {
        String roles = domain.getRoles() == null ? "" : String.join(",", domain.getRoles());
        return new UsuarioEntity(
                domain.getCpf(),
                domain.getNome(),
                domain.getCargo(),
                domain.getSenhaHash(),
                roles,
                domain.getDataCadastro()
        );
    }
}
