package covid_management.dao;

import covid_management.models.Province;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class ProvinceDAO implements DAO<Province, Integer> {
	private static final Logger logger = LogManager.getLogger(ProvinceDAO.class);

	@Override
	public ArrayList<Province> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Province> provinceList = new ArrayList<>();
		Statement statement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Province;";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);

			while (resultSet.next()) {
				provinceList.add(new Province(
						resultSet.getInt("provinceId"),
						resultSet.getNString("provinceName")
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

		return provinceList;
	}

	@Override
	public Optional<Province> get(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<Province> provinceOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Province WHERE provinceId = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				provinceOptional = Optional.of(new Province(
						resultSet.getInt("provinceId"),
						resultSet.getNString("provinceName")
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

		return provinceOptional;
	}

	@Override
	public void create(Province entity) throws DBConnectionException {

	}

	@Override
	public void update(Province entity) throws DBConnectionException {

	}

	@Override
	public void delete(Province entity) throws DBConnectionException {

	}
}
