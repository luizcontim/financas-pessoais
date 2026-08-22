package com.luizcontim.financas.dominio.modelo;

import com.luizcontim.financas.dominio.excecao.FaturaFechadaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Fatura {

	private final UUID id;
	private final int mesReferencia;
	private final int anoReferencia;
	private StatusFatura status;
	private final List<Parcela> parcelas = new ArrayList<>();

	private Fatura(UUID id, int mesReferencia, int anoReferencia, StatusFatura status, List<Parcela> parcelas) {
		this.id = Objects.requireNonNull(id);
		this.mesReferencia = mesReferencia;
		this.anoReferencia = anoReferencia;
		this.status = Objects.requireNonNull(status);
		this.parcelas.addAll(parcelas);
	}

	public static Fatura abrir(int mesReferencia, int anoReferencia) {
		return new Fatura(UUID.randomUUID(), mesReferencia, anoReferencia, StatusFatura.ABERTA, List.of());
	}

	public static Fatura reconstruir(UUID id, int mesReferencia, int anoReferencia, StatusFatura status, List<Parcela> parcelas) {
		return new Fatura(id, mesReferencia, anoReferencia, status, parcelas);
	}

	public void adicionarParcela(Parcela parcela) {
		if (status != StatusFatura.ABERTA) {
			throw new FaturaFechadaException(
					"Não é possível adicionar lançamentos à fatura %02d/%d, status atual: %s".formatted(mesReferencia, anoReferencia, status));
		}
		parcelas.add(parcela);
	}

	public void fechar() {
		if (status != StatusFatura.ABERTA) {
			throw new FaturaFechadaException("Fatura %02d/%d já está %s".formatted(mesReferencia, anoReferencia, status));
		}
		status = StatusFatura.FECHADA;
	}

	public Dinheiro valorTotal() {
		return parcelas.stream().map(Parcela::valor).reduce(Dinheiro.ZERO, Dinheiro::somar);
	}

	public boolean referenteA(int mes, int ano) {
		return this.mesReferencia == mes && this.anoReferencia == ano;
	}

	public UUID id() {
		return id;
	}

	public int mesReferencia() {
		return mesReferencia;
	}

	public int anoReferencia() {
		return anoReferencia;
	}

	public StatusFatura status() {
		return status;
	}

	public List<Parcela> parcelas() {
		return List.copyOf(parcelas);
	}
}
