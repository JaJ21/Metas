package br.com.empresa.metas.usuario.infra.controller;

import br.com.empresa.metas.usuario.core.domain.model.Usuario;
import br.com.empresa.metas.usuario.core.usecase.*;
import br.com.empresa.metas.usuario.infra.controller.dto.request.CadastrarUsuarioRequest;
import br.com.empresa.metas.usuario.infra.controller.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de cadastro de usuário — equivalente à parte de "Cadastrar
 * usuário" da tela /admin/permissoes do sistema em Python. Só quem tem
 * a role ADMIN pode usar (@PreAuthorize).
 */
@RestController
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final CadastrarOuAtualizarUsuarioUseCase cadastrarOuAtualizarUsuarioUseCase;
    private final RemoverUsuarioUseCase removerUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;

    public UsuarioController(
            CadastrarOuAtualizarUsuarioUseCase cadastrarOuAtualizarUsuarioUseCase,
            RemoverUsuarioUseCase removerUsuarioUseCase,
            ListarUsuariosUseCase listarUsuariosUseCase,
            BuscarUsuarioUseCase buscarUsuarioUseCase
    ) {
        this.cadastrarOuAtualizarUsuarioUseCase = cadastrarOuAtualizarUsuarioUseCase;
        this.removerUsuarioUseCase = removerUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.buscarUsuarioUseCase = buscarUsuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrarOuAtualizar(@Valid @RequestBody CadastrarUsuarioRequest request) {
        List<String> roles = request.admin() ? List.of("ADMIN") : List.of();
        Usuario usuario = cadastrarOuAtualizarUsuarioUseCase.executar(
                request.cpf(), request.nome(), request.cargo(), request.senha(), roles
        );
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioResponse.from(usuario));
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return listarUsuariosUseCase.executar().stream().map(UsuarioResponse::from).toList();
    }

    @GetMapping("/{cpf}")
    public UsuarioResponse buscar(@PathVariable String cpf) {
        return UsuarioResponse.from(buscarUsuarioUseCase.executar(cpf));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> remover(@PathVariable String cpf) {
        boolean removeu = removerUsuarioUseCase.executar(cpf);
        return removeu ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
