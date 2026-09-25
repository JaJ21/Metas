package br.com.empresa.metas.meta.infra.controller.support;

import br.com.empresa.metas.meta.core.domain.model.CicloOrcamentario;
import br.com.empresa.metas.meta.core.domain.model.LinhaMetaInput;
import br.com.empresa.metas.meta.core.domain.model.Meta;
import br.com.empresa.metas.meta.core.domain.model.ValorMesInput;
import br.com.empresa.metas.meta.core.domain.enums.TipoValorMes;
import br.com.empresa.metas.shared.core.exception.ValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Leitura/escrita do arquivo .xlsx de metas (Apache POI). Equivalente a
 * ler_e_validar_planilha() (a parte de LEITURA do arquivo — a validação
 * de negócio em si é feita depois, pelo core) e gerar_modelo_bytes() do
 * excel_utils.py original.
 */
@Component
public class MetaPlanilhaService {

    private static final String COL_CENTRO_CUSTO = "Centro de Custo";
    private static final String COL_COD_CONTA = "Cod Conta";
    private static final String COL_JUSTIFICATIVA = "Justificativa";

    public List<LinhaMetaInput> importar(MultipartFile arquivo, CicloOrcamentario ciclo) {
        List<MesesLabel.MesRef> meses = MesesLabel.todos(ciclo);

        try (InputStream in = arquivo.getInputStream(); Workbook workbook = WorkbookFactory.create(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> colunaPorNome = mapearColunas(sheet, meses);

            List<LinhaMetaInput> linhas = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String centroCusto = valorTexto(row, colunaPorNome.get(COL_CENTRO_CUSTO));
                if (row == null || centroCusto.isBlank()) continue;

                String codConta = valorTexto(row, colunaPorNome.get(COL_COD_CONTA));
                String justificativa = valorTexto(row, colunaPorNome.get(COL_JUSTIFICATIVA));

                List<ValorMesInput> valores = new ArrayList<>();
                for (MesesLabel.MesRef mesRef : meses) {
                    Integer coluna = colunaPorNome.get(mesRef.label());
                    String bruto = valorTexto(row, coluna);
                    valores.add(new ValorMesInput(mesRef.ano(), mesRef.mes(), bruto));
                }

                linhas.add(new LinhaMetaInput(centroCusto, codConta, valores, justificativa));
            }

            if (linhas.isEmpty()) {
                throw new ValidationException("PLANILHA_VAZIA", "A planilha está vazia.");
            }
            return linhas;
        } catch (IOException e) {
            throw new ValidationException("PLANILHA_ILEGIVEL", "Não foi possível ler o arquivo Excel: " + e.getMessage());
        }
    }

    public byte[] gerarModelo(CicloOrcamentario ciclo) {
        List<String> colunas = colunasCompletas(ciclo);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("modelo");
            escreverCabecalho(sheet, colunas);

            // Duas linhas de exemplo, com zero em todos os meses — igual ao gerar_modelo_bytes() original.
            for (int linha = 1; linha <= 2; linha++) {
                Row row = sheet.createRow(linha);
                row.createCell(0).setCellValue(linha == 1 ? "1001" : "1002");
                row.createCell(1).setCellValue("30001");
                for (int i = 2; i < colunas.size() - 1; i++) {
                    row.createCell(i).setCellValue(0);
                }
                row.createCell(colunas.size() - 1).setCellValue("");
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao gerar planilha modelo.", e);
        }
    }

    /** Gera o .xlsx de download com os valores ATUAIS (do banco) das metas informadas — usado por "baixar último upload". */
    public byte[] gerarDownload(List<Meta> metas, CicloOrcamentario ciclo) {
        List<MesesLabel.MesRef> meses = MesesLabel.todos(ciclo);
        List<String> colunas = colunasCompletas(ciclo);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("ultimo_upload");
            escreverCabecalho(sheet, colunas);

            int linhaIdx = 1;
            for (Meta meta : metas) {
                Row row = sheet.createRow(linhaIdx++);
                row.createCell(0).setCellValue(meta.getCentroCusto());
                row.createCell(1).setCellValue(meta.getCodConta());
                for (int i = 0; i < meses.size(); i++) {
                    MesesLabel.MesRef mesRef = meses.get(i);
                    var valor = meta.getValores().stream()
                            .filter(v -> v.ano() == mesRef.ano() && v.mes() == mesRef.mes())
                            .findFirst();
                    row.createCell(2 + i).setCellValue(valor.map(v -> v.valor().doubleValue()).orElse(0.0));
                }
                row.createCell(colunas.size() - 1).setCellValue(meta.getJustificativa());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao gerar planilha de download.", e);
        }
    }

    private List<String> colunasCompletas(CicloOrcamentario ciclo) {
        List<String> colunas = new ArrayList<>();
        colunas.add(COL_CENTRO_CUSTO);
        colunas.add(COL_COD_CONTA);
        MesesLabel.todos(ciclo).forEach(m -> colunas.add(m.label()));
        colunas.add(COL_JUSTIFICATIVA);
        return colunas;
    }

    private void escreverCabecalho(Sheet sheet, List<String> colunas) {
        Row cabecalho = sheet.createRow(0);
        for (int i = 0; i < colunas.size(); i++) {
            cabecalho.createCell(i).setCellValue(colunas.get(i));
        }
    }

    private Map<String, Integer> mapearColunas(Sheet sheet, List<MesesLabel.MesRef> meses) {
        Row cabecalho = sheet.getRow(0);
        if (cabecalho == null) {
            throw new ValidationException("PLANILHA_INVALIDA", "A planilha não tem cabeçalho.");
        }

        Map<String, Integer> mapa = new java.util.HashMap<>();
        for (Cell cell : cabecalho) {
            cell.setCellType(CellType.STRING);
            mapa.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        }

        List<String> esperadas = new ArrayList<>(List.of(COL_CENTRO_CUSTO, COL_COD_CONTA));
        meses.forEach(m -> esperadas.add(m.label()));
        esperadas.add(COL_JUSTIFICATIVA);

        List<String> faltando = esperadas.stream().filter(c -> !mapa.containsKey(c)).toList();
        if (!faltando.isEmpty()) {
            throw new ValidationException("PLANILHA_INVALIDA",
                    "A planilha não segue o modelo padrão. Colunas faltando: " + String.join(", ", faltando));
        }
        return mapa;
    }

    private String valorTexto(Row row, Integer coluna) {
        if (row == null || coluna == null) return "";
        Cell cell = row.getCell(coluna);
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            double numero = cell.getNumericCellValue();
            // Evita que um valor inteiro (ex: 1001) vire "1001.0" ao ler como texto.
            return numero == Math.floor(numero) ? String.valueOf((long) numero) : String.valueOf(numero);
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
}
