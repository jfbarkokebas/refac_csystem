package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Client;
import services.ClientService;
import services.TransactionsService;
import utils.Const;
import utils.HandleTimeData;

@WebServlet("/transactions")
public class TransactionsServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private TransactionsService service = new TransactionsService();
	private ClientService clientService = new ClientService();
	final Logger logger = LoggerFactory.getLogger(TransactionsServlet.class);

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = (String) request.getParameter("action");
		
		if ("stock".equalsIgnoreCase(action)) { //AJAX EVENTS

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Map<String, Object> data = null;

			try {
//				data = service.getTotalStockMap();
				logger.info("Ajax para estoque bem sucedida.");

			} catch (Exception e) {
				logger.error("Erro na busca com AJAX. {}", e);
			}

			if (data == null) {
				data = new HashMap<>();
				logger.info("os dados da requisição ajax vieram nulos");
			}

			data.put("stockUpdateDate",  HandleTimeData.getActualFormattedDateTime());

			String json = new Gson().toJson(data);
			response.getWriter().write(json);

		} else { // FORM EVENTS

			HashMap<String, Object> transactionMap = null;
			try {
				if ("searchByName".equalsIgnoreCase(action)) {

					String search = (String) request.getParameter("search");
					transactionMap = service.getTableDataByName(search);

				} else if ("searchProduct".equalsIgnoreCase(action)) {
					
					String search = (String) request.getParameter("search");
					transactionMap = service.getTableDataByProduct(search);
					
				} else {
					transactionMap = service.getTableData();
				}
				
				List<Client> clients = clientService.getAllClientsWithLimit(200);
				request.getSession().setAttribute("clients", clients);

				transactionMap = this.extractCoffeeAndPepperAndForward(request, response, transactionMap);

			} catch (Exception e) {
				logger.error("não foi possível buscar dados para a transactionMap");
				request.setAttribute("errorMsg", "Erro ao carregar os dados");
				RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/erro.jsp");
				rd.forward(request, response);
			}

		}

	}

	private HashMap<String, Object> extractCoffeeAndPepperAndForward(HttpServletRequest request, HttpServletResponse response,
			HashMap<String, Object> resultMap) throws ServletException, IOException {

		if (resultMap == null || resultMap.isEmpty()) {
			throw new IllegalArgumentException("A resultMap não pode ser null ou vazia");
		}

		try {

			Map<String, Object> totalStock = service.getTotalStockMap();
			resultMap.put("cafe", totalStock.get("cafe"));
			resultMap.put("pimenta", totalStock.get("pimenta"));

			resultMap.put("stockUpdateDate",  HandleTimeData.getActualFormattedDateTime());
			request.getSession().setAttribute("dataStock", resultMap);
			RequestDispatcher dispatcher = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/transactions.jsp");
			dispatcher.forward(request, response);

		} catch (Exception e) {
			logger.info("erro ao buscar totalStock");
			request.setAttribute("errorMsg", "Erro ao carregar os dados");
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/erro.jsp");
			rd.forward(request, response);
		}

		return resultMap;

	}
	
}

