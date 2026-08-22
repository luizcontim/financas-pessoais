package com.luizcontim.financas.aplicacao.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarContaOutput(UUID id, String nome, BigDecimal saldo) {
}
