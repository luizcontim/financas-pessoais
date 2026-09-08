package com.luizcontim.financas.aplicacao.dto;

import java.util.UUID;

public record CriarUsuarioOutput(UUID id, String nome, String email) {
}
