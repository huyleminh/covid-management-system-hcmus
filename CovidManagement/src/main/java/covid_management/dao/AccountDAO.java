package covid_management.dao;

import covid_management.models.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class AccountDAO implements DAO<Account, String> {
	private static final Logger logger = LogManager.getLogger(AccountDAO.class);

	@Override
	public ArrayList<Account> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Account> accountList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM COVID_MANAGEMENT.Account;");

			while (resultSet.next()) {
				accountList.add(new Account(
						resultSet.getString("username"),
						resultSet.getString("password"),
						resultSet.getByte("role"),
						resultSet.getByte("isActive"),
						resultSet.getInt("userId")
				));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return accountList;
	}

	public ArrayList<Account> getAllByRole(byte role) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Account> accountList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Account WHERE role = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setByte(1, role);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				accountList.add(new Account(
						resultSet.getString("username"),
						resultSet.getString("password"),
						resultSet.getByte("role"),
						resultSet.getByte("isActive"),
						resultSet.getInt("userId")
				));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return accountList;
	}

	@Override
	public Optional<Account> get(String username) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<Account> accountOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Account WHERE username = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				accountOptional = Optional.of(new Account(
						resultSet.getString("username"),
						resultSet.getString("password"),
						resultSet.getByte("role"),
						resultSet.getByte("isActive"),
						resultSet.getInt("userId")
				));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return accountOptional;
	}

	@Override
	public void create(Account entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "INSERT INTO COVID_MANAGEMENT.Account (username, `password`, `role`, isActive, userId) " +
					"VALUES (?, ?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for non-null fields.
			preparedStatement.setString(1, entity.getUsername());
			preparedStatement.setByte(3, entity.getRole());
			preparedStatement.setByte(4, entity.getIsActive());

			// Set values for nullable fields.
			if (entity.getPassword() == null)
				preparedStatement.setNull(2, Types.VARCHAR);
			else
				preparedStatement.setString(2, entity.getPassword());

			if (entity.getUserId() == -1)
				preparedStatement.setNull(5, Types.INTEGER);
			else
				preparedStatement.setInt(5, entity.getUserId());

			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());

			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

	@Override
	public void update(Account entity) throws DBConnectionException {

	}

	@Override
	public void delete(Account entity) throws DBConnectionException {

	}

	public void updateOneFieldByUsername(Account entity, String fieldName) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "UPDATE COVID_MANAGEMENT.Account SET " + fieldName + " = ? WHERE username = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setString(2, entity.getUsername());
			switch (fieldName) {
				case "password" -> preparedStatement.setString(1, entity.getPassword());
				case "role" -> preparedStatement.setByte(1, entity.getRole());
				case "isActive" -> preparedStatement.setByte(1, entity.getIsActive());
				case "userId" -> preparedStatement.setInt(1, entity.getUserId());
			}

			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());

			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}
}
