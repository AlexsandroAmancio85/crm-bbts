package br.com.bbts.crm.contato.repository;

import br.com.bbts.crm.contato.entity.ContatoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContatoClienteRepository extends JpaRepository<ContatoCliente, Long> {
    List<ContatoCliente> findByClienteIdOrderByDataContatoDesc(Long clienteId);
}
