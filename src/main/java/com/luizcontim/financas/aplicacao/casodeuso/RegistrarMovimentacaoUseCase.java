package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.RegistrarMovimentacaoInput;
import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;
import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.modelo.Dinheiro;
import com.luizcontim.financas.dominio.modelo.TipoMovimentacao;
import com.luizcontim.financas.dominio.repositorio.ContaRepositorio;

public class RegistrarMovimentacaoUseCase {

	private final ContaRepositorio contaRepositorio;

	public RegistrarMovimentacaoUseCase(ContaRepositorio contaRepositorio) {
		this.contaRepositorio = contaRepositorio;
	}

	public void executar(RegistrarMovimentacaoInput input) {
		Conta conta = contaRepositorio.buscarPorId(input.contaId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada: " + input.contaId()));

		Dinheiro valor = Dinheiro.de(input.valor());
		if (input.tipo() == TipoMovimentacao.ENTRADA) {
			conta.registrarEntrada(input.descricao(), valor, input.categoria(), input.data());
		} else {
			conta.registrarSaida(input.descricao(), valor, input.categoria(), input.data());
		}

		contaRepositorio.salvar(conta);
	}
}
