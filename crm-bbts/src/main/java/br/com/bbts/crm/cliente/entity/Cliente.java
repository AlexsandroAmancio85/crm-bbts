package br.com.bbts.crm.cliente.entity;

import br.com.bbts.crm.common.enums.StatusCliente;
import br.com.bbts.crm.importacao.entity.ArquivoImportado;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 14)
    private String cpf;

    @Column(length = 150)
    private String propriedade;

    @Column(length = 100)
    private String cultura;

    @Column(length = 100)
    private String municipio;

    @Column(length = 30)
    private String telefone;

    @Column(length = 150)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCliente status;

    @Column(nullable = false)
    private boolean qualificado;

    // ===== Campos de venda (preenchidos pelo vendedor) =====
    @Column(length = 100)
    private String convenio;

    @Column
    private Integer prazo;

    @Column(name = "valor_vendido", precision = 14, scale = 2)
    private BigDecimal valorVendido;

    @Column(length = 1000)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arquivo_importado_id")
    private ArquivoImportado arquivoImportado;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        dataCriacao = now; dataAtualizacao = now;
        if (status == null) status = StatusCliente.PENDENTE;
    }
    @PreUpdate void preUpdate() { dataAtualizacao = LocalDateTime.now(); }
}
