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
import model.Client;
import utils.UtilsDao;

public class ClientDao {

	final Logger logger = LoggerFactory.getLogger(ClientDao.class);

	public List<Client> saveClient(Client client) throws SQLException {

		if (client == null) {
			throw new IllegalArgumentException("Não é possível salvar um cliente = NULL");
		}

		String sql = "INSERT INTO cm_system.clients(name, cpf, phone_number, email, adress) VALUES(?, ?, ?, ?, ?);";

		try (Connection connection = ConnectionFactory.conectar();
				PreparedStatement stmt = connection.prepareStatement(sql);) {

			stmt.setString(1, client.getName());
			stmt.setString(2, client.getCpf());
			stmt.setString(3, client.getPhoneNumber());
			stmt.setString(4, client.getEmail());
			stmt.setString(5, client.getAdress());
			stmt.execute();

		} catch (Exception e) {
			logger.error("saveClient() -> Erro ao salvar o cliente: {}", e.getMessage());
			throw new SQLException("Erro ao salvar o cliente: " + e.getMessage());
		}

		List<Client> persistedClients = this.getAllClientsWithLimit(300);

		return persistedClients;
	}

	
	public Client findClientByID(Long id) throws SQLException {

		if (id == null) {
			throw new IllegalArgumentException("O valor do ID não pode ser NULL");
		}

		String sql = "SELECT id, name, cpf, phone_number, email, adress"
				+ " FROM cm_system.clients WHERE id = ? AND active = true";

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Client client = new Client();

		try {
			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, id);

			rs = stmt.executeQuery();

			while (rs.next()) {
				client.setId(rs.getLong("id"));
				client.setName(rs.getString("name"));
				client.setCpf(rs.getString("cpf"));
				client.setEmail(rs.getString("email"));
				client.setPhoneNumber(rs.getString("phone_number"));
				client.setAdress(rs.getString("adress"));

			}

		} catch (Exception e) {
			logger.error("findClientByID() -> Erro ao tentar achar um usuário: {}", e.getMessage());
			throw new SQLException("Erro ao tentar achar um usuário: " + e.getMessage());

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return client;

	}

	public List<Client> getAllClientsWithLimit(Integer queryLimit) throws SQLException {
		
		if (queryLimit == null) {
			queryLimit = 300;
		}
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		List<Client> result = new ArrayList<Client>();
		
		try {
			
			String sql = "SELECT id, name, cpf, phone_number, email, adress"
					+ " FROM cm_system.clients WHERE active = true  ORDER BY name LIMIT ? ";
			
			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, queryLimit);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				Client client = new Client();
				client.setId(rs.getLong("id"));
				client.setName(rs.getString("name"));
				client.setCpf(rs.getString("cpf"));
				client.setEmail(rs.getString("email"));
				client.setPhoneNumber(rs.getString("phone_number"));
				client.setAdress(rs.getString("adress"));
				
				result.add(client);
				
			}
			
		} catch (SQLException e) {
			logger.error("getAllClientsWithLimit() -> Erro ao tentar achar um usuário: {}", e.getMessage());
			throw e;
			
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}
		
		return result;
		
	}
	public List<Client> getInactiveClientsWithLimit(Integer queryLimit) throws SQLException {

		if (queryLimit == null) {
			queryLimit = 300;
		}

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<Client> result = new ArrayList<Client>();

		try {

			String sql = "SELECT id, name, cpf, phone_number, email, adress"
					+ " FROM cm_system.commodity_system.clients WHERE active = false  ORDER BY name LIMIT ? ";

			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, queryLimit);

			rs = stmt.executeQuery();

			while (rs.next()) {
				Client client = new Client();
				client.setId(rs.getLong("id"));
				client.setName(rs.getString("name"));
				client.setCpf(rs.getString("cpf"));
				client.setEmail(rs.getString("email"));
				client.setPhoneNumber(rs.getString("phone_number"));
				client.setAdress(rs.getString("adress"));

				result.add(client);

			}

		} catch (SQLException e) {
			logger.error("getAllClientsWithLimit() -> Erro ao tentar achar um usuário: {}", e.getMessage());
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return result;

	}

	@SuppressWarnings("unused")
	public boolean isClient(String cpf) throws SQLException {

		if (cpf == null || cpf.trim().isEmpty()) {
			throw new IllegalArgumentException("O cpf passado não pode ser nulo ou vazio");
		}

		String sql = "SELECT 1 FROM cm_system.commodity_system.clients WHERE cpf = ?";

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);

			stmt.setString(1, cpf);
			rs = stmt.executeQuery();
			return rs.next();

		} catch (SQLException e) {
			logger.error("isClient() -> Erro ao verificar a existência do cliente no DB: {}", e.getMessage());
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

	}

	public List<Client> findClientByLetters(String searchName) throws Exception {

		if (searchName == null) {
			throw new IllegalArgumentException("O nome passado não pode ser nulo");
		}

		List<Client> result = new ArrayList<>();

		String sql = "SELECT name, cpf, phone_number, email, adress"
				+ " FROM cm_system.commodity_system.clients WHERE active = true AND UPPER(name) like UPPER(?)";

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%" + searchName + "%");

			rs = stmt.executeQuery();

			while (rs.next()) {
				Client client = new Client();
				client.setName(rs.getString("name"));
				client.setCpf(rs.getString("cpf"));
				client.setEmail(rs.getString("email"));
				client.setPhoneNumber(rs.getString("phone_number"));
				client.setAdress(rs.getString("adress"));

				result.add(client);
			}

		} catch (Exception e) {
			logger.error("findClientByLetters() -> Erro ao buscar cliente por nome: {}", e.getMessage());
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return result;
	}

	public Boolean setClientActiveStatus(Long id, Boolean disable) throws SQLException {

		Boolean inactivated = false;

		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = ConnectionFactory.conectar();

			String sql = "UPDATE cm_system.commodity_system.clients SET active = ? WHERE id = ?";
			stmt = connection.prepareStatement(sql);
			stmt.setBoolean(1, disable);
			stmt.setLong(2, id);

			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				inactivated = true;
			}

		} catch (SQLException e) {
			logger.error("setClientAsInactive() -> Erro ao inativar o cliente: {}", e.getMessage());
			throw e;

		} finally {
			UtilsDao.closeDAOResources(null, stmt, connection, logger);
		}

		return inactivated;
	}

	public boolean updateClient(Client client) throws SQLException {

		if (client == null || client.getId() == null) {
			throw new IllegalArgumentException("Cliente ou ID do cliente não pode ser nulo para atualização.");
		}

		String sql = "UPDATE cm_system.clients SET name = ?, cpf = ?, phone_number = ?, email = ?, adress = ? WHERE id = ?;";

		try (Connection connection = ConnectionFactory.conectar();
			 PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setString(1, client.getName());
			stmt.setString(2, client.getCpf());
			stmt.setString(3, client.getPhoneNumber());
			stmt.setString(4, client.getEmail());
			stmt.setString(5, client.getAdress());
			stmt.setLong(6, client.getId());

			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;

		} catch (Exception e) {
			logger.error("updateClient() -> Erro ao atualizar o cliente: {}", e.getMessage());
			throw new SQLException("Erro ao atualizar o cliente: " + e.getMessage());
		}
	}


}
