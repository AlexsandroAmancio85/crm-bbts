package br.com.bbts.crm.logsistema.entity;

import br.com.bbts.crm.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_sistema")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LogSistema {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 80)
    private String acao;

    @Column(nullable = false, length = 60)
    private String entidade;

    @Column(name = "entidade_id")
    private Long entidadeId;

    @Column(length = 2000)
    private String detalhes;

    @Column(length = 45)
    private String ip;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    @PrePersist void prePersist() { if (dataHora == null) dataHora = LocalDateTime.now(); }
}
