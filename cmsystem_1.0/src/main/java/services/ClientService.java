package services;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import daos.ClientDao;
import model.Client;

public class ClientService {
	
	private ClientDao dao;
	
	public ClientService() {
		this.dao = new ClientDao();
	}

	public List<Client> saveClient(Client client) throws SQLException {
		
		List<Client> savedClients = dao.saveClient(client);
		
		
		return savedClients;
	}

	public List<Client> getAllClientsWithLimit(Integer limit) throws SQLException {
		if(limit == null) {
			limit = 200;
		}
		
		List<Client> result = dao.getAllClientsWithLimit(limit);
		
		return result;
		
	}

	public List<Client> findClientByName(String searchName) throws Exception {
		
		if(searchName == null) {
			throw new IllegalArgumentException("A busca não pode ter valor NULL.");
		}
		
		if("".equalsIgnoreCase(searchName)) {
			return this.getAllClientsWithLimit(200);
		}else {
			return this.dao.findClientByLetters(searchName);
		}
		
	}

	public Map<String, Object> setClientActiveStatus(String clientID, Boolean active) throws NumberFormatException, SQLException {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(clientID == null) {
			throw new IllegalArgumentException("O id do cliente não pode ser nullo para efetuar o delete.");
		}
		
		Long id = Long.parseLong(clientID);
		
		Boolean excluded = this.dao.setClientActiveStatus(id, active);
		result.put("excluded", excluded);
		
		List<Client> clientList = this.dao.getAllClientsWithLimit(300);
		result.put("clientList", clientList);
		
		return result;
	}

	public Client findClientByID(String clientID) throws NumberFormatException, SQLException {
		
		if(clientID == null) {
			throw new IllegalArgumentException("Para editar o cliente id não pode ser null");
		}
		
		return this.dao.findClientByID(Long.parseLong(clientID));
	}

	public Map<String, Object> updateClient(Client client) throws SQLException {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Boolean updated = dao.updateClient(client);
		result.put("updated", updated);
		
		List<Client> clientList = this.dao.getAllClientsWithLimit(300);
		result.put("clientList", clientList);
		
		return result;
	}

	public List<Client> getInactiveClientsWithLimit(Integer limit) throws SQLException {
		
		if(limit == null) {
			limit = 200;
		}
		
		List<Client> result = dao.getInactiveClientsWithLimit(limit);
		
		return result;
	}

}
