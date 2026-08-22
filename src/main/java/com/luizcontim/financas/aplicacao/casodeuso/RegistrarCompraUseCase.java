package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.RegistrarCompraInput;
import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;
import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;
import com.luizcontim.financas.dominio.modelo.Dinheiro;
import com.luizcontim.financas.dominio.repositorio.CartaoRepositorio;

public class RegistrarCompraUseCase {

	private final CartaoRepositorio cartaoRepositorio;

	public RegistrarCompraUseCase(CartaoRepositorio cartaoRepositorio) {
		this.cartaoRepositorio = cartaoRepositorio;
	}

	public void executar(RegistrarCompraInput input) {
		CartaoDeCredito cartao = cartaoRepositorio.buscarPorId(input.cartaoId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Cartão não encontrado: " + input.cartaoId()));

		cartao.registrarCompra(input.descricao(), Dinheiro.de(input.valorTotal()), input.categoria(), input.dataCompra(),
				input.quantidadeParcelas());

		cartaoRepositorio.salvar(cartao);
	}
}
