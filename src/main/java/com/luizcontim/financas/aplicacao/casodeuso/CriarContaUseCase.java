package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.CriarContaInput;
import com.luizcontim.financas.aplicacao.dto.CriarContaOutput;
import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.repositorio.ContaRepositorio;

public class CriarContaUseCase {

	private final ContaRepositorio contaRepositorio;

	public CriarContaUseCase(ContaRepositorio contaRepositorio) {
		this.contaRepositorio = contaRepositorio;
	}

	public CriarContaOutput executar(CriarContaInput input) {
		Conta conta = Conta.abrir(input.nome());
		contaRepositorio.salvar(conta);
		return new CriarContaOutput(conta.id(), conta.nome(), conta.saldo().valor());
	}
}
