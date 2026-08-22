package com.luizcontim.financas.infraestrutura.persistencia.entidade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cartao_credito")
@Getter
@Setter
@NoArgsConstructor
public class CartaoCreditoJpaEntity {

	@Id
	private UUID id;

	private String nome;

	private String bandeira;

	private BigDecimal limite;

	@Column(name = "dia_fechamento")
	private int diaFechamento;

	@Column(name = "dia_vencimento")
	private int diaVencimento;

	@OneToMany(mappedBy = "cartao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<CompraJpaEntity> compras = new ArrayList<>();

	@OneToMany(mappedBy = "cartao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<FaturaJpaEntity> faturas = new ArrayList<>();
}
