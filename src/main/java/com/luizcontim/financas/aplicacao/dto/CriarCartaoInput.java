package com.luizcontim.financas.aplicacao.dto;

import com.luizcontim.financas.dominio.modelo.Bandeira;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarCartaoInput(String nome, Bandeira bandeira, BigDecimal limite, int diaFechamento, int diaVencimento,
		UUID usuarioId) {
}
