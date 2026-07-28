package br.com.bbts.crm.contato.entity;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "contato_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContatoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @Column(nullable = false, length = 30)
    private String canal;

    @Column(length = 500)
    private String observacao;

    @Column(name = "data_contato", nullable = false, updatable = false)
    private LocalDateTime dataContato;

    @PrePersist
    void prePersist() {
        if (dataContato == null) dataContato = LocalDateTime.now();
        if (canal == null) canal = "TELEFONE";
    }
}
