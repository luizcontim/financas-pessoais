package com.luizcontim.financas.aplicacao.dto;

import com.luizcontim.financas.dominio.modelo.Categoria;
import com.luizcontim.financas.dominio.modelo.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RegistrarMovimentacaoInput(
		UUID contaId,
		TipoMovimentacao tipo,
		String descricao,
		BigDecimal valor,
		Categoria categoria,
		LocalDate data) {
}
