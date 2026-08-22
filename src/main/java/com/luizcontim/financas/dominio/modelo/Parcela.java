package com.luizcontim.financas.dominio.modelo;

import java.util.Objects;
import java.util.UUID;

public final class Parcela {

	private final UUID id;
	private final UUID compraId;
	private final String descricao;
	private final Categoria categoria;
	private final int numero;
	private final int totalParcelas;
	private final Dinheiro valor;

	public Parcela(UUID id, UUID compraId, String descricao, Categoria categoria, int numero, int totalParcelas, Dinheiro valor) {
		this.id = Objects.requireNonNull(id);
		this.compraId = Objects.requireNonNull(compraId);
		this.descricao = Objects.requireNonNull(descricao);
		this.categoria = Objects.requireNonNull(categoria);
		this.numero = numero;
		this.totalParcelas = totalParcelas;
		this.valor = Objects.requireNonNull(valor);
	}

	public UUID id() {
		return id;
	}

	public UUID compraId() {
		return compraId;
	}

	public String descricao() {
		return descricao;
	}

	public Categoria categoria() {
		return categoria;
	}

	public int numero() {
		return numero;
	}

	public int totalParcelas() {
		return totalParcelas;
	}

	public Dinheiro valor() {
		return valor;
	}
}
