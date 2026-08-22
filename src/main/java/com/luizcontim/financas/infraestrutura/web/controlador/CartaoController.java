package com.luizcontim.financas.infraestrutura.web.controlador;

import com.luizcontim.financas.aplicacao.casodeuso.ConsultarFaturaUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.CriarCartaoUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.FecharFaturaUseCase;
import com.luizcontim.financas.aplicacao.casodeuso.RegistrarCompraUseCase;
import com.luizcontim.financas.aplicacao.dto.CriarCartaoInput;
import com.luizcontim.financas.aplicacao.dto.CriarCartaoOutput;
import com.luizcontim.financas.aplicacao.dto.FaturaOutput;
import com.luizcontim.financas.aplicacao.dto.RegistrarCompraInput;
import com.luizcontim.financas.infraestrutura.web.dto.CriarCartaoRequest;
import com.luizcontim.financas.infraestrutura.web.dto.RegistrarCompraRequest;
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
@RequestMapping("/cartoes")
public class CartaoController {

	private final CriarCartaoUseCase criarCartaoUseCase;
	private final RegistrarCompraUseCase registrarCompraUseCase;
	private final FecharFaturaUseCase fecharFaturaUseCase;
	private final ConsultarFaturaUseCase consultarFaturaUseCase;

	public CartaoController(CriarCartaoUseCase criarCartaoUseCase, RegistrarCompraUseCase registrarCompraUseCase,
			FecharFaturaUseCase fecharFaturaUseCase, ConsultarFaturaUseCase consultarFaturaUseCase) {
		this.criarCartaoUseCase = criarCartaoUseCase;
		this.registrarCompraUseCase = registrarCompraUseCase;
		this.fecharFaturaUseCase = fecharFaturaUseCase;
		this.consultarFaturaUseCase = consultarFaturaUseCase;
	}

	@PostMapping
	public ResponseEntity<CriarCartaoOutput> criar(@Valid @RequestBody CriarCartaoRequest request) {
		CriarCartaoOutput output = criarCartaoUseCase.executar(new CriarCartaoInput(request.nome(), request.bandeira(),
				request.limite(), request.diaFechamento(), request.diaVencimento()));
		return ResponseEntity.created(URI.create("/cartoes/" + output.id())).body(output);
	}

	@PostMapping("/{id}/compras")
	public ResponseEntity<Void> registrarCompra(@PathVariable UUID id, @Valid @RequestBody RegistrarCompraRequest request) {
		registrarCompraUseCase.executar(new RegistrarCompraInput(id, request.descricao(), request.valorTotal(),
				request.categoria(), request.dataCompra(), request.quantidadeParcelas()));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/{id}/faturas/{ano}/{mes}/fechar")
	public ResponseEntity<Void> fecharFatura(@PathVariable UUID id, @PathVariable int ano, @PathVariable int mes) {
		fecharFaturaUseCase.executar(id, mes, ano);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/faturas/{ano}/{mes}")
	public ResponseEntity<FaturaOutput> fatura(@PathVariable UUID id, @PathVariable int ano, @PathVariable int mes) {
		return ResponseEntity.ok(consultarFaturaUseCase.executar(id, mes, ano));
	}
}
