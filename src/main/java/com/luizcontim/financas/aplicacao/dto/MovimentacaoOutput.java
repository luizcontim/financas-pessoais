package com.luizcontim.financas.aplicacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MovimentacaoOutput(UUID id, String tipo, String descricao, BigDecimal valor, String categoria, LocalDate data) {
}
