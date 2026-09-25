package br.com.empresa.metas.meta.core.domain.model;

import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;

import java.time.Instant;
import java.util.List;

/**
 * Resumo do último upload confirmado de um CPF — equivalente ao
 * ultimos_uploads.xlsx do sistema em Python. Guarda também QUAIS pares
 * (Centro de Custo + Cod Conta) fizeram parte desse upload, pra permitir
 * reconstruir/baixar de novo consultando os valores atuais desses pares.
 *
 * Diferença importante em relação ao sistema em Python: lá o arquivo
 * .xlsx exato enviado ficava em cache; aqui, como tudo virou banco
 * relacional, o "baixar de novo" busca os valores ATUAIS desses mesmos
 * pares no banco (que podem ter mudado desde então, se outra pessoa
 * também alterou). Vale deixar isso documentado pro usuário final.
 */
public record UltimoUpload(
        String cpf,
        String nomeArquivoOriginal,
        Instant data,
        int linhas,
        int substituidas,
        int novas,
        List<ChaveCentroConta> chaves
) {
}
