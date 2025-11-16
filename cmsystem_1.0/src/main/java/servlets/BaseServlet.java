package servlets;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {
	
final Logger logger = LoggerFactory.getLogger(BaseServlet.class);

	@Override
	protected final void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean isSessionValid = this.validateUserSession(request, response);
		if (isSessionValid) {
			this.processGet(request, response);
		} else {
			logger.info("Sessão inválida: logout");
			response.sendRedirect("index.jsp");
		}
	}

	@Override
	protected final void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean isSessionValid = this.validateUserSession(request, response);
		if (isSessionValid) {
			this.processPost(request, response);
		} else {
			logger.info("Sessão inválida: logout");
			response.sendRedirect("index.jsp");
		}
	}

	protected void processPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	protected void processGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	private boolean validateUserSession(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession(false);

		boolean result = session != null && session.getAttribute("logedUser") != null;

		return result;
	}

}
