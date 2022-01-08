package covid_management.dao;

import covid_management.models.PaymentHistory;
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

public class PaymentHistoryDAO implements DAO<PaymentHistory, Integer> {
	private static final Logger logger = LogManager.getLogger(PaymentHistoryDAO.class);

	@Override
	public ArrayList<PaymentHistory> getAll() throws DBConnectionException {
		return null;
	}

	public ArrayList<PaymentHistory> getAllByUserId(Integer userId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<PaymentHistory> paymentHistoryList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.PaymentHistory WHERE userId = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				paymentHistoryList.add(new PaymentHistory(
						resultSet.getInt("historyId"),
						resultSet.getInt("userId"),
						resultSet.getTimestamp("date"),
						resultSet.getInt("paymentAmount")
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

		return paymentHistoryList;
	}

	public int getTotalAmountByUserId(Integer userId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		int totalAmount = 0;
		PreparedStatement preparedStatement = null;

		try {
			final String sqlStatement = "SELECT SUM(paymentAmount) AS 'totalAmount'" +
					" FROM COVID_MANAGEMENT.PaymentHistory WHERE userId = ?;";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
				totalAmount = resultSet.getInt("totalAmount");
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

		return totalAmount;
	}

	@Override
	public Optional<PaymentHistory> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void create(PaymentHistory entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			final String sqlStatement = "INSERT COVID_MANAGEMENT.PaymentHistory (userId, date, paymentAmount)" +
					" VALUES (?, ?, ?)";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, entity.getUserId());
			preparedStatement.setTimestamp(2, entity.getDate());
			preparedStatement.setInt(3, entity.getPaymentAmount());

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
	public void update(PaymentHistory entity) throws DBConnectionException {

	}

	@Override
	public void delete(PaymentHistory entity) throws DBConnectionException {

	}
}
