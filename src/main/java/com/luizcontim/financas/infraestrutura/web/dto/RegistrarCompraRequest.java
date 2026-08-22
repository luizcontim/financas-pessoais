package com.luizcontim.financas.infraestrutura.web.dto;

import com.luizcontim.financas.dominio.modelo.Categoria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistrarCompraRequest(
		@NotBlank String descricao,
		@NotNull @Positive BigDecimal valorTotal,
		@NotNull Categoria categoria,
		@NotNull LocalDate dataCompra,
		@Min(1) int quantidadeParcelas) {
}
