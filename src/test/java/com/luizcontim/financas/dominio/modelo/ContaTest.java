package com.luizcontim.financas.dominio.modelo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ContaTest {

	@Test
	void deveAumentarSaldoAoRegistrarEntrada() {
		Conta conta = Conta.abrir("Conta Corrente");

		conta.registrarEntrada("Salário", Dinheiro.de("5000"), Categoria.OUTROS, LocalDate.now());

		assertThat(conta.saldo().valor()).isEqualByComparingTo("5000.00");
	}

	@Test
	void deveDiminuirSaldoAoRegistrarSaida() {
		Conta conta = Conta.abrir("Conta Corrente");
		conta.registrarEntrada("Salário", Dinheiro.de("5000"), Categoria.OUTROS, LocalDate.now());

		conta.registrarSaida("Aluguel", Dinheiro.de("1500"), Categoria.MORADIA, LocalDate.now());

		assertThat(conta.saldo().valor()).isEqualByComparingTo("3500.00");
		assertThat(conta.movimentacoes()).hasSize(2);
	}
}
