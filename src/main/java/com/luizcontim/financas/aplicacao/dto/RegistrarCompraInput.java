package com.luizcontim.financas.aplicacao.dto;

import com.luizcontim.financas.dominio.modelo.Categoria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RegistrarCompraInput(
		UUID cartaoId,
		String descricao,
		BigDecimal valorTotal,
		Categoria categoria,
		LocalDate dataCompra,
		int quantidadeParcelas) {
}
