package utils;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandleForward {

	public static void forward(HttpServletRequest request, HttpServletResponse response, String message, String pagePath) 
			throws ServletException, IOException {
		request.setAttribute("msg", message);
		RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH +"/" + pagePath);
		rd.forward(request, response);
	}
	public static void errorForward(HttpServletRequest request, HttpServletResponse response, String message, String pagePath) 
			throws ServletException, IOException {
		request.setAttribute("errorMsg", message);
		RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH +"/" + pagePath);
		rd.forward(request, response);
	}
	
	public static void rulesForward(HttpServletRequest request, HttpServletResponse response, String title, String message) 
			throws ServletException, IOException {
		request.setAttribute("ruleTitle", title);
		request.setAttribute("ruleMsg", message);
		RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH +"/rules.jsp");
		rd.forward(request, response);
	}
}
