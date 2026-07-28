package br.com.bbts.crm.importacao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "log_importacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogImportacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arquivo_importado_id", nullable = false)
    private ArquivoImportado arquivoImportado;

    @Column(nullable = false)
    private int linha;

    @Column(name = "mensagem_erro", nullable = false, length = 500)
    private String mensagemErro;
}
