package com.luizcontim.financas.dominio.modelo;

import java.util.Objects;
import java.util.UUID;

public final class Usuario {

	private final UUID id;
	private final String nome;
	private final Email email;
	private final SenhaHash senhaHash;

	private Usuario(UUID id, String nome, Email email, SenhaHash senhaHash) {
		this.id = Objects.requireNonNull(id);
		this.nome = Objects.requireNonNull(nome);
		this.email = Objects.requireNonNull(email);
		this.senhaHash = Objects.requireNonNull(senhaHash);
	}

	public static Usuario registrar(String nome, Email email, SenhaHash senhaHash) {
		return new Usuario(UUID.randomUUID(), nome, email, senhaHash);
	}

	public static Usuario reconstruir(UUID id, String nome, Email email, SenhaHash senhaHash) {
		return new Usuario(id, nome, email, senhaHash);
	}

	public UUID id() {
		return id;
	}

	public String nome() {
		return nome;
	}

	public Email email() {
		return email;
	}

	public SenhaHash senhaHash() {
		return senhaHash;
	}
}
