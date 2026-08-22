package com.luizcontim.financas.infraestrutura.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarContaRequest(@NotBlank String nome) {
}
