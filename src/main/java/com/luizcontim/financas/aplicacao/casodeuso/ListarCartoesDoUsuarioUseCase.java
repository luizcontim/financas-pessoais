package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.CriarCartaoOutput;
import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;
import com.luizcontim.financas.dominio.repositorio.CartaoRepositorio;

import java.util.List;
import java.util.UUID;

public class ListarCartoesDoUsuarioUseCase {

	private final CartaoRepositorio cartaoRepositorio;

	public ListarCartoesDoUsuarioUseCase(CartaoRepositorio cartaoRepositorio) {
		this.cartaoRepositorio = cartaoRepositorio;
	}

	public List<CriarCartaoOutput> executar(UUID usuarioId) {
		return cartaoRepositorio.listarPorUsuario(usuarioId).stream()
				.map(this::paraOutput)
				.toList();
	}

	private CriarCartaoOutput paraOutput(CartaoDeCredito cartao) {
		return new CriarCartaoOutput(cartao.id(), cartao.nome(), cartao.bandeira(), cartao.limite().valor(),
				cartao.diaFechamento(), cartao.diaVencimento());
	}
}
