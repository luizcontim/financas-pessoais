package com.luizcontim.financas.infraestrutura.persistencia.mapeador;

import com.luizcontim.financas.dominio.modelo.Categoria;
import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.modelo.Dinheiro;
import com.luizcontim.financas.dominio.modelo.Movimentacao;
import com.luizcontim.financas.dominio.modelo.TipoMovimentacao;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.ContaJpaEntity;
import com.luizcontim.financas.infraestrutura.persistencia.entidade.MovimentacaoJpaEntity;

import java.util.List;

public final class ContaMapper {

	private ContaMapper() {
	}

	public static ContaJpaEntity paraEntidade(Conta conta) {
		ContaJpaEntity entidade = new ContaJpaEntity();
		entidade.setId(conta.id());
		entidade.setNome(conta.nome());
		entidade.setSaldo(conta.saldo().valor());
		entidade.setMoeda(conta.saldo().moeda());

		List<MovimentacaoJpaEntity> movimentacoes = conta.movimentacoes().stream()
				.map(m -> paraEntidade(m, entidade))
				.toList();
		entidade.getMovimentacoes().addAll(movimentacoes);

		return entidade;
	}

	private static MovimentacaoJpaEntity paraEntidade(Movimentacao movimentacao, ContaJpaEntity conta) {
		MovimentacaoJpaEntity entidade = new MovimentacaoJpaEntity();
		entidade.setId(movimentacao.id());
		entidade.setConta(conta);
		entidade.setTipo(movimentacao.tipo().name());
		entidade.setDescricao(movimentacao.descricao());
		entidade.setValor(movimentacao.valor().valor());
		entidade.setCategoria(movimentacao.categoria().name());
		entidade.setData(movimentacao.data());
		return entidade;
	}

	public static Conta paraDominio(ContaJpaEntity entidade) {
		List<Movimentacao> movimentacoes = entidade.getMovimentacoes().stream()
				.map(ContaMapper::paraDominio)
				.toList();

		return Conta.reconstruir(entidade.getId(), entidade.getNome(),
				new Dinheiro(entidade.getSaldo(), entidade.getMoeda()), movimentacoes);
	}

	private static Movimentacao paraDominio(MovimentacaoJpaEntity entidade) {
		return new Movimentacao(entidade.getId(), TipoMovimentacao.valueOf(entidade.getTipo()), entidade.getDescricao(),
				Dinheiro.de(entidade.getValor()), Categoria.valueOf(entidade.getCategoria()), entidade.getData());
	}
}
