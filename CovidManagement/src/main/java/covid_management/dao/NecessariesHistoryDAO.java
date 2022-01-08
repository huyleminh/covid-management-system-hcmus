package covid_management.dao;

import covid_management.models.NecessariesHistory;
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

public class NecessariesHistoryDAO implements DAO<NecessariesHistory, Integer> {
	private static final Logger logger = LogManager.getLogger(NecessariesHistoryDAO.class);

	@Override
	public ArrayList<NecessariesHistory> getAll() throws DBConnectionException {
		return null;
	}

	public ArrayList<NecessariesHistory> getAllByManagerUsername(String managerUsername)
	throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<NecessariesHistory> necessariesHistoryList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.NecessariesHistory WHERE managerUsername = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setString(1, managerUsername);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				necessariesHistoryList.add(new NecessariesHistory(
						resultSet.getInt("historyId"),
						resultSet.getString("managerUsername"),
						resultSet.getTimestamp("date"),
						resultSet.getNString("description"),
						resultSet.getByte("operationType")
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

		return necessariesHistoryList;
	}

	@Override
	public Optional<NecessariesHistory> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void create(NecessariesHistory entity) throws DBConnectionException {

	}

	@Override
	public void update(NecessariesHistory entity) throws DBConnectionException {

	}

	@Override
	public void delete(NecessariesHistory entity) throws DBConnectionException {

	}
}
