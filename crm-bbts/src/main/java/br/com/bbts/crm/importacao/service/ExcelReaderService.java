package br.com.bbts.crm.importacao.service;

import br.com.bbts.crm.importacao.dto.LinhaPlanilha;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Etapa 2 do fluxo — "Importação e Leitura".
 * Lê a planilha (.xls/.xlsx) enviada pelo gerente e converte cada linha em um LinhaPlanilha,
 * já sinalizando problemas básicos (nome ausente, etc.) para a etapa de validação.
 *
 * Colunas esperadas (na primeira linha / cabeçalho, em qualquer ordem):
 * nome | propriedade | cultura | municipio | telefone | email
 */
@Service
public class ExcelReaderService {

    private static final List<String> COLUNAS_ESPERADAS =
            List.of("nome", "propriedade", "cultura", "municipio", "telefone", "email");

    public List<LinhaPlanilha> ler(InputStream inputStream) throws IOException {
        List<LinhaPlanilha> linhas = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            Row cabecalho = sheet.getRow(sheet.getFirstRowNum());
            var indices = mapearColunas(cabecalho, formatter);

            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || linhaVazia(row, formatter)) continue;

                String nome = valor(row, indices.get("nome"), formatter);
                String propriedade = valor(row, indices.get("propriedade"), formatter);
                String cultura = valor(row, indices.get("cultura"), formatter);
                String municipio = valor(row, indices.get("municipio"), formatter);
                String telefone = valor(row, indices.get("telefone"), formatter);
                String email = valor(row, indices.get("email"), formatter);

                String erro = !StringUtils.hasText(nome) ? "Nome do produtor é obrigatório" : null;

                linhas.add(new LinhaPlanilha(i + 1, nome, propriedade, cultura, municipio, telefone, email, erro));
            }
        }
        return linhas;
    }

    private java.util.Map<String, Integer> mapearColunas(Row cabecalho, DataFormatter formatter) {
        java.util.Map<String, Integer> indices = new java.util.HashMap<>();
        if (cabecalho != null) {
            for (Cell cell : cabecalho) {
                String texto = formatter.formatCellValue(cell).trim().toLowerCase();
                if (COLUNAS_ESPERADAS.contains(texto)) {
                    indices.put(texto, cell.getColumnIndex());
                }
            }
        }
        // fallback: se o cabeçalho não bater com o esperado, assume a ordem padrão das colunas
        for (int i = 0; i < COLUNAS_ESPERADAS.size(); i++) {
            indices.putIfAbsent(COLUNAS_ESPERADAS.get(i), i);
        }
        return indices;
    }

    private String valor(Row row, Integer indice, DataFormatter formatter) {
        if (indice == null) return null;
        Cell cell = row.getCell(indice);
        if (cell == null) return null;
        String texto = formatter.formatCellValue(cell).trim();
        return texto.isEmpty() ? null : texto;
    }

    private boolean linhaVazia(Row row, DataFormatter formatter) {
        for (Cell cell : row) {
            if (StringUtils.hasText(formatter.formatCellValue(cell))) return false;
        }
        return true;
    }
}
