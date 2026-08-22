package com.luizcontim.financas.aplicacao.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record FaturaOutput(UUID cartaoId, int mesReferencia, int anoReferencia, String status, BigDecimal valorTotal,
		List<ParcelaOutput> parcelas) {
}
