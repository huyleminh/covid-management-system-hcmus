package com.utilities;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// ----------------------------------
// two lines used for SingletonDBConnectionExample class.
import java.sql.ResultSet;
import java.sql.Statement;
// ----------------------------------

public class SingletonDBConnection {
	private static final String  JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private Connection connection;

	private SingletonDBConnection() {
		try {
			Class.forName(JDBC_DRIVER);

			Dotenv dotenv = Dotenv.load();

			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(
					dotenv.get("DB_URL"),
					dotenv.get("DB_USER"),
					dotenv.get("DB_PASSWORD")
			);
			System.out.println("Connected!");
		} catch (SQLException sqlException) {
			System.out.println("Cannot connect to MySQL.");
			sqlException.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println(JDBC_DRIVER + " class not found.");
			e.printStackTrace();
		}
	}

	private static class BillPughSingleton {
		private static final SingletonDBConnection INSTANCE = new SingletonDBConnection();
	}

	public static SingletonDBConnection getInstance() {
		return BillPughSingleton.INSTANCE;
	}

	public Connection getConnection() {
		return connection;
	}
}

class SingletonDBConnectionExample {
	public static void main(String[] args) {
		SingletonDBConnection sharedInstance = SingletonDBConnection.getInstance();
		Connection connection = sharedInstance.getConnection();

		if (connection != null) {
			try {
				Statement statement = connection.createStatement();
				ResultSet accountList = statement.executeQuery("select * from Account");

				while (accountList.next()) {
					System.out.print("(username: " + accountList.getString("username") + ", ");
					System.out.print("password: " + accountList.getString("password") + ", ");
					System.out.print("role: " + accountList.getByte("role") + ", ");
					System.out.println("active: " + accountList.getByte("active") + ")");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
