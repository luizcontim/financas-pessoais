package com.luizcontim.financas.dominio.servico;

import com.luizcontim.financas.dominio.modelo.SenhaHash;

public interface CodificadorDeSenha {

	SenhaHash codificar(String senhaEmTextoPuro);
}
