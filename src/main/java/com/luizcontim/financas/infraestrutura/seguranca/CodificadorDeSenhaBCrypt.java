package com.luizcontim.financas.infraestrutura.seguranca;

import com.luizcontim.financas.dominio.modelo.SenhaHash;
import com.luizcontim.financas.dominio.servico.CodificadorDeSenha;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CodificadorDeSenhaBCrypt implements CodificadorDeSenha {

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public SenhaHash codificar(String senhaEmTextoPuro) {
		return new SenhaHash(encoder.encode(senhaEmTextoPuro));
	}
}
