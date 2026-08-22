package com.luizcontim.financas.infraestrutura.persistencia;

import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;
import com.luizcontim.financas.dominio.repositorio.CartaoRepositorio;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.CartaoCreditoJpaEntity;
import com.luizcontim.financas.infraestrutura.persistencia.mapeador.CartaoCreditoMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CartaoRepositorioJpa implements CartaoRepositorio {

	private final CartaoCreditoSpringDataRepository springDataRepository;

	public CartaoRepositorioJpa(CartaoCreditoSpringDataRepository springDataRepository) {
		this.springDataRepository = springDataRepository;
	}

	@Override
	public CartaoDeCredito salvar(CartaoDeCredito cartao) {
		CartaoCreditoJpaEntity entidade = springDataRepository.save(CartaoCreditoMapper.paraEntidade(cartao));
		return CartaoCreditoMapper.paraDominio(entidade);
	}

	@Override
	public Optional<CartaoDeCredito> buscarPorId(UUID id) {
		return springDataRepository.findById(id).map(CartaoCreditoMapper::paraDominio);
	}
}
