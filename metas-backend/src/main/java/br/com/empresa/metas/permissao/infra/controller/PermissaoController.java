package br.com.empresa.metas.permissao.infra.controller;

import br.com.empresa.metas.permissao.core.domain.model.Permissao;
import br.com.empresa.metas.permissao.core.usecase.*;
import br.com.empresa.metas.permissao.infra.controller.dto.request.PermissaoRequest;
import br.com.empresa.metas.permissao.infra.controller.dto.response.PermissaoResponse;
import br.com.empresa.metas.permissao.infra.controller.support.PermissaoPlanilhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Endpoints de administração de permissões — equivalente à tela
 * /admin/permissoes do sistema em Python (a parte de permissões; o
 * cadastro de usuário fica em UsuarioController). Tudo aqui exige a
 * role ADMIN.
 */
@RestController
@RequestMapping("/permissoes")
@PreAuthorize("hasRole('ADMIN')")
public class PermissaoController {

    private final SalvarPermissaoUseCase salvarPermissaoUseCase;
    private final RemoverPermissaoUseCase removerPermissaoUseCase;
    private final ListarPermissoesUseCase listarPermissoesUseCase;
    private final ImportarPermissoesUseCase importarPermissoesUseCase;
    private final PermissaoPlanilhaService permissaoPlanilhaService;

    public PermissaoController(
            SalvarPermissaoUseCase salvarPermissaoUseCase,
            RemoverPermissaoUseCase removerPermissaoUseCase,
            ListarPermissoesUseCase listarPermissoesUseCase,
            ImportarPermissoesUseCase importarPermissoesUseCase,
            PermissaoPlanilhaService permissaoPlanilhaService
    ) {
        this.salvarPermissaoUseCase = salvarPermissaoUseCase;
        this.removerPermissaoUseCase = removerPermissaoUseCase;
        this.listarPermissoesUseCase = listarPermissoesUseCase;
        this.importarPermissoesUseCase = importarPermissoesUseCase;
        this.permissaoPlanilhaService = permissaoPlanilhaService;
    }

    @PostMapping
    public PermissaoResponse salvar(@Valid @RequestBody PermissaoRequest request) {
        Permissao permissao = salvarPermissaoUseCase.executar(
                request.cpf(), request.centroCusto(), request.codConta(),
                request.read(), request.write(), request.delete()
        );
        return PermissaoResponse.from(permissao);
    }

    @GetMapping
    public List<PermissaoResponse> listar() {
        return listarPermissoesUseCase.executar().stream().map(PermissaoResponse::from).toList();
    }

    @DeleteMapping
    public ResponseEntity<Void> remover(
            @RequestParam String cpf, @RequestParam String centroCusto, @RequestParam String codConta
    ) {
        boolean removeu = removerPermissaoUseCase.executar(cpf, centroCusto, codConta);
        return removeu ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> importar(@RequestParam("arquivo") MultipartFile arquivo) {
        List<Permissao> permissoes = permissaoPlanilhaService.importar(arquivo);
        int quantidade = importarPermissoesUseCase.executar(permissoes);
        return Map.of("importadas", quantidade, "mensagem", "Tabela de permissões substituída com sucesso.");
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar() {
        byte[] arquivo = permissaoPlanilhaService.exportar(listarPermissoesUseCase.executar());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=permissoes.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(arquivo);
    }
}
