package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import connection.ConnectionFactory;
import model.User;
import utils.SenhaHash;

public class UsuarioDAO {

	final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

	public void saveUser(User usuario) throws SQLException {

		if (usuario != null) {

				String sql = "INSERT INTO usuarios (nome_usuario, login, senha)"
						+ "	VALUES (?, ?, ?) ";

				try (Connection connection = ConnectionFactory.conectar();
						PreparedStatement stmt = connection.prepareStatement(sql);) {

					stmt.setString(1, usuario.getNomeUsuario());
					stmt.setString(2, usuario.getLogin());
					stmt.setString(3, usuario.getSenha());
					stmt.execute();

				} catch (Exception e) {
					logger.error("saveUser() -> Erro ao salvar um usuário: {}", e.getMessage());
					throw e;
				}
		
		} else {
			throw new IllegalArgumentException("Não é possível salvar usuário NULL.");
		}

	}


	public boolean verificarUsuario(String login, String senha) throws SQLException {

		if ((login != null && senha != null) || (!"".equalsIgnoreCase(login) && !"".equalsIgnoreCase(login))) {

			String sql = "SELECT login, senha FROM cm_system.usuarios WHERE login = ? AND senha = ?";

			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;

			try {
				connection = ConnectionFactory.conectar();
				stmt = connection.prepareStatement(sql);

				stmt.setString(1, login);
				stmt.setString(2, senha);

				rs = stmt.executeQuery();

				if (rs.next()) {
					return true;
				}

			} catch (Exception e) {
				logger.error("verificarUsuario() -> Erro ao verificar um usuário: {}", e.getMessage());
				throw e;

			} finally {
				closeUsuarioDAOResources(rs, stmt, connection);
			}

			return false;

		} else {
			throw new IllegalArgumentException("Login e (ou) senha com valores inválidos");
		}
	}

	public User acharUsuario(String login, String senha) throws SQLException {

		if ((login != null && senha != null) || (!"".equalsIgnoreCase(login) && !"".equalsIgnoreCase(login))) {

			String sql = "SELECT id, nome_usuario, login, senha"
					+ " FROM cm_system.usuarios WHERE login = ? AND senha = ?";

			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			User usuario = new User();

			try {
				connection = ConnectionFactory.conectar();
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, login);
				stmt.setString(2, senha);

				rs = stmt.executeQuery();

				while (rs.next()) {
					usuario.setId(rs.getLong("id"));
					usuario.setNomeUsuario(rs.getString("nome_usuario"));
					usuario.setLogin(rs.getString("login"));
					usuario.setSenha(rs.getString("senha"));

				}

			} catch (Exception e) {
				logger.error("acharUsuario() -> Erro ao tentar achar um usuário: {}", e.getMessage());
				throw e;

			} finally {
				closeUsuarioDAOResources(rs, stmt, connection);
			}

			return usuario;

		} else {
			throw new IllegalArgumentException(
					"Não foi possível achar o usuário, login e (ou) senha com valores inválidos");
		}
	}

	public User resetarSenha(Long id) throws Exception {

		String defaultSenha = SenhaHash.encriptografarSenha("Abcd1234!");

		User response = null;

		String sql = "UPDATE cm_system.usuarios SET senha = ? WHERE id = ?";

		try (Connection connection = ConnectionFactory.conectar();
				PreparedStatement stmt = connection.prepareStatement(sql);) {

			stmt.setString(1, defaultSenha);
			stmt.setLong(2, id);
			stmt.execute();

			response = this.buscarUsuarioPorID(id);
			logger.info("Usuario id: {} trocou a senha com sucesso!", id);

		} catch (Exception e) {
			logger.error("resetarSenha() -> Erro ao trocar a senha: {}", e.getMessage());
			throw e;
		}

		return response;
	}

	public User trocarSenha(Long id, String novaSenha) throws Exception {

		User response = null;

		String sql = "UPDATE cm_system.usuarios SET senha = ? WHERE id = ?";

		try (Connection connection = ConnectionFactory.conectar();
				PreparedStatement stmt = connection.prepareStatement(sql);) {

			stmt.setString(1, novaSenha);
			stmt.setLong(2, id);
			stmt.execute();

			response = this.buscarUsuarioPorID(id);
			logger.info("Usuario id: {} trocou a senha com sucesso!", id);

		} catch (Exception e) {
			logger.error("trocarSenha() -> Erro ao trocar a senha: {}", e.getMessage());
			throw e;
		}

		return response;
	}

	public User buscarUsuarioPorID(Long id) throws Exception {
		if (id != null) {

			String sql = "SELECT id, nome_usuario, login, senha, is_membro, is_admin " 
			+ "FROM cm_system.usuarios WHERE id = ?";

			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			User response = null;

			try {
				connection = ConnectionFactory.conectar();
				stmt = connection.prepareStatement(sql);

				stmt.setLong(1, id);
				rs = stmt.executeQuery();

				if (rs.next()) {
					response = new User();
					response.setId(rs.getLong("id"));
					response.setNomeUsuario(rs.getString("nome_usuario"));
					response.setLogin(rs.getString("login"));
					return response;
				}

			} catch (Exception e) {
				logger.error("buscarUsuarioPorID() -> Erro ao buscar o usuário por id. ERRO: {}", e.getMessage());
				throw e;
			} finally {
				this.closeUsuarioDAOResources(rs, stmt, connection);
			}

			return response;

		} else {
			throw new IllegalArgumentException("O id passado não pode ser nulo");
		}

	}

	public boolean isUser(Long id) throws SQLException {

		if (id != null) {

			String sql = "SELECT 1 FROM cm_system.usuarios WHERE id = ?";

			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;

			try {
				connection = ConnectionFactory.conectar();
				stmt = connection.prepareStatement(sql);

				stmt.setLong(1, id);
				rs = stmt.executeQuery();
				return rs.next();

			} catch (Exception e) {
				logger.error("isUser() -> Erro ao verificar a existência de um usuário: {}", e.getMessage());
				throw e;
			} finally {
				this.closeUsuarioDAOResources(rs, stmt, connection);
			}

		} else {
			throw new IllegalArgumentException("O id passado não pode ser nulo");
		}
	}

	public List<User> obterTodosUsuarios() throws SQLException {

		List<User> result = new ArrayList<>();
		String sql = "SELECT id, nome_usuario, login, senha, is_admin, is_membro, foto_usuario, extensao_foto"
				+ " FROM cm_system.usuarios WHERE id > 2 ORDER BY nome_usuario ASC LIMIT 50";

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next()) {
				User usuario = new User();
				usuario.setId(rs.getLong("id"));
				usuario.setNomeUsuario(rs.getString("nome_usuario"));
				usuario.setLogin(rs.getString("login"));

				result.add(usuario);
			}

		} catch (Exception e) {
			logger.error("obterTodosUsuarios() -> Erro ao buscar todos os usuários: {}", e.getMessage());
			throw e;

		} finally {

			closeUsuarioDAOResources(rs, stmt, connection);
		}

		return result;
	}

	public User consutarPorLogin(String login) throws Exception {

		if (login != null) {

			User usuario = new User();
			String sql = "SELECT id, nome_usuario, login, senha, is_admin, is_membro, foto_usuario, extensao_foto"
					+ " FROM cm_system.usuarios WHERE UPPER(login) = UPPER(?) ";

			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;

			try {
				connection = ConnectionFactory.conectar();
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, login);

				rs = stmt.executeQuery();

				while (rs.next()) {
					usuario.setId(rs.getLong("id"));
					usuario.setNomeUsuario(rs.getString("nome_usuario"));
					usuario.setLogin(rs.getString("login"));

				}

			} catch (Exception e) {
				logger.error("consutarPorLogin() -> Erro ao efetuar uma consulta por login: {}", e.getMessage());
				throw e;
				
			} finally {
				this.closeUsuarioDAOResources(rs, stmt, connection);
			}

			return usuario;

		} else {
			throw new IllegalArgumentException("O login passado não pode ser nulo");
		}
	}

	public boolean existsByLogin(String login) throws SQLException {
		String sql = "SELECT COUNT(*) FROM cm_system.usuarios WHERE login = ?";

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);

			stmt.setString(1, login);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			logger.error("existsByLogin() -> Erro ao efetuar uma consulta por login: {}", e.getMessage());
			throw e;
		} finally {
			closeUsuarioDAOResources(rs, stmt, connection);
		}
		return false;
	}

	public List<User> consutarPorNome(String nome) throws SQLException {

		if (nome != null) {

			List<User> result = new ArrayList<User>();

			if ("".equalsIgnoreCase(nome)) {
				try {
					return this.obterTodosUsuarios();

				} catch (SQLException e) {
					logger.error("existsByLogin() -> Erro ao efetuar uma consulta por nome: {}", e.getMessage());
					throw e;
				}
			}

			String sql = "SELECT id, nome_usuario, login, senha, is_admin, is_membro, foto_usuario, extensao_foto"
					+ " FROM cm_system.usuarios WHERE UPPER(nome_usuario) like UPPER(?) AND id > 2";

			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;

			try {
				connection = ConnectionFactory.conectar();
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, "%" + nome + "%");

				rs = stmt.executeQuery();

				while (rs.next()) {
					User usuario = new User();
					usuario.setId(rs.getLong("id"));
					usuario.setNomeUsuario(rs.getString("nome_usuario"));
					usuario.setLogin(rs.getString("login"));

					result.add(usuario);
				}

			} catch (Exception e) {
				logger.error("Erro ao buscar usuário por nome: {}", e.getMessage());
				throw e;
			} finally {
				this.closeUsuarioDAOResources(rs, stmt, connection);
			}

			return result;
		} else {
			throw new IllegalArgumentException("O nome passado não pode ser nulo");
		}
	}

	public List<User> deleteUser(Long id) throws Exception {

		if (id != null) {

			List<User> result = new ArrayList<User>();
			String sql = "DELETE FROM cm_system.usuarios WHERE id = ? ";

			try (Connection connection = ConnectionFactory.conectar();
					PreparedStatement stmt = connection.prepareStatement(sql);) {
				stmt.setLong(1, id);
				stmt.executeUpdate();

				try {
					result = this.obterTodosUsuarios();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				logger.error("Erro ao remover o usuário de id = {}: {}", id, e.getMessage());
				throw e;
			}

			return result;

		} else {
			throw new IllegalArgumentException("Para efetuar um delete o id passado não pode ser nulo");
		}
	}

	public boolean podeCadastrarMembro() throws SQLException {
		String sql = "SELECT COUNT(*) FROM cm_system.usuarios WHERE is_membro = true";

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);

			rs = stmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);
				return count < 4;
			}

		} catch (Exception e) {
			logger.error("Erro ao verificar se um membro pode ser cadastrado: {}", e.getMessage());
			throw e;

		} finally {
			this.closeUsuarioDAOResources(rs, stmt, connection);
		}

		return false;
	}

	private void closeUsuarioDAOResources(ResultSet rs, PreparedStatement stmt, Connection connection) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Erro ao fechar o resultSet da classe UsuarioDAO: {}", e.getMessage());
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("Erro ao fechar o preparedStatment da classe UsuarioDAO: {}", e.getMessage());
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Erro ao fechar a connection da classe UsuarioDAO: {}", e.getMessage());
			}
		}
	}

}
