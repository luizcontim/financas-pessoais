package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.CriarContaOutput;
import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.repositorio.ContaRepositorio;

import java.util.List;
import java.util.UUID;

public class ListarContasDoUsuarioUseCase {

	private final ContaRepositorio contaRepositorio;

	public ListarContasDoUsuarioUseCase(ContaRepositorio contaRepositorio) {
		this.contaRepositorio = contaRepositorio;
	}

	public List<CriarContaOutput> executar(UUID usuarioId) {
		return contaRepositorio.listarPorUsuario(usuarioId).stream()
				.map(this::paraOutput)
				.toList();
	}

	private CriarContaOutput paraOutput(Conta conta) {
		return new CriarContaOutput(conta.id(), conta.nome(), conta.saldo().valor());
	}
}
