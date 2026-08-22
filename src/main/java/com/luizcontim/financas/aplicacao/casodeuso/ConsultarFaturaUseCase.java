package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.FaturaOutput;
import com.luizcontim.financas.aplicacao.dto.ParcelaOutput;
import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;
import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;
import com.luizcontim.financas.dominio.modelo.Fatura;
import com.luizcontim.financas.dominio.repositorio.CartaoRepositorio;

import java.util.List;
import java.util.UUID;

public class ConsultarFaturaUseCase {

	private final CartaoRepositorio cartaoRepositorio;

	public ConsultarFaturaUseCase(CartaoRepositorio cartaoRepositorio) {
		this.cartaoRepositorio = cartaoRepositorio;
	}

	public FaturaOutput executar(UUID cartaoId, int mes, int ano) {
		CartaoDeCredito cartao = cartaoRepositorio.buscarPorId(cartaoId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Cartão não encontrado: " + cartaoId));

		Fatura fatura = cartao.fatura(mes, ano);

		List<ParcelaOutput> parcelas = fatura.parcelas().stream()
				.map(p -> new ParcelaOutput(p.id(), p.compraId(), p.descricao(), p.categoria().name(), p.numero(), p.totalParcelas(), p.valor().valor()))
				.toList();

		return new FaturaOutput(cartaoId, fatura.mesReferencia(), fatura.anoReferencia(), fatura.status().name(),
				fatura.valorTotal().valor(), parcelas);
	}
}
