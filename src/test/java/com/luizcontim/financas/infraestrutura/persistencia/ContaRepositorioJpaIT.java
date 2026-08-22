package com.luizcontim.financas.infraestrutura.persistencia;

import com.luizcontim.financas.TestcontainersConfiguration;
import com.luizcontim.financas.dominio.modelo.Categoria;
import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.modelo.Dinheiro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ContaRepositorioJpaIT {

	@Autowired
	private ContaRepositorioJpa contaRepositorioJpa;

	@Test
	void devePersistirERecuperarContaComMovimentacoes() {
		Conta conta = Conta.abrir("Conta Teste");
		conta.registrarEntrada("Salário", Dinheiro.de("3000"), Categoria.OUTROS, LocalDate.now());
		conta.registrarSaida("Mercado", Dinheiro.de("200"), Categoria.ALIMENTACAO, LocalDate.now());

		contaRepositorioJpa.salvar(conta);

		Optional<Conta> recuperada = contaRepositorioJpa.buscarPorId(conta.id());

		assertThat(recuperada).isPresent();
		assertThat(recuperada.get().saldo().valor()).isEqualByComparingTo("2800.00");
		assertThat(recuperada.get().movimentacoes()).hasSize(2);
	}
}
