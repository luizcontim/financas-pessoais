package com.luizcontim.financas.infraestrutura.persistencia.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioJpaEntity {

	@Id
	private UUID id;

	private String nome;

	private String email;

	@Column(name = "senha_hash")
	private String senhaHash;
}
