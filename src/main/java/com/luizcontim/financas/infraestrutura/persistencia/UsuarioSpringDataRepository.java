package com.luizcontim.financas.infraestrutura.persistencia;

import com.luizcontim.financas.infraestrutura.persistencia.entidade.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface UsuarioSpringDataRepository extends JpaRepository<UsuarioJpaEntity, UUID> {

	Optional<UsuarioJpaEntity> findByEmail(String email);
}
