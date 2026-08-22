package com.luizcontim.financas.infraestrutura.web.dto;

import com.luizcontim.financas.dominio.modelo.Categoria;
import com.luizcontim.financas.dominio.modelo.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistrarMovimentacaoRequest(
		@NotNull TipoMovimentacao tipo,
		@NotBlank String descricao,
		@NotNull @Positive BigDecimal valor,
		@NotNull Categoria categoria,
		@NotNull LocalDate data) {
}
