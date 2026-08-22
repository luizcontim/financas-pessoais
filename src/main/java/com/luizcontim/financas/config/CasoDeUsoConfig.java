package com.luizcontim.financas.config;

import com.luizcontim.financas.aplicacao.casodeuso.ConsultarExtratoUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.ConsultarFaturaUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.CriarCartaoUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.CriarContaUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.FecharFaturaUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.RegistrarCompraUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.RegistrarMovimentacaoUseCase;
import com.luizcontim.financas.dominio.repositorio.CartaoRepositorio;
import com.luizcontim.financas.dominio.repositorio.ContaRepositorio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fronteira explícita entre o framework e a camada de aplicação: os casos de uso
 * não têm nenhuma anotação do Spring, então a fiação de dependências acontece aqui.
 */
@Configuration
public class CasoDeUsoConfig {

	@Bean
	CriarContaUseCase criarContaUseCase(ContaRepositorio contaRepositorio) {
		return new CriarContaUseCase(contaRepositorio);
	}

	@Bean
	RegistrarMovimentacaoUseCase registrarMovimentacaoUseCase(ContaRepositorio contaRepositorio) {
		return new RegistrarMovimentacaoUseCase(contaRepositorio);
	}

	@Bean
	ConsultarExtratoUseCase consultarExtratoUseCase(ContaRepositorio contaRepositorio) {
		return new ConsultarExtratoUseCase(contaRepositorio);
	}

	@Bean
	CriarCartaoUseCase criarCartaoUseCase(CartaoRepositorio cartaoRepositorio) {
		return new CriarCartaoUseCase(cartaoRepositorio);
	}

	@Bean
	RegistrarCompraUseCase registrarCompraUseCase(CartaoRepositorio cartaoRepositorio) {
		return new RegistrarCompraUseCase(cartaoRepositorio);
	}

	@Bean
	FecharFaturaUseCase fecharFaturaUseCase(CartaoRepositorio cartaoRepositorio) {
		return new FecharFaturaUseCase(cartaoRepositorio);
	}

	@Bean
	ConsultarFaturaUseCase consultarFaturaUseCase(CartaoRepositorio cartaoRepositorio) {
		return new ConsultarFaturaUseCase(cartaoRepositorio);
	}
}
