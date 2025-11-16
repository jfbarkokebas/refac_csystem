package services;

import java.sql.SQLException;
import java.util.List;

import daos.UsuarioDAO;
import model.User;
import utils.SenhaHash;

public class UserService {

	private UsuarioDAO dao;

	public UserService() {
		this.dao = new UsuarioDAO();
	}

	public void salvarUsuario(User usuario) throws SQLException {

		if (!dao.existsByLogin(null)) {
			this.dao.saveUser(usuario);
			
		}else {
			throw new IllegalArgumentException("O login informado já existe na base de dados");
		}
	}
	
	public void resetarSenha(Long id) throws Exception {
		
		if(id == null) {
			throw new IllegalArgumentException("O id passado para reset de senha não pode ser null");
		}
		
		if(!this.isUser(id)) {
			throw new IllegalArgumentException("Não foi encontrado usuário com o esse ID");
		}
		
		dao.resetarSenha(id);
	}
	
	public User trocarSenha(String inputPassword, String confirmPassword, Long id) throws Exception {
		
		if(inputPassword == null && confirmPassword == null && id == null) {
			throw new IllegalArgumentException("Os parametros passados não pode ser nulos");
		}
		
		if(!inputPassword.equals(confirmPassword)) {
			throw new IllegalArgumentException("As senhas precisam ser iguais");
		}
		
		String novaSenha = SenhaHash.encriptografarSenha(inputPassword);
		
		return dao.trocarSenha(id, novaSenha);
	}

	public boolean usuarioExiste(User usuario) throws SQLException {
		String login = usuario.getLogin();
		boolean result = dao.existsByLogin(login);
		
		return result;
	}

	public boolean existeUsuario(String login, String senha) throws SQLException {
		boolean result = this.dao.verificarUsuario(login, senha);
		return result;
	}

	public User acharUsuario(String login, String senha) throws SQLException {
		User result = this.dao.acharUsuario(login, senha);
		return result;
	}

	public List<User> acharTodosUsuarios() throws SQLException {
		List<User> result = this.dao.obterTodosUsuarios();
		return result;
	}

	public User consultarPorLogin(String login) throws Exception {
		User result = this.dao.consutarPorLogin(login);
		return result;
	}

	public List<User> consultarPorNome(String nome) throws SQLException {
		List<User> result = this.dao.consutarPorNome(nome);
		return result;
	}

	public List<User> deleteUser(Long id) throws Exception {
		List<User> result = this.dao.deleteUser(id);
		return result;
	}

	public boolean isUser(Long id) throws SQLException {
		boolean result = this.dao.isUser(id);
		return result;
	}
}
