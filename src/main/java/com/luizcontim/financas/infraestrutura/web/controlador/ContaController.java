package com.luizcontim.financas.infraestrutura.web.controlador;

import com.luizcontim.financas.aplicacao.casodeuso.ConsultarExtratoUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.CriarContaUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.RegistrarMovimentacaoUseCase;
import com.luizcontim.financas.aplicacao.dto.CriarContaInput;
import com.luizcontim.financas.aplicacao.dto.CriarContaOutput;
import com.luizcontim.financas.aplicacao.dto.ExtratoContaOutput;
import com.luizcontim.financas.aplicacao.dto.RegistrarMovimentacaoInput;
import com.luizcontim.financas.infraestrutura.web.dto.CriarContaRequest;
import com.luizcontim.financas.infraestrutura.web.dto.RegistrarMovimentacaoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/contas")
public class ContaController {

	private final CriarContaUseCase criarContaUseCase;
	private final RegistrarMovimentacaoUseCase registrarMovimentacaoUseCase;
	private final ConsultarExtratoUseCase consultarExtratoUseCase;

	public ContaController(CriarContaUseCase criarContaUseCase, RegistrarMovimentacaoUseCase registrarMovimentacaoUseCase,
			ConsultarExtratoUseCase consultarExtratoUseCase) {
		this.criarContaUseCase = criarContaUseCase;
		this.registrarMovimentacaoUseCase = registrarMovimentacaoUseCase;
		this.consultarExtratoUseCase = consultarExtratoUseCase;
	}

	@PostMapping
	public ResponseEntity<CriarContaOutput> criar(@Valid @RequestBody CriarContaRequest request) {
		CriarContaOutput output = criarContaUseCase.executar(new CriarContaInput(request.nome()));
		return ResponseEntity.created(URI.create("/contas/" + output.id())).body(output);
	}

	@PostMapping("/{id}/movimentacoes")
	public ResponseEntity<Void> registrarMovimentacao(@PathVariable UUID id, @Valid @RequestBody RegistrarMovimentacaoRequest request) {
		registrarMovimentacaoUseCase.executar(new RegistrarMovimentacaoInput(id, request.tipo(), request.descricao(),
				request.valor(), request.categoria(), request.data()));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{id}/extrato")
	public ResponseEntity<ExtratoContaOutput> extrato(@PathVariable UUID id) {
		return ResponseEntity.ok(consultarExtratoUseCase.executar(id));
	}
}
