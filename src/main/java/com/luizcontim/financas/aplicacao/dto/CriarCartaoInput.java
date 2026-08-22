package com.luizcontim.financas.aplicacao.dto;

import java.math.BigDecimal;

public record CriarCartaoInput(String nome, String bandeira, BigDecimal limite, int diaFechamento, int diaVencimento) {
}
