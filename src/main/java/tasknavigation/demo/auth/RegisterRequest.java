package tasknavigation.demo.auth;

import tasknavigation.demo.domain.enums.NivelAcesso;

public class RegisterRequest {
	
	private String nome ;
	private String email;
	private String password;
	private NivelAcesso nivelAcesso;

	public RegisterRequest() {

	}
	public RegisterRequest(String nome, String email, String password, NivelAcesso nivelAcesso) {
		this.nome = nome;
		this.email = email;
		this.password = password;
		this.nivelAcesso = nivelAcesso;

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public NivelAcesso getNivelAcesso() {
		return nivelAcesso;
	}

	public void setNivelAcesso(NivelAcesso nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}
}
