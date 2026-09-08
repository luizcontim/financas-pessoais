package com.luizcontim.financas.dominio.modelo;

import com.luizcontim.financas.dominio.excecao.RegraDeNegocioException;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String endereco) {

	private static final Pattern FORMATO_VALIDO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

	public Email {
		Objects.requireNonNull(endereco, "endereco não pode ser nulo");
		if (!FORMATO_VALIDO.matcher(endereco).matches()) {
			throw new RegraDeNegocioException("Email inválido: " + endereco);
		}
	}
}
