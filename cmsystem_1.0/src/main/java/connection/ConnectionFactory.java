package connection;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory {
	
	private static DataSource dataSource;


	static {
		Context initialContext;
		try {
			initialContext = new InitialContext();
			dataSource = (DataSource) initialContext.lookup("java:/cmsystem_DS");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	public static Connection conectar() {
		if (dataSource == null) {
			throw new RuntimeException("Data Source was not properly initialized");
		}
		try {
			
			return dataSource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
