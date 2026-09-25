package br.com.empresa.metas.meta.infra.controller.support;

import br.com.empresa.metas.meta.core.domain.model.CicloOrcamentario;

import java.util.ArrayList;
import java.util.List;

/**
 * Monta os rótulos de coluna da planilha ("Jan/2026", "Fev/2026", ...)
 * a partir do CicloOrcamentario — é um detalhe de FORMATO DE ARQUIVO
 * (infra), não regra de negócio, por isso fica aqui e não no core.
 * Equivalente às listas MESES_REAL/MESES_FORECAST/MESES_ORCADO do
 * config.py original.
 */
public final class MesesLabel {

    private static final String[] NOMES = {
            "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"
    };

    private MesesLabel() {}

    public record MesRef(int ano, int mes, String label) {}

    public static List<MesRef> real(CicloOrcamentario ciclo) {
        List<MesRef> lista = new ArrayList<>();
        for (int mes = 1; mes <= ciclo.mesCorteReal(); mes++) {
            lista.add(new MesRef(ciclo.anoRealizado(), mes, rotulo(mes, ciclo.anoRealizado())));
        }
        return lista;
    }

    public static List<MesRef> forecast(CicloOrcamentario ciclo) {
        List<MesRef> lista = new ArrayList<>();
        for (int mes = ciclo.mesCorteReal() + 1; mes <= 12; mes++) {
            lista.add(new MesRef(ciclo.anoRealizado(), mes, rotulo(mes, ciclo.anoRealizado())));
        }
        return lista;
    }

    public static List<MesRef> orcado(CicloOrcamentario ciclo) {
        List<MesRef> lista = new ArrayList<>();
        for (int mes = 1; mes <= 12; mes++) {
            lista.add(new MesRef(ciclo.anoOrcamento(), mes, rotulo(mes, ciclo.anoOrcamento())));
        }
        return lista;
    }

    public static List<MesRef> todos(CicloOrcamentario ciclo) {
        List<MesRef> lista = new ArrayList<>(real(ciclo));
        lista.addAll(forecast(ciclo));
        lista.addAll(orcado(ciclo));
        return lista;
    }

    private static String rotulo(int mes, int ano) {
        return NOMES[mes - 1] + "/" + ano;
    }
}
