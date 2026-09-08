package com.luizcontim.financas.infraestrutura.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarUsuarioRequest(
		@NotBlank String nome,
		@NotBlank String email,
		@NotBlank @Size(min = 8) String senha) {
}
