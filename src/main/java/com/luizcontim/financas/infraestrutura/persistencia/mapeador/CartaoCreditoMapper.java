package com.luizcontim.financas.infraestrutura.persistencia.mapeador;

import com.luizcontim.financas.dominio.modelo.Bandeira;
import com.luizcontim.financas.dominio.modelo.CartaoDeCredito;
import com.luizcontim.financas.dominio.modelo.Categoria;
import com.luizcontim.financas.dominio.modelo.Compra;
import com.luizcontim.financas.dominio.modelo.Dinheiro;
import com.luizcontim.financas.dominio.modelo.Fatura;
import com.luizcontim.financas.dominio.modelo.Parcela;
import com.luizcontim.financas.dominio.modelo.StatusFatura;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.CartaoCreditoJpaEntity;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.CompraJpaEntity;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.FaturaJpaEntity;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.ParcelaJpaEntity;

import java.util.List;

public final class CartaoCreditoMapper {

	private CartaoCreditoMapper() {
	}

	public static CartaoCreditoJpaEntity paraEntidade(CartaoDeCredito cartao) {
		CartaoCreditoJpaEntity entidade = new CartaoCreditoJpaEntity();
		entidade.setId(cartao.id());
		entidade.setNome(cartao.nome());
		entidade.setBandeira(cartao.bandeira().name());
		entidade.setLimite(cartao.limite().valor());
		entidade.setDiaFechamento(cartao.diaFechamento());
		entidade.setDiaVencimento(cartao.diaVencimento());

		entidade.getCompras().addAll(cartao.compras().stream().map(c -> paraEntidade(c, entidade)).toList());
		entidade.getFaturas().addAll(cartao.faturas().stream().map(f -> paraEntidade(f, entidade)).toList());

		return entidade;
	}

	private static CompraJpaEntity paraEntidade(Compra compra, CartaoCreditoJpaEntity cartao) {
		CompraJpaEntity entidade = new CompraJpaEntity();
		entidade.setId(compra.id());
		entidade.setCartao(cartao);
		entidade.setDescricao(compra.descricao());
		entidade.setValorTotal(compra.valorTotal().valor());
		entidade.setCategoria(compra.categoria().name());
		entidade.setData(compra.data());
		entidade.setQuantidadeParcelas(compra.quantidadeParcelas());
		return entidade;
	}

	private static FaturaJpaEntity paraEntidade(Fatura fatura, CartaoCreditoJpaEntity cartao) {
		FaturaJpaEntity entidade = new FaturaJpaEntity();
		entidade.setId(fatura.id());
		entidade.setCartao(cartao);
		entidade.setMesReferencia(fatura.mesReferencia());
		entidade.setAnoReferencia(fatura.anoReferencia());
		entidade.setStatus(fatura.status().name());
		entidade.getParcelas().addAll(fatura.parcelas().stream().map(p -> paraEntidade(p, entidade)).toList());
		return entidade;
	}

	private static ParcelaJpaEntity paraEntidade(Parcela parcela, FaturaJpaEntity fatura) {
		ParcelaJpaEntity entidade = new ParcelaJpaEntity();
		entidade.setId(parcela.id());
		entidade.setFatura(fatura);
		entidade.setCompraId(parcela.compraId());
		entidade.setDescricao(parcela.descricao());
		entidade.setCategoria(parcela.categoria().name());
		entidade.setNumero(parcela.numero());
		entidade.setTotalParcelas(parcela.totalParcelas());
		entidade.setValor(parcela.valor().valor());
		return entidade;
	}

	public static CartaoDeCredito paraDominio(CartaoCreditoJpaEntity entidade) {
		List<Compra> compras = entidade.getCompras().stream().map(CartaoCreditoMapper::paraDominio).toList();
		List<Fatura> faturas = entidade.getFaturas().stream().map(CartaoCreditoMapper::paraDominio).toList();

		return CartaoDeCredito.reconstruir(entidade.getId(), entidade.getNome(), Bandeira.valueOf(entidade.getBandeira()),
				Dinheiro.de(entidade.getLimite()), entidade.getDiaFechamento(), entidade.getDiaVencimento(), compras, faturas);
	}

	private static Compra paraDominio(CompraJpaEntity entidade) {
		return new Compra(entidade.getId(), entidade.getDescricao(), Dinheiro.de(entidade.getValorTotal()),
				Categoria.valueOf(entidade.getCategoria()), entidade.getData(), entidade.getQuantidadeParcelas());
	}

	private static Fatura paraDominio(FaturaJpaEntity entidade) {
		List<Parcela> parcelas = entidade.getParcelas().stream().map(CartaoCreditoMapper::paraDominio).toList();
		return Fatura.reconstruir(entidade.getId(), entidade.getMesReferencia(), entidade.getAnoReferencia(),
				StatusFatura.valueOf(entidade.getStatus()), parcelas);
	}

	private static Parcela paraDominio(ParcelaJpaEntity entidade) {
		return new Parcela(entidade.getId(), entidade.getCompraId(), entidade.getDescricao(),
				Categoria.valueOf(entidade.getCategoria()), entidade.getNumero(), entidade.getTotalParcelas(),
				Dinheiro.de(entidade.getValor()));
	}
}
