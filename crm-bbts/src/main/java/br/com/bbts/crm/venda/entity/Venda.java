package br.com.bbts.crm.venda.entity;

import br.com.bbts.crm.cliente.entity.Cliente;
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
@Table(name = "venda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(length = 500)
    private String observacao;

    @Column(name = "data_venda", nullable = false, updatable = false)
    private LocalDateTime dataVenda;

    @PrePersist
    void prePersist() {
        if (dataVenda == null) dataVenda = LocalDateTime.now();
    }
}
