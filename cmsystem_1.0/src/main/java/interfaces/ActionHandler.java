package interfaces;

import java.io.IOException;

import org.slf4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ActionHandler {
	
	void execute(HttpServletRequest request, HttpServletResponse response, Logger logger) 
			throws ServletException, IOException;

}
