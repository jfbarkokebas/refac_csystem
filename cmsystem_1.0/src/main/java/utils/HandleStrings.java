package utils;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandleStrings {

	/**
	 * Verifica se a string é null e remove os sinais (<) e (>)
	 * 
	 * @param string
	 * @return
	 */
	public static String validateAndRemoveDangerousCharaters(String string) {

		String result = null;
		if (string != null) {
			String t1 = string.replaceAll("<", "");
			result = t1.replaceAll(">", "");
		}

		return result;
	}

	public static boolean isValid(String parameter) {
		return parameter != null && !parameter.trim().isEmpty();
	}

	@Deprecated
	/**
	 * Use o método isInvalid.
	 * @param parameters
	 * @return
	 */
	public static boolean isValid(String... parameters) {

		for (String parameter : parameters) {
			if (parameter == null || parameter.trim().isEmpty()) {
				return false;
			}
		}

		return true;

	}
	
	public static boolean isInvalid(String... parameters) {
		
		for (String parameter : parameters) {
			if (parameter == null || parameter.trim().isEmpty()) {
				System.out.println(parameter);
				return true;
			}
		}
		
		return false;
		
	}
	
	public static void ifParameterIsInvalidForwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String... parameters)
			throws ServletException, IOException {

		for (String parameter : parameters) {
			if (parameter == null || parameter.trim().isEmpty()) {
				HandleForward.errorForward(request, response, "Parâmetro inválido", "erro.jsp");
				return;
			}
		}

	}

	public static Long stringToLong(String clientID) {
		Long number = null;
		
		try {
			number= Long.parseLong(clientID);
		} catch (Exception e) {
			System.out.println("stringToLong() -> ERRO: " + e);
		}
		
		return number;
	}
	
}
