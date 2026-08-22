package com.luizcontim.financas.infraestrutura.persistencia;

import com.luizcontim.financas.infraestrutura.persistencia.entidade.CartaoCreditoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface CartaoCreditoSpringDataRepository extends JpaRepository<CartaoCreditoJpaEntity, UUID> {
}
