package com.luizcontim.financas.aplicacao.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ExtratoContaOutput(UUID contaId, String nome, BigDecimal saldo, List<MovimentacaoOutput> movimentacoes) {
}
