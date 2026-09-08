package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.CriarContaInput;
import com.luizcontim.financas.aplicacao.dto.CriarContaOutput;
import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;
import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.repositorio.ContaRepositorio;
import com.luizcontim.financas.dominio.repositorio.UsuarioRepositorio;

public class CriarContaUseCase {

	private final ContaRepositorio contaRepositorio;
	private final UsuarioRepositorio usuarioRepositorio;

	public CriarContaUseCase(ContaRepositorio contaRepositorio, UsuarioRepositorio usuarioRepositorio) {
		this.contaRepositorio = contaRepositorio;
		this.usuarioRepositorio = usuarioRepositorio;
	}

	public CriarContaOutput executar(CriarContaInput input) {
		usuarioRepositorio.buscarPorId(input.usuarioId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + input.usuarioId()));

		Conta conta = Conta.abrir(input.nome(), input.usuarioId());
		contaRepositorio.salvar(conta);
		return new CriarContaOutput(conta.id(), conta.nome(), conta.saldo().valor());
	}
}
