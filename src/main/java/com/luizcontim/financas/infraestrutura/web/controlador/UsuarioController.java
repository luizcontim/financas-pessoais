package com.luizcontim.financas.infraestrutura.web.controlador;

import com.luizcontim.financas.aplicacao.casodeuso.CriarUsuarioUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.ListarCartoesDoUsuarioUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.ListarContasDoUsuarioUseCase;
import com.luizcontim.financas.aplicacao.dto.CriarCartaoOutput;
import com.luizcontim.financas.aplicacao.dto.CriarContaOutput;
import com.luizcontim.financas.aplicacao.dto.CriarUsuarioInput;
import com.luizcontim.financas.aplicacao.dto.CriarUsuarioOutput;
import com.luizcontim.financas.infraestrutura.web.dto.CriarUsuarioRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final CriarUsuarioUseCase criarUsuarioUseCase;
	private final ListarContasDoUsuarioUseCase listarContasDoUsuarioUseCase;
	private final ListarCartoesDoUsuarioUseCase listarCartoesDoUsuarioUseCase;

	public UsuarioController(CriarUsuarioUseCase criarUsuarioUseCase, ListarContasDoUsuarioUseCase listarContasDoUsuarioUseCase,
			ListarCartoesDoUsuarioUseCase listarCartoesDoUsuarioUseCase) {
		this.criarUsuarioUseCase = criarUsuarioUseCase;
		this.listarContasDoUsuarioUseCase = listarContasDoUsuarioUseCase;
		this.listarCartoesDoUsuarioUseCase = listarCartoesDoUsuarioUseCase;
	}

	@PostMapping
	public ResponseEntity<CriarUsuarioOutput> criar(@Valid @RequestBody CriarUsuarioRequest request) {
		CriarUsuarioOutput output = criarUsuarioUseCase.executar(new CriarUsuarioInput(request.nome(), request.email(),
				request.senha()));
		return ResponseEntity.created(URI.create("/usuarios/" + output.id())).body(output);
	}

	@GetMapping("/{id}/contas")
	public ResponseEntity<List<CriarContaOutput>> contas(@PathVariable UUID id) {
		return ResponseEntity.ok(listarContasDoUsuarioUseCase.executar(id));
	}

	@GetMapping("/{id}/cartoes")
	public ResponseEntity<List<CriarCartaoOutput>> cartoes(@PathVariable UUID id) {
		return ResponseEntity.ok(listarCartoesDoUsuarioUseCase.executar(id));
	}
}
