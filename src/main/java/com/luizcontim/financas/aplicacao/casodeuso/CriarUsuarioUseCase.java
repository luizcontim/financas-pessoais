package com.luizcontim.financas.aplicacao.casodeuso;

import com.luizcontim.financas.aplicacao.dto.CriarUsuarioInput;
import com.luizcontim.financas.aplicacao.dto.CriarUsuarioOutput;
import com.luizcontim.financas.dominio.excecao.EmailJaCadastradoException;
import com.luizcontim.financas.dominio.modelo.Email;
import com.luizcontim.financas.dominio.modelo.SenhaHash;
import com.luizcontim.financas.dominio.modelo.Usuario;
import com.luizcontim.financas.dominio.repositorio.UsuarioRepositorio;
import com.luizcontim.financas.dominio.servico.CodificadorDeSenha;

public class CriarUsuarioUseCase {

	private final UsuarioRepositorio usuarioRepositorio;
	private final CodificadorDeSenha codificadorDeSenha;

	public CriarUsuarioUseCase(UsuarioRepositorio usuarioRepositorio, CodificadorDeSenha codificadorDeSenha) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.codificadorDeSenha = codificadorDeSenha;
	}

	public CriarUsuarioOutput executar(CriarUsuarioInput input) {
		Email email = new Email(input.email());
		if (usuarioRepositorio.buscarPorEmail(email).isPresent()) {
			throw new EmailJaCadastradoException("Já existe um usuário cadastrado com o email: " + input.email());
		}

		SenhaHash senhaHash = codificadorDeSenha.codificar(input.senha());
		Usuario usuario = Usuario.registrar(input.nome(), email, senhaHash);
		usuarioRepositorio.salvar(usuario);

		return new CriarUsuarioOutput(usuario.id(), usuario.nome(), usuario.email().endereco());
	}
}
