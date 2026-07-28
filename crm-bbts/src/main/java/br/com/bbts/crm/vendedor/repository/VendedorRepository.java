package br.com.bbts.crm.vendedor.repository;

import br.com.bbts.crm.vendedor.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
}
