package br.com.empresa.metas.shared.infra.web;

import br.com.empresa.metas.shared.core.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Captura QUALQUER exceção lançada por qualquer controller do sistema e
 * transforma numa resposta HTTP padronizada, no formato RFC 7807
 * ("Problem Details for HTTP APIs") — é o formato exigido pelo padrão de
 * engenharia. Assim, o front-end sempre recebe o erro no mesmo "shape",
 * não importa qual módulo ou qual tipo de erro foi.
 *
 * @RestControllerAdvice faz esse tratamento valer pra TODOS os
 * @RestController do projeto automaticamente — não precisa repetir
 * try/catch em cada endpoint.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getCode(), ex.getMessage(), req);
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex, HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getCode(), ex.getMessage(), req);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbidden(ForbiddenException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, ex.getCode(), ex.getMessage(), req);
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getCode(), ex.getMessage(), req);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, ex.getCode(), ex.getMessage(), req);
    }

    /**
     * Erros de validação do Bean Validation (@Valid nos DTOs de request)
     * — por exemplo, um campo @NotBlank que veio vazio. Junta todos os
     * erros de campo numa mensagem só, pra a pessoa corrigir tudo de
     * uma vez em vez de descobrir um erro por vez.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBeanValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String detalhe = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatarErroDeCampo)
                .collect(Collectors.joining("; "));
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "REQUEST_INVALIDO", detalhe, req);
    }

    /**
     * Qualquer outra exceção não prevista (bug, erro de infraestrutura,
     * etc) cai aqui — nunca deixamos o stack trace vazar pro cliente,
     * só uma mensagem genérica de erro interno.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenerico(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "ERRO_INTERNO",
                "Ocorreu um erro inesperado. Tente novamente ou contate o suporte.", req);
    }

    private String formatarErroDeCampo(FieldError erro) {
        return erro.getField() + ": " + erro.getDefaultMessage();
    }

    private ProblemDetail build(HttpStatus status, String code, String message, HttpServletRequest req) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, message);
        problem.setTitle(code);
        problem.setInstance(URI.create(req.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("code", code);
        return problem;
    }
}
