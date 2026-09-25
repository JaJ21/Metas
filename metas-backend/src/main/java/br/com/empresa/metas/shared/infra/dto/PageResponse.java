package br.com.empresa.metas.shared.infra.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Formato padrão de resposta paginada, usado por QUALQUER endpoint que
 * devolve uma lista grande (ex: listar permissões, listar metas). Em vez
 * de cada controller inventar seu próprio formato de paginação, todos
 * usam este DTO — o front-end trata todas as listas do sistema da mesma
 * forma.
 *
 * Um "record" em Java é uma forma enxuta de declarar uma classe que só
 * carrega dados (sem lógica) — o compilador já gera construtor,
 * getters, equals/hashCode e toString sozinho. É o equivalente Java mais
 * próximo de um "dataclass" do Python, ou de um dict fixo.
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
