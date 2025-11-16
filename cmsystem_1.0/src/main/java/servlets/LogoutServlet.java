package servlets;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/logout")
public class LogoutServlet extends BaseServlet{
	
	final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);
	
	
    @Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			String usuarioLogado = (String) session.getAttribute("logedUser");
			logger.info("Usuário {} fez logoff do sistema", usuarioLogado);
			
			request.getSession().invalidate();
		}

		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}


	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processGet(request, response);
	}
    
    

}
