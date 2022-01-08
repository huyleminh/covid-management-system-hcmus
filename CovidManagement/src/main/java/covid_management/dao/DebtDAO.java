package covid_management.dao;

import covid_management.models.Debt;
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

public class DebtDAO implements DAO<Debt, Integer> {
	private static final Logger logger = LogManager.getLogger(DebtDAO.class);

	@Override
	public ArrayList<Debt> getAll() throws DBConnectionException {
		return null;
	}

	public ArrayList<Debt> getAllByUserId(Integer userId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Debt> debtList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Debt WHERE userId = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				debtList.add(new Debt(
						resultSet.getInt("debtId"),
						resultSet.getInt("userId"),
						resultSet.getTimestamp("debtDate"),
						resultSet.getInt("totalDebt")
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

		return debtList;
	}

	@Override
	public Optional<Debt> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void create(Debt entity) throws DBConnectionException {

	}

	@Override
	public void update(Debt entity) throws DBConnectionException {

	}

	@Override
	public void delete(Debt entity) throws DBConnectionException {

	}

	public int getTotalDebtByUsedId(Integer userId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;
		int totalDebt = 0;

		try {
			final String sqlStatement = "SELECT SUM(totalDebt) AS 'totalDebt'" +
					" FROM COVID_MANAGEMENT.Debt WHERE userId = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
				totalDebt = resultSet.getInt(1);
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

		return totalDebt;
	}
}
