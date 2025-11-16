package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;

public class UtilsDao {

	/**
	 * 
	 * @param rs
	 * @param stmt
	 * @param connection
	 * @param logger
	 */
	public static void closeDAOResources(ResultSet rs, PreparedStatement stmt, Connection connection, Logger logger) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Erro ao fechar o resultSet: {}", e.getMessage());
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("Erro ao fechar o preparedStatment: {}", e.getMessage());
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Erro ao fechar a connection: {}", e.getMessage());
			}
		}
	}
	
	public static void closeDAOResources(ResultSet rs,  Connection connection, Logger logger, PreparedStatement... stmts) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Erro ao fechar o resultSet", e);
			}
		}
		
		for(PreparedStatement stmt : stmts) {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error("Erro ao fechar o preparedStatement: {}", e.getMessage());
				}
			}
		}
		
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Erro ao fechar a connection: {}", e.getMessage());
			}
		}
	}
}
