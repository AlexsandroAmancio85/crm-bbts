package br.com.bbts.crm.importacao.service;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.cliente.repository.ClienteRepository;
import br.com.bbts.crm.common.enums.StatusCliente;
import br.com.bbts.crm.common.enums.StatusImportacao;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.importacao.dto.LinhaPlanilha;
import br.com.bbts.crm.importacao.entity.ArquivoImportado;
import br.com.bbts.crm.importacao.entity.LogImportacao;
import br.com.bbts.crm.importacao.repository.ArquivoImportadoRepository;
import br.com.bbts.crm.importacao.repository.LogImportacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Orquestra as etapas 1-3 do fluxo: Recebimento da Base -> Importação e Leitura -> Validação da Base.
 */
@Service
@RequiredArgsConstructor
public class ImportacaoService {

    private final ExcelReaderService excelReaderService;
    private final ArquivoImportadoRepository arquivoImportadoRepository;
    private final LogImportacaoRepository logImportacaoRepository;
    private final ClienteRepository clienteRepository;

    public List<ArquivoImportado> listar() {
        return arquivoImportadoRepository.findAll();
    }

    @Transactional
    public ArquivoImportado importar(MultipartFile arquivo) throws IOException {
        List<LinhaPlanilha> linhas = excelReaderService.ler(arquivo.getInputStream());

        ArquivoImportado importado = ArquivoImportado.builder()
                .nomeArquivo(arquivo.getOriginalFilename())
                .linhasLidas(linhas.size())
                .linhasValidas((int) linhas.stream().filter(LinhaPlanilha::valida).count())
                .linhasComErro((int) linhas.stream().filter(l -> !l.valida()).count())
                .status(StatusImportacao.AGUARDANDO_VALIDACAO)
                .build();
        importado = arquivoImportadoRepository.save(importado);

        for (LinhaPlanilha linha : linhas) {
            if (linha.valida()) {
                Cliente cliente = Cliente.builder()
                        .nome(linha.nome())
                        .propriedade(linha.propriedade())
                        .cultura(linha.cultura())
                        .municipio(linha.municipio())
                        .telefone(linha.telefone())
                        .email(linha.email())
                        .status(StatusCliente.PENDENTE)
                        .qualificado(false)
                        .arquivoImportado(importado)
                        .build();
                clienteRepository.save(cliente);
            } else {
                LogImportacao log = LogImportacao.builder()
                        .arquivoImportado(importado)
                        .linha(linha.numeroLinha())
                        .mensagemErro(linha.erro())
                        .build();
                logImportacaoRepository.save(log);
            }
        }
        return importado;
    }

    @Transactional
    public ArquivoImportado validar(Long id) {
        ArquivoImportado importado = obter(id);
        importado.setStatus(importado.getLinhasComErro() > 0 ? StatusImportacao.COM_ERRO : StatusImportacao.VALIDADO);
        return arquivoImportadoRepository.save(importado);
    }

    public List<LogImportacao> obterLog(Long id) {
        obter(id);
        return logImportacaoRepository.findByArquivoImportadoId(id);
    }

    private ArquivoImportado obter(Long id) {
        return arquivoImportadoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Importação", id));
    }
}
