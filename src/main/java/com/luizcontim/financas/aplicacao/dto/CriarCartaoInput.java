package com.luizcontim.financas.aplicacao.dto;

import com.luizcontim.financas.dominio.modelo.Bandeira;

import java.math.BigDecimal;

public record CriarCartaoInput(String nome, Bandeira bandeira, BigDecimal limite, int diaFechamento, int diaVencimento) {
}
