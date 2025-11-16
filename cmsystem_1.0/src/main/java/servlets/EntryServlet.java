package servlets;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Entry;
import model.Stock;
import services.EntryService;
import utils.Const;
import utils.HandleForward;
import utils.HandleStrings;

@WebServlet("/stockentries")
public class EntryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private EntryService stockEntryService = new EntryService();
	final Logger logger = LoggerFactory.getLogger(EntryServlet.class);

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		String pageJsp = Const.ROOT_PAGE_PATH + "/receipt.jsp";
		String entryId = request.getParameter("entryId");

		if ("delete".equalsIgnoreCase(action)) {

			if (entryId == null || entryId.isEmpty()) {
				logger.error("O id da compra veio nulo ou vazio na requisição");
				HandleForward.errorForward(request, response, "Erro ao buscar dados para a exclusãp da compra", "erro.jsp");
			}
			
			Map<String, Object> resultMap;

			try {
				
				Long stockEntryId = Long.parseLong(entryId);
				
				resultMap = this.stockEntryService.deleteStockEntry(stockEntryId);
				boolean isDeleted = (boolean) resultMap.get("isDeleted");

				if (!isDeleted) {
					String msg = (String) resultMap.get("msg");
					HandleForward.forward(request, response, msg, "transactions.jsp");
				}
				
				response.sendRedirect("transactions");

			} catch (Exception e) {
				logger.error("Erro ao deletar a entrada do estoque ", e);
				HandleForward.errorForward(request, response, "Erro ao excluir a entrada de estoque. " + e.getMessage(),
						"erro.jsp");
			}

		} else {

			try {

				Long id = Long.parseLong(entryId);
				Map<String, Object> details = this.stockEntryService.getStockEntryDetails(id);
				request.setAttribute("details", details);
				RequestDispatcher dispatcher = request.getRequestDispatcher(pageJsp);
				dispatcher.forward(request, response);

			} catch (Exception e) {
				logger.info("Os dados não foram encontrados.");
				HandleForward.errorForward(request, response, "Os dados não foram encontrados.", "erro.jsp");
			}

		}

	}

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String clienteID = request.getParameter("clientStock");
		String produtoID = request.getParameter("productStock");
		String quantidade = request.getParameter("qtdStock");
		String data = request.getParameter("dateStock");
		String observacao = request.getParameter("observationStock");

		if (HandleStrings.isInvalid(clienteID, produtoID, quantidade, data)) {
			HandleForward.forward(request, response, "Por favor, preencha todos os campos do formulário.",
					"transactions.jsp");
			return;

		}

		Entry stockEntry = new Entry();
		stockEntry.setClientId(clienteID);
		stockEntry.setProductId(produtoID);
		stockEntry.setQuantity(quantidade);
		stockEntry.setEntryDate(data);
		stockEntry.setObservation(observacao);

		try {

			Stock stock = new Stock();
			stock.setClientId(stockEntry.getClientId());
			stock.setProductId(stockEntry.getProduct_id());
			stock.setQuantity(stockEntry.getQuantity());

			this.stockEntryService.saveEntry(stockEntry, stock);

			HandleForward.forward(request, response, "Produto estocado com sucesso!", "transactions.jsp");
			return;

//		} catch (SQLIntegrityConstraintViolationException e) {
//			logger.info("Entrada duplicada, estoque não salvo", produtoID, e);
//			HandleForward.forward(request, response, "Entrada duplicada", "transactions.jsp");
//			
		}catch (Exception e1) {
			logger.error("Erro ao salvar o produto {} no estoque", produtoID, e1);
			HandleForward.errorForward(request, response, "Erro ao dar entrada no estoque", "erro.jsp");
		}

	}

}
