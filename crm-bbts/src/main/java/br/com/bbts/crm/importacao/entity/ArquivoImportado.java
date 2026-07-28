package br.com.bbts.crm.importacao.entity;

import br.com.bbts.crm.common.enums.StatusImportacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "arquivo_importado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArquivoImportado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(name = "linhas_lidas", nullable = false)
    private int linhasLidas;

    @Column(name = "linhas_validas", nullable = false)
    private int linhasValidas;

    @Column(name = "linhas_com_erro", nullable = false)
    private int linhasComErro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusImportacao status;

    @Column(name = "data_importacao", nullable = false, updatable = false)
    private LocalDateTime dataImportacao;

    @PrePersist
    void prePersist() {
        if (dataImportacao == null) dataImportacao = LocalDateTime.now();
        if (status == null) status = StatusImportacao.AGUARDANDO_VALIDACAO;
    }
}
