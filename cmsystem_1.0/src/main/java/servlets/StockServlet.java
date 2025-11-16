package servlets;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import interfaces.ActionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import registry.StockActionRegistry;
import utils.HandleForward;

@WebServlet("/stockinfo")
public class StockServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	final Logger logger = LoggerFactory.getLogger(StockServlet.class);

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		ActionHandler handler = StockActionRegistry.get(action);

		if (handler == null) {
			logger.error("A ação da requisição não está registrada em StockActionRegistry");
			HandleForward.errorForward(request, response, "Ação não registrada.", "erro.jsp");
		}

		handler.execute(request, response, logger);

	}

}
