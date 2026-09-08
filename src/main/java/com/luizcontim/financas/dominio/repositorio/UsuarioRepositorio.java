package com.luizcontim.financas.dominio.repositorio;

import com.luizcontim.financas.dominio.modelo.Email;
import com.luizcontim.financas.dominio.modelo.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositorio {

	Usuario salvar(Usuario usuario);

	Optional<Usuario> buscarPorId(UUID id);

	Optional<Usuario> buscarPorEmail(Email email);
}
