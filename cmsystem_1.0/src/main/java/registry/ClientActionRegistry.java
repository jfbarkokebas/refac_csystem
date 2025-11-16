package registry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.gson.Gson;

import interfaces.ActionHandler;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Client;
import services.ClientService;
import utils.Const;

public class ClientActionRegistry {

	private static final Map<String, ActionHandler> registry = new HashMap<>();

	static {

		registry.put("reportPage", new GetAllClients());
		registry.put("include", new SetClientAsActive());
		registry.put("getClientList", new GetClientList());
		registry.put("updateClient", new UpdateClient());
		registry.put("getClient", new FindClientById());
		registry.put("exclude", new SetClientAsInactive());
		registry.put("login", new GetClientListToRegisterPage());
		registry.put("selectClients", new GetClientListToTransctionsPage());
		registry.put("search", new SearchClientsByName());
		registry.put("select", new InitSelect());

	}

	public static ActionHandler get(String action) {
		return registry.get(action);
	}

}

class GetAllClients implements ActionHandler {
	private ClientService service = new ClientService();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		try {
			List<Client> clients = service.getAllClientsWithLimit(200);

			request.setAttribute("clients", clients);

			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/supplier-receipt.jsp");
			rd.forward(request, response);

		} catch (SQLException e) {
			logger.info("Erro ao buscar a lista de clientes");
			request.setAttribute("errorMsg", "Não foi possível carregar a lista de clientes");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/erro.jsp");
			rd.forward(request, response);
		}
		
	}
	
}

class SetClientAsActive implements ActionHandler {
	private ClientService service = new ClientService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String clientID = request.getParameter("clientId");
		String message = "";

		try {

			Map<String, Object> clientsMap = service.setClientActiveStatus(clientID, true);
			boolean included = (boolean) clientsMap.get("excluded");

			if (included)
				message = "Cliente ativado com sucesso";
			else
				message = "Erro ao ativar o cliente";

			request.setAttribute("msg", message);
			request.setAttribute("clients", clientsMap.get("clientList"));

			List<Client> inactiveClients = service.getInactiveClientsWithLimit(200);
			request.setAttribute("inactiveClients", inactiveClients);

			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
			rd.forward(request, response);

		} catch (Exception e) {
			logger.info("Erro ao excluir o cliente! ", e);
			request.setAttribute("errorMsg", "Não foi possível desativar o cliente!");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/erro.jsp");
			rd.forward(request, response);
		}
		
	}
	
}

class GetClientList implements ActionHandler {
	private ClientService service = new ClientService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		try {
			List<Client> clients = service.getAllClientsWithLimit(200);

			request.setAttribute("clients", clients);

			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/purchase-receipts.jsp");
			rd.forward(request, response);

		} catch (SQLException e) {
			logger.info("Erro ao buscar a lista de clientes");
			request.setAttribute("errorMsg", "Não foi possível carregar a lista de clientes");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/erro.jsp");
			rd.forward(request, response);
		}
		
	}
	
}

class UpdateClient implements ActionHandler {
	private ClientService service = new ClientService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String clientID = (String) request.getParameter("clientID");
		String clientName = (String) request.getParameter("new-clientName");
		String clientCPF = (String) request.getParameter("new-clientCPF");
		String phoneNumber = (String) request.getParameter("new-phoneNumber");
		String clientEmail = (String) request.getParameter("new-clientEmail");
		String clientAdress = (String) request.getParameter("new-clientAdress");
		String message = "";

		if (clientID == null || clientName == null || phoneNumber == null || clientEmail == null
				|| clientAdress == null) {
			request.setAttribute("msg", "Os parâmetros obrigatórios precisam ser preenchidos");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
			rd.forward(request, response);
			return;

		}

		try {
			Client client = new Client(clientName, clientCPF, phoneNumber, clientEmail, clientAdress);
			client.setId(Long.parseLong(clientID));

			Map<String, Object> clientsMap = this.service.updateClient(client);

			if ((boolean) clientsMap.get("updated")) {
				message = "Cliente atualizado";
			} else {
				message = "Erro ao atualizar o cliente";
			}

			request.setAttribute("msg", message);
			request.setAttribute("clients", clientsMap.get("clientList"));
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
			rd.forward(request, response);

		} catch (SQLException e) {
			logger.info("Erro ao buscar todos os clientes após update");
			request.setAttribute("msg", "Não foi possível carregar a lista de clientes");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/transactions.jsp");
			rd.forward(request, response);
		}

		
	}
	
}

class FindClientById implements ActionHandler {
	private ClientService service = new ClientService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String clientID = request.getParameter("clientId");

		try {

			Client client = this.service.findClientByID(clientID);

			request.setAttribute("client", client);
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/client-update.jsp");
			rd.forward(request, response);

		} catch (Exception e) {
			logger.error("Erro ao buscar o cliente! ", e);
			request.setAttribute("errorMsg", "Não foi possível buscar o cliente!");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/erro.jsp");
			rd.forward(request, response);
		}
		
	}
	
}

class SetClientAsInactive implements ActionHandler {
	private ClientService service = new ClientService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String clientID = request.getParameter("clientId");
		String message = "";

		try {

			Map<String, Object> clientsMap = service.setClientActiveStatus(clientID, false);
			boolean excluded = (boolean) clientsMap.get("excluded");

			if (excluded) {
				message = "Cliente desativado com sucesso";
			} else {
				message = "Erro ao excluir o cliente";
			}

			request.setAttribute("msg", message);
			request.setAttribute("clients", clientsMap.get("clientList"));

			List<Client> inactiveClients = service.getInactiveClientsWithLimit(200);
			request.setAttribute("inactiveClients", inactiveClients);

			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
			rd.forward(request, response);

		} catch (Exception e) {
			logger.error("Erro ao excluir o cliente! ", e);
			request.setAttribute("errorMsg", "Não foi possível desativar o cliente!");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/erro.jsp");
			rd.forward(request, response);
		}
		
	}
	
}

class GetClientListToRegisterPage implements ActionHandler {
	private ClientService service = new ClientService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		try {
			List<Client> clients = service.getAllClientsWithLimit(200);
			request.setAttribute("clients", clients);

			List<Client> inactiveClients = service.getInactiveClientsWithLimit(200);
			request.setAttribute("inactiveClients", inactiveClients);

			if (clients.isEmpty()) {
				this.createDefaultUser(request, response);
				
			}else {
				RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
				rd.forward(request, response);
			}


		} catch (SQLException e) {
			logger.error("Erro ao buscar todos os clientes após login");
			request.setAttribute("msg", "Não foi possível carregar a lista de clientes");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
			rd.forward(request, response);
		}
		
	}
	
	private void createDefaultUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String clientName = "Usuario Teste";
		String clientCPF = "00000000000";
		String phoneNumber = "00 0000-0000";
		String clientEmail = "teste@teste.com";
		String clientAdress = "Brasil";

		if (clientName == null || phoneNumber == null || clientEmail == null || clientAdress == null) {
			request.setAttribute("msg", "Os parâmetros obrigatórios precisam ser preenchidos");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
			rd.forward(request, response);

		} else {

			List<Client> result;

			try {
				Client requestClient = new Client(clientName, clientCPF, phoneNumber, clientEmail, clientAdress);
				result = service.saveClient(requestClient);

				request.setAttribute("clients", result);
				RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
				rd.forward(request, response);

			} catch (Exception e) {
				request.setAttribute("msg", "Erro ao tentar salvar o cliente.");
				RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
				rd.forward(request, response);
			}
		}
	}
	
}

class GetClientListToTransctionsPage implements ActionHandler {
	private ClientService service = new ClientService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		try {
			List<Client> clients = service.getAllClientsWithLimit(200);
			request.getSession().setAttribute("clients", clients);
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/transactions.jsp");
			rd.forward(request, response);

		} catch (SQLException e) {
			logger.error("Erro ao buscar todos os clientes após login");
			request.setAttribute("errorMsg", "Não foi possível carregar a lista de clientes");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/erro.jsp");
			rd.forward(request, response);
		}
		
	}
	
}

class SearchClientsByName implements ActionHandler {
	private ClientService service = new ClientService();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String searchName = (String) request.getParameter("searchName");

		try {
			List<Client> clients = service.findClientByName(searchName);
			request.getSession().setAttribute("clients", clients);
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
			rd.forward(request, response);
			return;

		} catch (Exception e) {
			logger.error("Erro ao buscar clientes por nome");
			request.setAttribute("msg", "Não foi possível carregar a lista buscada de clientes");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
			rd.forward(request, response);
		}
		
	}
	
}

class InitSelect implements ActionHandler{


    private ClientService service = new ClientService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
            throws ServletException, IOException {

    	 String query = request.getParameter("query");
         response.setContentType("application/json;charset=UTF-8");

         try {
             List<Client> clients = service.findClientByName(query);

             Gson gson = new Gson();
             String json = gson.toJson(clients);

             response.getWriter().write(json);
             response.getWriter().flush();   // garante envio completo
             response.getWriter().close();   // encerra resposta

         } catch (Exception e) {
             logger.error("Erro ao buscar clientes: {}", e.getMessage());
             response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             response.getWriter().write("{\"error\":\"Erro ao buscar clientes\"}");
         }
    }
	
}
