package com.luizcontim.financas.aplicacao.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarCartaoOutput(UUID id, String nome, String bandeira, BigDecimal limite, int diaFechamento, int diaVencimento) {
}
