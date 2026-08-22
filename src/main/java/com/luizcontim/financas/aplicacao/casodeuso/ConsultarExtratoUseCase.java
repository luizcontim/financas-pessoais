package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.ExtratoContaOutput;
import com.luizcontim.financas.aplicacao.dto.MovimentacaoOutput;
import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;
import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.repositorio.ContaRepositorio;

import java.util.List;
import java.util.UUID;

public class ConsultarExtratoUseCase {

	private final ContaRepositorio contaRepositorio;

	public ConsultarExtratoUseCase(ContaRepositorio contaRepositorio) {
		this.contaRepositorio = contaRepositorio;
	}

	public ExtratoContaOutput executar(UUID contaId) {
		Conta conta = contaRepositorio.buscarPorId(contaId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Conta não encontrada: " + contaId));

		List<MovimentacaoOutput> movimentacoes = conta.movimentacoes().stream()
				.map(m -> new MovimentacaoOutput(m.id(), m.tipo().name(), m.descricao(), m.valor().valor(), m.categoria().name(), m.data()))
				.toList();

		return new ExtratoContaOutput(conta.id(), conta.nome(), conta.saldo().valor(), movimentacoes);
	}
}
