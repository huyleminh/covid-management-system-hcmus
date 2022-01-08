package payment_system.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import payment_system.models.SystemInfo;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class SystemInfoDAO implements DAO<SystemInfo, Integer> {
	private static final Logger logger = LogManager.getLogger(SystemInfoDAO.class);

	@Override
	public ArrayList<SystemInfo> getAll() throws DBConnectionException {
		return null;
	}

	@Override
	public Optional<SystemInfo> get(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<SystemInfo> systemInfoOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM PAYMENT_SYSTEM.SystemInfo WHERE id = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				systemInfoOptional = Optional.of(new SystemInfo(
						resultSet.getInt("id"),
						resultSet.getByte("firstLoggedIn"),
						resultSet.getString("bankAccountNumber"),
						resultSet.getInt("balance"),
						resultSet.getInt("defaultBalanceOfNewAccount")
				));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
					logger.error(e.getStackTrace());
				}
			}
		}

		return systemInfoOptional;
	}

	@Override
	public void create(SystemInfo entity) throws DBConnectionException {

	}

	@Override
	public void update(SystemInfo entity) throws DBConnectionException {

	}

	@Override
	public void delete(SystemInfo entity) throws DBConnectionException {

	}
}
