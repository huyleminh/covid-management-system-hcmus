package payment_system.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import payment_system.models.Transaction;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Constants;
import shared.utilities.UtilityFunctions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class TransactionDAO implements DAO<Transaction, Integer> {
	private static final Logger logger = LogManager.getLogger(TransactionDAO.class);

	@Override
	public ArrayList<Transaction> getAll() throws DBConnectionException {
		return null;
	}

	public Vector<Vector<Object>> getAllTransactionHistories() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Vector<Vector<Object>> transactionHistoryList = new Vector<>();
		Statement statement = null;

		try {
			String sqlStatement = "SELECT pa.userIdentifierNumber, pa.fullname, t.paymentAmount, t.transactionDate" +
					" FROM PAYMENT_SYSTEM.Transaction t" +
					" JOIN PAYMENT_SYSTEM.PaymentAccount pa on pa.paymentId = t.sourceAccount" +
					" ORDER BY t.transactionDate DESC";

			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);

			while (resultSet.next()) {
				transactionHistoryList.add(new Vector<>(
						List.of(
								resultSet.getString("userIdentifierNumber"),
								resultSet.getNString("fullname"),
								resultSet.getInt("paymentAmount"),
								UtilityFunctions.formatTimestamp(
										Constants.TIMESTAMP_WITHOUT_NANOSECOND,
										resultSet.getTimestamp("transactionDate")
								)
						)
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

		return transactionHistoryList;
	}

	@Override
	public Optional<Transaction> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void create(Transaction entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementInsertTransaction = null;
		PreparedStatement preparedStatementUpdatePaymentAccount = null;
		PreparedStatement preparedStatementUpdateSystemInfo = null;

		try {
			String insertTransactionStatement = "INSERT INTO PAYMENT_SYSTEM.Transaction" +
					" (sourceAccount, transactionDate, paymentAmount) VALUES (?, ?, ?)";
			String updatePaymentAccountStatement = "UPDATE PAYMENT_SYSTEM.PaymentAccount" +
					" SET balance = balance - ? WHERE paymentId = ?";
			String updateSystemInfoStatement = "UPDATE PAYMENT_SYSTEM.SystemInfo" +
					" SET balance = balance + ? WHERE id = 1";

			preparedStatementInsertTransaction = connection.prepareStatement(insertTransactionStatement);
			preparedStatementUpdatePaymentAccount = connection.prepareStatement(updatePaymentAccountStatement);
			preparedStatementUpdateSystemInfo = connection.prepareStatement(updateSystemInfoStatement);

			preparedStatementInsertTransaction.setInt(1, entity.getSourceAccount());
			preparedStatementInsertTransaction.setTimestamp(2, entity.getTransactionDate());
			preparedStatementInsertTransaction.setInt(3, entity.getPaymentAmount());
			preparedStatementUpdatePaymentAccount.setInt(1, entity.getPaymentAmount());
			preparedStatementUpdatePaymentAccount.setInt(2, entity.getSourceAccount());
			preparedStatementUpdateSystemInfo.setInt(1, entity.getPaymentAmount());

			connection.setAutoCommit(false);
			preparedStatementInsertTransaction.executeUpdate();
			preparedStatementUpdatePaymentAccount.executeUpdate();
			preparedStatementUpdateSystemInfo.executeUpdate();
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
				if (preparedStatementInsertTransaction != null)
					preparedStatementInsertTransaction.close();
				if (preparedStatementUpdatePaymentAccount != null)
					preparedStatementUpdatePaymentAccount.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

	@Override
	public void update(Transaction entity) throws DBConnectionException {

	}

	@Override
	public void delete(Transaction entity) throws DBConnectionException {

	}
}
