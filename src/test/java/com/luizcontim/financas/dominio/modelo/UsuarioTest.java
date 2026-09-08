package com.luizcontim.financas.dominio.modelo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsuarioTest {

	@Test
	void deveRegistrarUsuarioComEmailValido() {
		Usuario usuario = Usuario.registrar("Ana Silva", new Email("ana@example.com"), new SenhaHash("hash-qualquer"));

		assertThat(usuario.id()).isNotNull();
		assertThat(usuario.nome()).isEqualTo("Ana Silva");
		assertThat(usuario.email().endereco()).isEqualTo("ana@example.com");
	}

	@Test
	void naoDevePermitirEmailComFormatoInvalido() {
		assertThatThrownBy(() -> new Email("nao-e-um-email"))
				.isInstanceOf(com.luizcontim.financas.dominio.excecao.RegraDeNegocioException.class);
	}
}
