package com.luizcontim.financas.dominio.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Conta {

	private final UUID id;
	private final UUID usuarioId;
	private final String nome;
	private Dinheiro saldo;
	private final List<Movimentacao> movimentacoes = new ArrayList<>();

	private Conta(UUID id, UUID usuarioId, String nome, Dinheiro saldo, List<Movimentacao> movimentacoes) {
		this.id = Objects.requireNonNull(id);
		this.usuarioId = Objects.requireNonNull(usuarioId);
		this.nome = Objects.requireNonNull(nome);
		this.saldo = Objects.requireNonNull(saldo);
		this.movimentacoes.addAll(movimentacoes);
	}

	public static Conta abrir(String nome, UUID usuarioId) {
		return new Conta(UUID.randomUUID(), usuarioId, nome, Dinheiro.ZERO, List.of());
	}

	public static Conta reconstruir(UUID id, UUID usuarioId, String nome, Dinheiro saldo, List<Movimentacao> movimentacoes) {
		return new Conta(id, usuarioId, nome, saldo, movimentacoes);
	}

	public Movimentacao registrarEntrada(String descricao, Dinheiro valor, Categoria categoria, LocalDate data) {
		return registrar(TipoMovimentacao.ENTRADA, descricao, valor, categoria, data);
	}

	public Movimentacao registrarSaida(String descricao, Dinheiro valor, Categoria categoria, LocalDate data) {
		return registrar(TipoMovimentacao.SAIDA, descricao, valor, categoria, data);
	}

	private Movimentacao registrar(TipoMovimentacao tipo, String descricao, Dinheiro valor, Categoria categoria, LocalDate data) {
		Movimentacao movimentacao = new Movimentacao(UUID.randomUUID(), tipo, descricao, valor, categoria, data);
		movimentacoes.add(movimentacao);
		saldo = saldo.somar(movimentacao.impactoNoSaldo());
		return movimentacao;
	}

	public UUID id() {
		return id;
	}

	public UUID usuarioId() {
		return usuarioId;
	}

	public String nome() {
		return nome;
	}

	public Dinheiro saldo() {
		return saldo;
	}

	public List<Movimentacao> movimentacoes() {
		return List.copyOf(movimentacoes);
	}
}
