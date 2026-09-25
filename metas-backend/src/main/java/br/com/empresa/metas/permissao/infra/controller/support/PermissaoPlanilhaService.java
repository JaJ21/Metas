package br.com.empresa.metas.permissao.infra.controller.support;

import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import br.com.empresa.metas.permissao.core.domain.model.Permissao;
import br.com.empresa.metas.permissao.core.exception.PlanilhaPermissaoInvalidaException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Leitura e escrita da planilha de permissões (.xlsx), usando Apache
 * POI. Fica na camada INFRA de propósito (não no core) — ler/escrever
 * um formato de arquivo específico (Excel) é um detalhe técnico, não uma
 * regra de negócio. O core só lida com a lista de objetos Permissao já
 * prontos; quem sabe transformar isso de/para bytes de um .xlsx é esta
 * classe.
 *
 * Equivalente a permissoes.importar_planilha() / permissoes.exportar
 * (via Flask send_file) do sistema em Python.
 */
@Component
public class PermissaoPlanilhaService {

    private static final List<String> COLUNAS = List.of("CPF", "Centro de Custo", "Cod Conta", "Read", "Write", "Delete");

    public List<Permissao> importar(MultipartFile arquivo) {
        try (InputStream in = arquivo.getInputStream(); Workbook workbook = WorkbookFactory.create(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            validarCabecalho(sheet);

            List<Permissao> permissoes = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || valorTexto(row, 0).isBlank()) continue;

                String cpf = valorTexto(row, 0);
                String centroCusto = valorTexto(row, 1);
                String codConta = valorTexto(row, 2);
                boolean read = valorBooleano(row, 3);
                boolean write = valorBooleano(row, 4);
                boolean delete = valorBooleano(row, 5);

                permissoes.add(new Permissao(cpf, new ChaveCentroConta(centroCusto, codConta), read, write, delete));
            }
            return permissoes;
        } catch (IOException e) {
            throw new PlanilhaPermissaoInvalidaException("Não foi possível ler o arquivo: " + e.getMessage());
        }
    }

    public byte[] exportar(List<Permissao> permissoes) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("permissoes");

            Row cabecalho = sheet.createRow(0);
            for (int i = 0; i < COLUNAS.size(); i++) {
                cabecalho.createCell(i).setCellValue(COLUNAS.get(i));
            }

            int linha = 1;
            for (Permissao p : permissoes) {
                Row row = sheet.createRow(linha++);
                row.createCell(0).setCellValue(p.getCpf());
                row.createCell(1).setCellValue(p.getChave().centroCusto());
                row.createCell(2).setCellValue(p.getChave().codConta());
                row.createCell(3).setCellValue(p.isRead() ? "Sim" : "Não");
                row.createCell(4).setCellValue(p.isWrite() ? "Sim" : "Não");
                row.createCell(5).setCellValue(p.isDelete() ? "Sim" : "Não");
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao gerar planilha de permissões.", e);
        }
    }

    private void validarCabecalho(Sheet sheet) {
        Row cabecalho = sheet.getRow(0);
        if (cabecalho == null) {
            throw new PlanilhaPermissaoInvalidaException("Planilha sem cabeçalho.");
        }
        for (int i = 0; i < COLUNAS.size(); i++) {
            String esperado = COLUNAS.get(i);
            String encontrado = valorTexto(cabecalho, i);
            if (!esperado.equalsIgnoreCase(encontrado)) {
                throw new PlanilhaPermissaoInvalidaException(
                        "Coluna " + (i + 1) + " deveria ser '" + esperado + "', encontrado '" + encontrado + "'."
                );
            }
        }
    }

    private String valorTexto(Row row, int coluna) {
        Cell cell = row.getCell(coluna);
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private boolean valorBooleano(Row row, int coluna) {
        String valor = valorTexto(row, coluna).trim().toLowerCase();
        return valor.equals("1") || valor.equals("true") || valor.equals("sim") || valor.equals("s") || valor.equals("x");
    }
}
