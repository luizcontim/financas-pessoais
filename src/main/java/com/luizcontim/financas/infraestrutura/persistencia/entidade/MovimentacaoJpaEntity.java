package com.luizcontim.financas.infraestrutura.persistencia.entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "movimentacao")
@Getter
@Setter
@NoArgsConstructor
public class MovimentacaoJpaEntity {

	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conta_id", nullable = false)
	private ContaJpaEntity conta;

	private String tipo;

	private String descricao;

	private BigDecimal valor;

	private String categoria;

	@jakarta.persistence.Column(name = "data_movimentacao")
	private LocalDate data;
}
