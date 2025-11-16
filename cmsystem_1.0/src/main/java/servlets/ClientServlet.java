package servlets;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import interfaces.ActionHandler;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Client;
import registry.ClientActionRegistry;
import services.ClientService;
import utils.Const;
import utils.HandleForward;

@WebServlet("/clients")
public class ClientServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private ClientService service = new ClientService();
	final Logger logger = LoggerFactory.getLogger(ClientServlet.class);

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());

		String action = request.getParameter("action");

		ActionHandler handler = ClientActionRegistry.get(action);

		if (handler == null) {
			logger.error("A ação da requisição não está registrada em ClientActionRegistry");
			HandleForward.errorForward(request, response, "Ação não registrada.", "erro.jsp");
			return;
		}

		handler.execute(request, response, logger);

	}

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String clientName = (String) request.getParameter("clientName");
		String clientCPF = (String) request.getParameter("clientCPF");
		String phoneNumber = (String) request.getParameter("phoneNumber");
		String clientEmail = (String) request.getParameter("clientEmail");
		String clientAdress = (String) request.getParameter("clientAdress");

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
				logger.error("Falha ao salvar o cliente: " + e.getMessage());
				request.setAttribute("msg", "Erro ao tentar salvar o cliente.");
				RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/register.jsp");
				rd.forward(request, response);
			}
		}

	}

}
