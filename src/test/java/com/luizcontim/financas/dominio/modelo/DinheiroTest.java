package com.luizcontim.financas.dominio.modelo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DinheiroTest {

	@Test
	void deveSomarValoresNaMesmaMoeda() {
		Dinheiro resultado = Dinheiro.de("10.50").somar(Dinheiro.de("5.25"));
		assertThat(resultado.valor()).isEqualByComparingTo("15.75");
	}

	@Test
	void deveArredondarParaDuasCasasDecimais() {
		Dinheiro dinheiro = Dinheiro.de(new BigDecimal("10.005"));
		assertThat(dinheiro.valor()).isEqualByComparingTo("10.01");
	}

	@Test
	void naoDeveOperarMoedasDiferentes() {
		Dinheiro real = Dinheiro.de("10");
		Dinheiro dolar = new Dinheiro(BigDecimal.TEN, "USD");
		assertThatThrownBy(() -> real.somar(dolar)).isInstanceOf(IllegalArgumentException.class);
	}
}
