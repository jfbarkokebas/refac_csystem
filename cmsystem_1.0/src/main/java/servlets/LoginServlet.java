package servlets;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import services.UserService;
import utils.SenhaHash;

@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("login");
		String senhaStr = request.getParameter("senha");

		String senha = SenhaHash.encriptografarSenha(senhaStr);

		UserService service = new UserService();

		if (login == null || login.isEmpty() || senha == null || senha.isEmpty()) {
			request.setAttribute("msg", "Precisa preencher todos os campos.");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);

		} else {

			try {

				User usuario = (User) service.acharUsuario(login, senha);

				if (usuario.getLogin() == null) {
					request.setAttribute("msg", "Usuário ou senha inválidos.");
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
					requestDispatcher.forward(request, response);

				} else {

					request.getSession().setAttribute("logedUser", usuario.getNomeUsuario());

					if (!response.isCommitted()) {

						logger.info("Usuário {} logado com sucesso.", login);
						response.sendRedirect("transactions");
					}
				}

			} catch (Exception e) {
				logger.error("Erro: {} tentou logar sem sucesso! Msg: {}", login, e);
			}
		}

	}

}
