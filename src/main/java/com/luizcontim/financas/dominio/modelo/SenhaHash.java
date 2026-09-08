package com.luizcontim.financas.dominio.modelo;

import java.util.Objects;

public record SenhaHash(String valor) {

	public SenhaHash {
		Objects.requireNonNull(valor, "valor não pode ser nulo");
		if (valor.isBlank()) {
			throw new IllegalArgumentException("Hash de senha não pode ser vazio");
		}
	}
}
