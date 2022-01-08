package shared.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.utilities.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonDBConnection {
	private static final Logger logger = LogManager.getLogger(SingletonDBConnection.class);

	private static final String  JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private Connection connection = null;

	private SingletonDBConnection() {
		connect();
	}

	private static class BillPughSingleton {
		private static final SingletonDBConnection INSTANCE = new SingletonDBConnection();
	}

	public static SingletonDBConnection getInstance() {
		return BillPughSingleton.INSTANCE;
	}

	public void connect() {
		try {
			if (connection != null && !connection.isClosed())
				closeConnection();

			Class.forName(JDBC_DRIVER);

			logger.info("Connecting to the database");
			connection = DriverManager.getConnection(
					Constants.DOTENV.get("DB_URL"),
					Constants.DOTENV.get("DB_USER"),
					Constants.DOTENV.get("DB_PASSWORD")
			);
			logger.info("Connected to the database");
		} catch (SQLException sqlException) {
			logger.error("Can not connect to MySQL");
			logger.error(sqlException.getMessage());
			logger.trace(sqlException.getStackTrace());
		} catch (ClassNotFoundException e) {
			logger.error(JDBC_DRIVER + " class not found.");
			logger.error(e.getMessage());
			logger.trace(e.getStackTrace());
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			connection = null;
			logger.info("Database connection is closed");
		}
	}
}
