package br.com.empresa.metas.usuario.core.domain.gateway;

import br.com.empresa.metas.usuario.core.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * "Porta" (port) de saída do módulo usuário — uma interface que o CORE
 * define, mas não implementa. Quem implementa é a camada infra
 * (UsuarioGatewayImpl, usando JPA/PostgreSQL). Essa inversão é o
 * coração da arquitetura hexagonal: o core diz "eu preciso conseguir
 * buscar e salvar um usuário", sem saber (nem se importar) se isso é
 * feito com Postgres, um arquivo, ou uma chamada HTTP pra outro serviço.
 */
public interface UsuarioGateway {

    Optional<Usuario> buscarPorCpf(String cpf);

    List<Usuario> listarTodos();

    Usuario salvar(Usuario usuario);

    boolean remover(String cpf);
}
