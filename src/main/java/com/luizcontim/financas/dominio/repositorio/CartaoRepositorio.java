package com.luizcontim.financas.dominio.repositorio;

import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;

import java.util.Optional;
import java.util.UUID;

public interface CartaoRepositorio {

	CartaoDeCredito salvar(CartaoDeCredito cartao);

	Optional<CartaoDeCredito> buscarPorId(UUID id);
}
