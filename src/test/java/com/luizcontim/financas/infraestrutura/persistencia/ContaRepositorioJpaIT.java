package com.luizcontim.financas.infraestrutura.persistencia;

import com.luizcontim.financas.TestcontainersConfiguration;
import com.luizcontim.financas.dominio.modelo.Categoria;
import com.luizcontim.financas.dominio.modelo.Conta;
import com.luizcontim.financas.dominio.modelo.Dinheiro;
import com.luizcontim.financas.dominio.modelo.Email;
import com.luizcontim.financas.dominio.modelo.SenhaHash;
import com.luizcontim.financas.dominio.modelo.Usuario;
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

	@Autowired
	private UsuarioRepositorioJpa usuarioRepositorioJpa;

	@Test
	void devePersistirERecuperarContaComMovimentacoes() {
		Usuario usuario = Usuario.registrar("Usuário Teste", new Email("teste@example.com"), new SenhaHash("hash"));
		usuarioRepositorioJpa.salvar(usuario);

		Conta conta = Conta.abrir("Conta Teste", usuario.id());
		conta.registrarEntrada("Salário", Dinheiro.de("3000"), Categoria.OUTROS, LocalDate.now());
		conta.registrarSaida("Mercado", Dinheiro.de("200"), Categoria.ALIMENTACAO, LocalDate.now());

		contaRepositorioJpa.salvar(conta);

		Optional<Conta> recuperada = contaRepositorioJpa.buscarPorId(conta.id());

		assertThat(recuperada).isPresent();
		assertThat(recuperada.get().saldo().valor()).isEqualByComparingTo("2800.00");
		assertThat(recuperada.get().movimentacoes()).hasSize(2);
	}
}
