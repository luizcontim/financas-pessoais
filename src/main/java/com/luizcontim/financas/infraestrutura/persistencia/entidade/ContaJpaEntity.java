package com.luizcontim.financas.infraestrutura.persistencia.entidade;

import jakarta.persistence.CascadeType;
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
@Table(name = "conta")
@Getter
@Setter
@NoArgsConstructor
public class ContaJpaEntity {

	@Id
	private UUID id;

	private String nome;

	private BigDecimal saldo;

	private String moeda;

	@OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MovimentacaoJpaEntity> movimentacoes = new ArrayList<>();
}
