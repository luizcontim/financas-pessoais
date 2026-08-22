package com.luizcontim.financas.infraestrutura.persistencia;

import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.repositorio.ContaRepositorio;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.ContaJpaEntity;
import com.luizcontim.financas.infraestrutura.persistencia.mapeador.ContaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ContaRepositorioJpa implements ContaRepositorio {

	private final ContaSpringDataRepository springDataRepository;

	public ContaRepositorioJpa(ContaSpringDataRepository springDataRepository) {
		this.springDataRepository = springDataRepository;
	}

	@Override
	public Conta salvar(Conta conta) {
		ContaJpaEntity entidade = springDataRepository.save(ContaMapper.paraEntidade(conta));
		return ContaMapper.paraDominio(entidade);
	}

	@Override
	public Optional<Conta> buscarPorId(UUID id) {
		return springDataRepository.findById(id).map(ContaMapper::paraDominio);
	}
}
