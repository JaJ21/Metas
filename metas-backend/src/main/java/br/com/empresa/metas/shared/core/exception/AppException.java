package br.com.empresa.metas.shared.core.exception;

/**
 * Classe-base de TODAS as exceções de negócio do sistema. Fica em
 * "shared/core" (não em "infra") porque é usada pelos casos de uso
 * (camada core) de qualquer módulo — e o core não pode depender de nada
 * do Spring/framework, então essa exceção também não depende.
 *
 * Cada subtipo carrega um "code" — um identificador curto e estável
 * (ex: "META_NAO_ENCONTRADA") que o front-end pode usar pra tratar o
 * erro de forma programática, sem depender do texto da mensagem (que
 * pode mudar).
 */
public abstract class AppException extends RuntimeException {

    private final String code;

    protected AppException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
