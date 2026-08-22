package com.luizcontim.financas.infraestrutura.web.controlador;

import com.luizcontim.financas.dominio.excecao.FaturaFechadaException;
import com.luizcontim.financas.dominio.excecao.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	ResponseEntity<Map<String, Object>> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
		return corpo(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(FaturaFechadaException.class)
	ResponseEntity<Map<String, Object>> tratarFaturaFechada(FaturaFechadaException ex) {
		return corpo(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<Map<String, Object>> tratarArgumentoInvalido(IllegalArgumentException ex) {
		return corpo(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	private ResponseEntity<Map<String, Object>> corpo(HttpStatus status, String mensagem) {
		return ResponseEntity.status(status).body(Map.of(
				"timestamp", Instant.now().toString(),
				"status", status.value(),
				"mensagem", mensagem));
	}
}
