package com.luizcontim.financas.dominio.modelo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class Movimentacao {

	private final UUID id;
	private final TipoMovimentacao tipo;
	private final String descricao;
	private final Dinheiro valor;
	private final Categoria categoria;
	private final LocalDate data;

	public Movimentacao(UUID id, TipoMovimentacao tipo, String descricao, Dinheiro valor, Categoria categoria, LocalDate data) {
		this.id = Objects.requireNonNull(id);
		this.tipo = Objects.requireNonNull(tipo);
		this.descricao = Objects.requireNonNull(descricao);
		this.valor = Objects.requireNonNull(valor);
		this.categoria = Objects.requireNonNull(categoria);
		this.data = Objects.requireNonNull(data);
		if (valor.negativo() || valor.compareTo(Dinheiro.ZERO) == 0) {
			throw new IllegalArgumentException("O valor de uma movimentação deve ser positivo");
		}
	}

	public Dinheiro impactoNoSaldo() {
		return tipo == TipoMovimentacao.ENTRADA ? valor : valor.multiplicarPor(-1);
	}

	public UUID id() {
		return id;
	}

	public TipoMovimentacao tipo() {
		return tipo;
	}

	public String descricao() {
		return descricao;
	}

	public Dinheiro valor() {
		return valor;
	}

	public Categoria categoria() {
		return categoria;
	}

	public LocalDate data() {
		return data;
	}
}
