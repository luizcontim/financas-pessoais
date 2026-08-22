package com.luizcontim.financas.dominio.repositorio;

import com.luizcontim.financas.dominio.modelo.Conta;

import java.util.Optional;
import java.util.UUID;

public interface ContaRepositorio {

	Conta salvar(Conta conta);

	Optional<Conta> buscarPorId(UUID id);
}
