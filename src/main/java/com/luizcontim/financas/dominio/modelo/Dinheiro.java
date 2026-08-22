package com.luizcontim.financas.dominio.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Dinheiro(BigDecimal valor, String moeda) implements Comparable<Dinheiro> {

	public static final String MOEDA_PADRAO = "BRL";
	public static final Dinheiro ZERO = Dinheiro.de(BigDecimal.ZERO);

	public Dinheiro {
		Objects.requireNonNull(valor, "valor não pode ser nulo");
		Objects.requireNonNull(moeda, "moeda não pode ser nula");
		valor = valor.setScale(2, RoundingMode.HALF_UP);
	}

	public static Dinheiro de(BigDecimal valor) {
		return new Dinheiro(valor, MOEDA_PADRAO);
	}

	public static Dinheiro de(String valor) {
		return de(new BigDecimal(valor));
	}

	public Dinheiro somar(Dinheiro outro) {
		validarMesmaMoeda(outro);
		return new Dinheiro(this.valor.add(outro.valor), this.moeda);
	}

	public Dinheiro subtrair(Dinheiro outro) {
		validarMesmaMoeda(outro);
		return new Dinheiro(this.valor.subtract(outro.valor), this.moeda);
	}

	public Dinheiro multiplicarPor(int fator) {
		return new Dinheiro(this.valor.multiply(BigDecimal.valueOf(fator)), this.moeda);
	}

	public boolean maiorQue(Dinheiro outro) {
		return compareTo(outro) > 0;
	}

	public boolean negativo() {
		return valor.signum() < 0;
	}

	private void validarMesmaMoeda(Dinheiro outro) {
		if (!this.moeda.equals(outro.moeda)) {
			throw new IllegalArgumentException(
					"Não é possível operar valores em moedas diferentes: %s e %s".formatted(this.moeda, outro.moeda));
		}
	}

	@Override
	public int compareTo(Dinheiro outro) {
		validarMesmaMoeda(outro);
		return this.valor.compareTo(outro.valor);
	}

	@Override
	public String toString() {
		return "%s %s".formatted(moeda, valor);
	}
}
