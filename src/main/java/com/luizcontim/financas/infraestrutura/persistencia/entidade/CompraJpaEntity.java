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
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "compra")
@Getter
@Setter
@NoArgsConstructor
public class CompraJpaEntity {

	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cartao_id", nullable = false)
	private CartaoCreditoJpaEntity cartao;

	private String descricao;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	private String categoria;

	@Column(name = "data_compra")
	private LocalDate data;

	@Column(name = "quantidade_parcelas")
	private int quantidadeParcelas;
}
