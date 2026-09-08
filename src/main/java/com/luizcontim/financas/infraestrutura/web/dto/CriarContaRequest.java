package com.luizcontim.financas.infraestrutura.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarContaRequest(@NotBlank String nome, @NotNull UUID usuarioId) {
}
