package br.com.bbts.crm.cliente.repository;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.common.enums.StatusCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {

    List<Cliente> findByQualificadoFalse();

    List<Cliente> findByVendedorId(Long vendedorId);

    List<Cliente> findByQualificadoTrueAndVendedorIsNull();

    long countByVendedorId(Long vendedorId);

    long countByStatus(StatusCliente status);

    long countByQualificadoTrue();
}
