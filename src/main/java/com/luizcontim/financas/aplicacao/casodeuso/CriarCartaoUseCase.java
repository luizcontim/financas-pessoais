package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.CriarCartaoInput;
import com.luizcontim.financas.aplicacao.dto.CriarCartaoOutput;
import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;
import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;
import com.luizcontim.financas.dominio.modelo.Dinheiro;
import com.luizcontim.financas.dominio.repositorio.CartaoRepositorio;
import com.luizcontim.financas.dominio.repositorio.UsuarioRepositorio;

public class CriarCartaoUseCase {

	private final CartaoRepositorio cartaoRepositorio;
	private final UsuarioRepositorio usuarioRepositorio;

	public CriarCartaoUseCase(CartaoRepositorio cartaoRepositorio, UsuarioRepositorio usuarioRepositorio) {
		this.cartaoRepositorio = cartaoRepositorio;
		this.usuarioRepositorio = usuarioRepositorio;
	}

	public CriarCartaoOutput executar(CriarCartaoInput input) {
		usuarioRepositorio.buscarPorId(input.usuarioId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + input.usuarioId()));

		CartaoDeCredito cartao = CartaoDeCredito.emitir(input.nome(), input.bandeira(), Dinheiro.de(input.limite()),
				input.diaFechamento(), input.diaVencimento(), input.usuarioId());
		cartaoRepositorio.salvar(cartao);
		return new CriarCartaoOutput(cartao.id(), cartao.nome(), cartao.bandeira(), cartao.limite().valor(),
				cartao.diaFechamento(), cartao.diaVencimento());
	}
}
