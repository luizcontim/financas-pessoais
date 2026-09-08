package com.luizcontim.financas.infraestrutura.persistencia.mapeador;

import com.luizcontim.financas.dominio.modelo.Email;
import com.luizcontim.financas.dominio.modelo.SenhaHash;
import com.luizcontim.financas.dominio.modelo.Usuario;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.UsuarioJpaEntity;

public final class UsuarioMapper {

	private UsuarioMapper() {
	}

	public static UsuarioJpaEntity paraEntidade(Usuario usuario) {
		UsuarioJpaEntity entidade = new UsuarioJpaEntity();
		entidade.setId(usuario.id());
		entidade.setNome(usuario.nome());
		entidade.setEmail(usuario.email().endereco());
		entidade.setSenhaHash(usuario.senhaHash().valor());
		return entidade;
	}

	public static Usuario paraDominio(UsuarioJpaEntity entidade) {
		return Usuario.reconstruir(entidade.getId(), entidade.getNome(), new Email(entidade.getEmail()),
				new SenhaHash(entidade.getSenhaHash()));
	}
}
