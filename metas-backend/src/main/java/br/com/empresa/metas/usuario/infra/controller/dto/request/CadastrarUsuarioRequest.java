package br.com.empresa.metas.usuario.infra.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO de entrada do endpoint de cadastro. "DTO" = Data Transfer Object:
 * uma classe só pra carregar dados de um lado pro outro (aqui, do JSON
 * da requisição pro controller) — não é a mesma coisa que o Model de
 * domínio nem que a Entity do banco, cada camada tem o seu.
 *
 * As anotações @NotBlank/@Pattern são do Bean Validation — o Spring
 * confere elas automaticamente quando o parâmetro do controller tem
 * @Valid na frente, e devolve erro 422 sozinho se algo não bater (ver
 * GlobalExceptionHandler.handleBeanValidation).
 *
 * "senha" é opcional (pode vir null) — mesmo comportamento do sistema
 * em Python: se vier em branco, não mexe na senha/dados já salvos,
 * exceto quando é um cadastro novo (senha se torna obrigatória nesse
 * caso, checado no controller).
 */
public record CadastrarUsuarioRequest(
        @NotBlank @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos numéricos") String cpf,
        @NotBlank String nome,
        String cargo,
        String senha,
        boolean admin
) {
}
