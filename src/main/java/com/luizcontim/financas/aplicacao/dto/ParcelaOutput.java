package com.luizcontim.financas.aplicacao.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ParcelaOutput(UUID id, UUID compraId, String descricao, String categoria, int numero, int totalParcelas, BigDecimal valor) {
}
