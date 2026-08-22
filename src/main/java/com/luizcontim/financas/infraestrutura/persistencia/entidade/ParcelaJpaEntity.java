package com.luizcontim.financas.infraestrutura.persistencia.entidade;

import jakarta.persistence.Column;
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
import java.util.UUID;

@Entity
@Table(name = "parcela")
@Getter
@Setter
@NoArgsConstructor
public class ParcelaJpaEntity {

	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fatura_id", nullable = false)
	private FaturaJpaEntity fatura;

	@Column(name = "compra_id")
	private UUID compraId;

	private String descricao;

	private String categoria;

	private int numero;

	@Column(name = "total_parcelas")
	private int totalParcelas;

	private BigDecimal valor;
}
