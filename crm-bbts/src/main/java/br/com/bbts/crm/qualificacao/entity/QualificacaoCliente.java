package br.com.bbts.crm.qualificacao.entity;

import br.com.bbts.crm.cliente.entity.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "qualificacao_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualificacaoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private boolean aprovado;

    @Column(name = "motivo_rejeicao", length = 255)
    private String motivoRejeicao;

    @Column(name = "qualificado_por", length = 150)
    private String qualificadoPor;

    @Column(name = "data_qualificacao", nullable = false, updatable = false)
    private LocalDateTime dataQualificacao;

    @PrePersist
    void prePersist() {
        if (dataQualificacao == null) dataQualificacao = LocalDateTime.now();
    }
}
