package br.com.bbts.crm.agenda.entity;

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
@Table(name = "retorno_agendado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetornoAgendado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;

    @Column(name = "data_agendada", nullable = false)
    private LocalDateTime dataAgendada;

    @Column(nullable = false)
    private boolean concluido;

    @Column(length = 500)
    private String observacao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
    }
}
