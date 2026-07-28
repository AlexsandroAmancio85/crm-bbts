package br.com.bbts.crm.logprodutividade.entity;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.vendedor.entity.Vendedor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_produtividade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LogProdutividade {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /** ABERTURA, CONTATADO, INDISPONIVEL, VENDIDO, AGENDAMENTO */
    @Column(name = "tipo_atividade", nullable = false, length = 50)
    private String tipoAtividade;

    @Column(length = 500)
    private String observacao;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    @PrePersist void prePersist() { if (dataHora == null) dataHora = LocalDateTime.now(); }
}
