package com.luizcontim.financas.dominio.modelo;

import com.luizcontim.financas.dominio.excecao.FaturaFechadaException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartaoDeCreditoTest {

	private final CartaoDeCredito cartao = CartaoDeCredito.emitir("Nubank", "Mastercard", Dinheiro.de("5000"), 10, 17);

	@Test
	void compraAntesDoFechamentoCaiNaFaturaDoMesCorrente() {
		LocalDate dataCompra = LocalDate.of(2026, Month.AUGUST, 5);

		cartao.registrarCompra("Supermercado", Dinheiro.de("300"), Categoria.ALIMENTACAO, dataCompra, 1);

		Fatura fatura = cartao.fatura(8, 2026);
		assertThat(fatura.parcelas()).hasSize(1);
		assertThat(fatura.valorTotal().valor()).isEqualByComparingTo("300.00");
	}

	@Test
	void compraAposFechamentoCaiNaFaturaDoMesSeguinte() {
		LocalDate dataCompra = LocalDate.of(2026, Month.AUGUST, 15);

		cartao.registrarCompra("Livraria", Dinheiro.de("100"), Categoria.EDUCACAO, dataCompra, 1);

		Fatura fatura = cartao.fatura(9, 2026);
		assertThat(fatura.parcelas()).hasSize(1);
	}

	@Test
	void deveDistribuirCompraParceladaEmFaturasConsecutivas() {
		LocalDate dataCompra = LocalDate.of(2026, Month.AUGUST, 5);

		cartao.registrarCompra("Notebook", Dinheiro.de("1000"), Categoria.OUTROS, dataCompra, 3);

		assertThat(cartao.fatura(8, 2026).parcelas()).hasSize(1);
		assertThat(cartao.fatura(9, 2026).parcelas()).hasSize(1);
		assertThat(cartao.fatura(10, 2026).parcelas()).hasSize(1);

		Dinheiro somaDasParcelas = cartao.fatura(8, 2026).valorTotal()
				.somar(cartao.fatura(9, 2026).valorTotal())
				.somar(cartao.fatura(10, 2026).valorTotal());
		assertThat(somaDasParcelas.valor()).isEqualByComparingTo("1000.00");
	}

	@Test
	void deveAbsorverArredondamentoNaUltimaParcela() {
		LocalDate dataCompra = LocalDate.of(2026, Month.AUGUST, 5);

		cartao.registrarCompra("Presente", Dinheiro.de("100"), Categoria.LAZER, dataCompra, 3);

		assertThat(cartao.fatura(8, 2026).valorTotal().valor()).isEqualByComparingTo("33.33");
		assertThat(cartao.fatura(9, 2026).valorTotal().valor()).isEqualByComparingTo("33.33");
		assertThat(cartao.fatura(10, 2026).valorTotal().valor()).isEqualByComparingTo("33.34");
	}

	@Test
	void naoDevePermitirNovaCompraEmFaturaJaFechada() {
		LocalDate dataCompra = LocalDate.of(2026, Month.AUGUST, 5);
		cartao.registrarCompra("Mercado", Dinheiro.de("200"), Categoria.ALIMENTACAO, dataCompra, 1);
		cartao.fecharFatura(8, 2026);

		Parcela novaParcela = new Parcela(UUID.randomUUID(), UUID.randomUUID(), "Extra", Categoria.OUTROS, 1, 1, Dinheiro.de("10"));

		assertThatThrownBy(() -> cartao.fatura(8, 2026).adicionarParcela(novaParcela))
				.isInstanceOf(FaturaFechadaException.class);
	}
}
