package covid_management.dao;

import covid_management.models.Ward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class WardDAO implements DAO<Ward, Integer> {
	private static final Logger logger = LogManager.getLogger(WardDAO.class);

	@Override
	public ArrayList<Ward> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Ward> wardList = new ArrayList<>();
		Statement statement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Ward;";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);

			while (resultSet.next()) {
				wardList.add(new Ward(
						resultSet.getInt("wardId"),
						resultSet.getNString("wardName"),
						resultSet.getInt("districtId")
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

		return wardList;
	}

	@Override
	public Optional<Ward> get(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<Ward> wardOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Ward WHERE wardId = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				wardOptional = Optional.of(new Ward(
						resultSet.getInt("wardId"),
						resultSet.getNString("wardName"),
						resultSet.getInt("districtId")
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

		return wardOptional;
	}

	@Override
	public void create(Ward entity) throws DBConnectionException {

	}

	@Override
	public void update(Ward entity) throws DBConnectionException {

	}

	@Override
	public void delete(Ward entity) throws DBConnectionException {

	}
}
