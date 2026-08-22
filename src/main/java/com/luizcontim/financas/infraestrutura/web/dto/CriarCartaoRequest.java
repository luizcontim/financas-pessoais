package com.luizcontim.financas.infraestrutura.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CriarCartaoRequest(
		@NotBlank String nome,
		String bandeira,
		@NotNull @Positive BigDecimal limite,
		@Min(1) @Max(28) int diaFechamento,
		@Min(1) @Max(28) int diaVencimento) {
}
