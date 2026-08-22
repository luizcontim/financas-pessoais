package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;
import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;
import com.luizcontim.financas.dominio.repositorio.CartaoRepositorio;

import java.util.UUID;

public class FecharFaturaUseCase {

	private final CartaoRepositorio cartaoRepositorio;

	public FecharFaturaUseCase(CartaoRepositorio cartaoRepositorio) {
		this.cartaoRepositorio = cartaoRepositorio;
	}

	public void executar(UUID cartaoId, int mes, int ano) {
		CartaoDeCredito cartao = cartaoRepositorio.buscarPorId(cartaoId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Cartão não encontrado: " + cartaoId));

		cartao.fecharFatura(mes, ano);
		cartaoRepositorio.salvar(cartao);
	}
}
