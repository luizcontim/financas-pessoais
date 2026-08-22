package com.luizcontim.financas.dominio.modelo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class Compra {

	private final UUID id;
	private final String descricao;
	private final Dinheiro valorTotal;
	private final Categoria categoria;
	private final LocalDate data;
	private final int quantidadeParcelas;

	public Compra(UUID id, String descricao, Dinheiro valorTotal, Categoria categoria, LocalDate data, int quantidadeParcelas) {
		this.id = Objects.requireNonNull(id);
		this.descricao = Objects.requireNonNull(descricao);
		this.valorTotal = Objects.requireNonNull(valorTotal);
		this.categoria = Objects.requireNonNull(categoria);
		this.data = Objects.requireNonNull(data);
		if (quantidadeParcelas < 1) {
			throw new IllegalArgumentException("Uma compra deve ter ao menos 1 parcela");
		}
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public UUID id() {
		return id;
	}

	public String descricao() {
		return descricao;
	}

	public Dinheiro valorTotal() {
		return valorTotal;
	}

	public Categoria categoria() {
		return categoria;
	}

	public LocalDate data() {
		return data;
	}

	public int quantidadeParcelas() {
		return quantidadeParcelas;
	}
}
