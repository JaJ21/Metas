package br.com.empresa.metas.meta.infra.controller;

import br.com.empresa.metas.meta.core.domain.model.CicloOrcamentario;
import br.com.empresa.metas.meta.core.domain.model.LinhaMetaInput;
import br.com.empresa.metas.meta.core.domain.model.ValorMesInput;
import br.com.empresa.metas.meta.core.usecase.*;
import br.com.empresa.metas.meta.infra.controller.dto.request.ConfirmarUploadRequest;
import br.com.empresa.metas.meta.infra.controller.dto.request.LinhaMetaRequest;
import br.com.empresa.metas.meta.infra.controller.dto.response.*;
import br.com.empresa.metas.meta.infra.controller.support.MesesLabel;
import br.com.empresa.metas.meta.infra.controller.support.MetaPlanilhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Endpoints do fluxo principal do sistema — equivalente às rotas
 * /upload, /preview/confirmar, /modelo e /ultimo-upload do sistema em
 * Python. Não tem @PreAuthorize de role aqui: qualquer usuário logado
 * (com JWT válido) pode usar — a permissão fina (quem pode escrever em
 * qual Centro de Custo + Cod Conta) é resolvida dentro dos casos de uso,
 * não no nível do controller.
 *
 * O CPF do usuário logado nunca vem de um parâmetro do request (isso
 * seria inseguro — a pessoa podia mandar o CPF de outra pessoa). Ele
 * vem do "principal" da autenticação (Authentication), que o Spring
 * Security já preencheu a partir do "sub" do JWT (ver SecurityConfig).
 */
@RestController
@RequestMapping("/metas")
public class MetaController {

    private final ImportarPlanilhaUseCase importarPlanilhaUseCase;
    private final ConfirmarUploadUseCase confirmarUploadUseCase;
    private final ObterUltimoUploadUseCase obterUltimoUploadUseCase;
    private final ObterMetaAtualUseCase obterMetaAtualUseCase;
    private final MetaPlanilhaService metaPlanilhaService;
    private final CicloOrcamentario ciclo;

    public MetaController(
            ImportarPlanilhaUseCase importarPlanilhaUseCase,
            ConfirmarUploadUseCase confirmarUploadUseCase,
            ObterUltimoUploadUseCase obterUltimoUploadUseCase,
            ObterMetaAtualUseCase obterMetaAtualUseCase,
            MetaPlanilhaService metaPlanilhaService,
            CicloOrcamentario ciclo
    ) {
        this.importarPlanilhaUseCase = importarPlanilhaUseCase;
        this.confirmarUploadUseCase = confirmarUploadUseCase;
        this.obterUltimoUploadUseCase = obterUltimoUploadUseCase;
        this.obterMetaAtualUseCase = obterMetaAtualUseCase;
        this.metaPlanilhaService = metaPlanilhaService;
        this.ciclo = ciclo;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadPreviewResponse upload(@RequestParam("arquivo") MultipartFile arquivo, Authentication auth) {
        List<LinhaMetaInput> linhas = metaPlanilhaService.importar(arquivo, ciclo);
        List<PreviewLinha> preview = importarPlanilhaUseCase.executar(linhas, ciclo, cpfLogado(auth));

        return new UploadPreviewResponse(
                CicloOrcamentarioResponse.from(ciclo),
                preview.stream().map(MetaPreviewResponse::from).toList()
        );
    }

    @PostMapping("/confirmar")
    public ResumoUploadResponse confirmar(@Valid @RequestBody ConfirmarUploadRequest request, Authentication auth) {
        List<LinhaMetaInput> linhas = request.linhas().stream()
                .map(this::converterParaInput)
                .toList();

        var resumo = confirmarUploadUseCase.executar(linhas, ciclo, cpfLogado(auth), request.nomeArquivoOriginal());
        return new ResumoUploadResponse(resumo.substituidas(), resumo.novas());
    }

    @GetMapping("/modelo")
    public ResponseEntity<byte[]> baixarModelo() {
        byte[] arquivo = metaPlanilhaService.gerarModelo(ciclo);
        return responderArquivo(arquivo, "modelo_planilha.xlsx");
    }

    @GetMapping("/ultimo-upload")
    public ResponseEntity<UltimoUploadResponse> obterUltimoUpload(Authentication auth) {
        return obterUltimoUploadUseCase.executar(cpfLogado(auth))
                .map(u -> ResponseEntity.ok(UltimoUploadResponse.from(u)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/ultimo-upload/download")
    public ResponseEntity<byte[]> baixarUltimoUpload(Authentication auth) {
        var ultimoUpload = obterUltimoUploadUseCase.executar(cpfLogado(auth)).orElse(null);
        if (ultimoUpload == null) {
            return ResponseEntity.notFound().build();
        }
        var metas = obterMetaAtualUseCase.executar(ultimoUpload.chaves());
        byte[] arquivo = metaPlanilhaService.gerarDownload(metas, ciclo);
        return responderArquivo(arquivo, "ultimo_upload_" + cpfLogado(auth) + ".xlsx");
    }

    private LinhaMetaInput converterParaInput(LinhaMetaRequest request) {
        List<ValorMesInput> valores = new ArrayList<>();
        adicionarValores(valores, request.real(), MesesLabel.real(ciclo));
        adicionarValores(valores, request.forecast(), MesesLabel.forecast(ciclo));
        adicionarValores(valores, request.orcado(), MesesLabel.orcado(ciclo));
        return new LinhaMetaInput(request.centroCusto(), request.codConta(), valores, request.justificativa());
    }

    private void adicionarValores(List<ValorMesInput> destino, Map<Integer, String> valoresBrutos, List<MesesLabel.MesRef> referencias) {
        if (valoresBrutos == null) return;
        for (MesesLabel.MesRef ref : referencias) {
            String bruto = valoresBrutos.getOrDefault(ref.mes(), "0");
            destino.add(new ValorMesInput(ref.ano(), ref.mes(), bruto));
        }
    }

    private String cpfLogado(Authentication auth) {
        return auth.getName(); // preenchido a partir do claim "sub" do JWT — ver SecurityConfig
    }

    private ResponseEntity<byte[]> responderArquivo(byte[] arquivo, String nome) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nome)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(arquivo);
    }
}
