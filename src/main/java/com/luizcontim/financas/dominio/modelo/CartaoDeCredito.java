package com.luizcontim.financas.dominio.modelo;

import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class CartaoDeCredito {

	private final UUID id;
	private final String nome;
	private final Bandeira bandeira;
	private final Dinheiro limite;
	private final int diaFechamento;
	private final int diaVencimento;
	private final List<Compra> compras = new ArrayList<>();
	private final List<Fatura> faturas = new ArrayList<>();

	private CartaoDeCredito(UUID id, String nome, Bandeira bandeira, Dinheiro limite, int diaFechamento, int diaVencimento,
			List<Compra> compras, List<Fatura> faturas) {
		this.id = Objects.requireNonNull(id);
		this.nome = Objects.requireNonNull(nome);
		this.bandeira = Objects.requireNonNull(bandeira);
		this.limite = Objects.requireNonNull(limite);
		this.diaFechamento = validarDia(diaFechamento);
		this.diaVencimento = validarDia(diaVencimento);
		this.compras.addAll(compras);
		this.faturas.addAll(faturas);
	}

	public static CartaoDeCredito emitir(String nome, Bandeira bandeira, Dinheiro limite, int diaFechamento, int diaVencimento) {
		return new CartaoDeCredito(UUID.randomUUID(), nome, bandeira, limite, diaFechamento, diaVencimento, List.of(), List.of());
	}

	public static CartaoDeCredito reconstruir(UUID id, String nome, Bandeira bandeira, Dinheiro limite, int diaFechamento,
			int diaVencimento, List<Compra> compras, List<Fatura> faturas) {
		return new CartaoDeCredito(id, nome, bandeira, limite, diaFechamento, diaVencimento, compras, faturas);
	}

	private static int validarDia(int dia) {
		if (dia < 1 || dia > 28) {
			throw new IllegalArgumentException("O dia deve estar entre 1 e 28, recebido: " + dia);
		}
		return dia;
	}

	/**
	 * Encapsula a regra central do domínio: uma compra parcelada gera uma
	 * {@link Parcela} em cada uma das próximas N faturas, a partir da fatura em
	 * que a compra efetivamente cai (considerando o dia de fechamento).
	 */
	public Compra registrarCompra(String descricao, Dinheiro valorTotal, Categoria categoria, LocalDate dataCompra, int quantidadeParcelas) {
		Compra compra = new Compra(UUID.randomUUID(), descricao, valorTotal, categoria, dataCompra, quantidadeParcelas);
		compras.add(compra);

		List<Dinheiro> valoresDasParcelas = dividirEmParcelas(valorTotal, quantidadeParcelas);
		YearMonth primeiraFatura = faturaDeReferenciaPara(dataCompra);

		for (int numero = 1; numero <= quantidadeParcelas; numero++) {
			YearMonth referencia = primeiraFatura.plusMonths(numero - 1L);
			Fatura fatura = obterOuAbrirFatura(referencia.getMonthValue(), referencia.getYear());
			Parcela parcela = new Parcela(UUID.randomUUID(), compra.id(), descricao, categoria, numero, quantidadeParcelas,
					valoresDasParcelas.get(numero - 1));
			fatura.adicionarParcela(parcela);
		}

		return compra;
	}

	private YearMonth faturaDeReferenciaPara(LocalDate dataCompra) {
		YearMonth mesDaCompra = YearMonth.from(dataCompra);
		return dataCompra.getDayOfMonth() > diaFechamento ? mesDaCompra.plusMonths(1) : mesDaCompra;
	}

	private Fatura obterOuAbrirFatura(int mes, int ano) {
		return faturas.stream()
				.filter(f -> f.referenteA(mes, ano))
				.findFirst()
				.orElseGet(() -> {
					Fatura novaFatura = Fatura.abrir(mes, ano);
					faturas.add(novaFatura);
					return novaFatura;
				});
	}

	private static List<Dinheiro> dividirEmParcelas(Dinheiro valorTotal, int quantidade) {
		BigDecimal valorBase = valorTotal.valor().divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.DOWN);
		List<Dinheiro> parcelas = new ArrayList<>();
		Dinheiro somaParcial = Dinheiro.ZERO;
		for (int i = 0; i < quantidade - 1; i++) {
			Dinheiro parcela = Dinheiro.de(valorBase);
			parcelas.add(parcela);
			somaParcial = somaParcial.somar(parcela);
		}
		// a última parcela absorve o resto da divisão, garantindo que a soma bata com o valor total
		parcelas.add(valorTotal.subtrair(somaParcial));
		return parcelas;
	}

	public Fatura fecharFatura(int mes, int ano) {
		Fatura fatura = buscarFatura(mes, ano);
		fatura.fechar();
		return fatura;
	}

	public Fatura fatura(int mes, int ano) {
		return buscarFatura(mes, ano);
	}

	private Fatura buscarFatura(int mes, int ano) {
		return faturas.stream()
				.filter(f -> f.referenteA(mes, ano))
				.findFirst()
				.orElseThrow(() -> new RecursoNaoEncontradoException(
						"Fatura %02d/%d não encontrada para o cartão %s".formatted(mes, ano, nome)));
	}

	public UUID id() {
		return id;
	}

	public String nome() {
		return nome;
	}

	public Bandeira bandeira() {
		return bandeira;
	}

	public Dinheiro limite() {
		return limite;
	}

	public int diaFechamento() {
		return diaFechamento;
	}

	public int diaVencimento() {
		return diaVencimento;
	}

	public List<Compra> compras() {
		return List.copyOf(compras);
	}

	public List<Fatura> faturas() {
		return List.copyOf(faturas);
	}
}
