package covid_management.dao;

import covid_management.models.SystemInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.SystemInfo WHERE id = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				systemInfoOptional = Optional.of(new SystemInfo(
						resultSet.getInt("id"),
						resultSet.getByte("firstLoggedIn")
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

	public void updateOneField(SystemInfo entity, String fieldName) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "UPDATE COVID_MANAGEMENT.SystemInfo SET " + fieldName + " = ?;";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setByte(1, entity.getFirstLoggedIn());

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
