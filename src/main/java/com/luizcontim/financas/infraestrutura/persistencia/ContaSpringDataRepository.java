package com.luizcontim.financas.infraestrutura.persistencia;

import com.luizcontim.financas.infraestrutura.persistencia.entidade.ContaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ContaSpringDataRepository extends JpaRepository<ContaJpaEntity, UUID> {
}
