package model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nomeUsuario;
	private String login;
	private String senha;
	
	public User() {
		super();
	}

	public User(Long id, String nomeUsuario, String login, String senha) {
		this.id = id;
		this.nomeUsuario = nomeUsuario;
		this.login = login;
		this.senha = senha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return nomeUsuario + "; Id = " + id + " ;  Login = " + login + ".";
	}
	
	

}
