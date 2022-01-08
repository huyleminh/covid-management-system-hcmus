package covid_management.dao;

import covid_management.models.District;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class DistrictDAO implements DAO<District, Integer> {
	private static final Logger logger = LogManager.getLogger(DistrictDAO.class);

	@Override
	public ArrayList<District> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<District> districtList = new ArrayList<>();
		Statement statement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.District;";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);

			while (resultSet.next()) {
				districtList.add(new District(
						resultSet.getInt("districtId"),
						resultSet.getNString("districtName"),
						resultSet.getInt("provinceId")
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

		return districtList;
	}

	@Override
	public Optional<District> get(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<District> districtOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.District WHERE districtId = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				districtOptional = Optional.of(new District(
						resultSet.getInt("districtId"),
						resultSet.getNString("districtName"),
						resultSet.getInt("provinceId")
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

		return districtOptional;
	}

	@Override
	public void create(District entity) throws DBConnectionException {

	}

	@Override
	public void update(District entity) throws DBConnectionException {

	}

	@Override
	public void delete(District entity) throws DBConnectionException {

	}
}
