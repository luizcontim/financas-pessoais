package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.CriarCartaoInput;
import com.luizcontim.financas.aplicacao.dto.CriarCartaoOutput;
import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;
import com.luizcontim.financas.dominio.modelo.Dinheiro;
import com.luizcontim.financas.dominio.repositorio.CartaoRepositorio;

public class CriarCartaoUseCase {

	private final CartaoRepositorio cartaoRepositorio;

	public CriarCartaoUseCase(CartaoRepositorio cartaoRepositorio) {
		this.cartaoRepositorio = cartaoRepositorio;
	}

	public CriarCartaoOutput executar(CriarCartaoInput input) {
		CartaoDeCredito cartao = CartaoDeCredito.emitir(input.nome(), input.bandeira(), Dinheiro.de(input.limite()),
				input.diaFechamento(), input.diaVencimento());
		cartaoRepositorio.salvar(cartao);
		return new CriarCartaoOutput(cartao.id(), cartao.nome(), cartao.bandeira(), cartao.limite().valor(),
				cartao.diaFechamento(), cartao.diaVencimento());
	}
}
