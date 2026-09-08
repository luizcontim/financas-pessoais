package com.luizcontim.financas.infraestrutura.persistencia;

import com.luizcontim.financas.dominio.modelo.Email;
import com.luizcontim.financas.dominio.modelo.Usuario;
import com.luizcontim.financas.dominio.repositorio.UsuarioRepositorio;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.UsuarioJpaEntity;
import com.luizcontim.financas.infraestrutura.persistencia.mapeador.UsuarioMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioRepositorioJpa implements UsuarioRepositorio {

	private final UsuarioSpringDataRepository springDataRepository;

	public UsuarioRepositorioJpa(UsuarioSpringDataRepository springDataRepository) {
		this.springDataRepository = springDataRepository;
	}

	@Override
	public Usuario salvar(Usuario usuario) {
		UsuarioJpaEntity entidade = springDataRepository.save(UsuarioMapper.paraEntidade(usuario));
		return UsuarioMapper.paraDominio(entidade);
	}

	@Override
	public Optional<Usuario> buscarPorId(UUID id) {
		return springDataRepository.findById(id).map(UsuarioMapper::paraDominio);
	}

	@Override
	public Optional<Usuario> buscarPorEmail(Email email) {
		return springDataRepository.findByEmail(email.endereco()).map(UsuarioMapper::paraDominio);
	}
}
