package br.com.bbts.crm.oportunidade.entity;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.common.enums.StatusOportunidade;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "valor_estimado", precision = 14, scale = 2)
    private BigDecimal valorEstimado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOportunidade status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        if (status == null) status = StatusOportunidade.ABERTA;
    }
}
