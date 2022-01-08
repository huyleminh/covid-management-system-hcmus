package payment_system.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import payment_system.models.PaymentAccount;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PaymentAccountDAO implements DAO<PaymentAccount, Integer> {
	private static final Logger logger = LogManager.getLogger(PaymentAccountDAO.class);

	@Override
	public ArrayList<PaymentAccount> getAll() throws DBConnectionException {
		return null;
	}

	@Override
	public Optional<PaymentAccount> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	public Optional<PaymentAccount> getByUserIdentifierNumber(String identifierNumber) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<PaymentAccount> systemInfoOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM PAYMENT_SYSTEM.PaymentAccount WHERE userIdentifierNumber = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setString(1, identifierNumber);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				systemInfoOptional = Optional.of(new PaymentAccount(
						resultSet.getInt("paymentId"),
						resultSet.getInt("balance"),
						resultSet.getNString("fullname"),
						resultSet.getString("userIdentifierNumber")
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
	public void create(PaymentAccount entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "INSERT INTO PAYMENT_SYSTEM.PaymentAccount" +
					" (balance, fullname, userIdentifierNumber) VALUES (?, ?, ?)";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, entity.getBalance());
			preparedStatement.setNString(2, entity.getFullname());
			preparedStatement.setString(3, entity.getUserIdentifierNumber());

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
	public void update(PaymentAccount entity) throws DBConnectionException {

	}

	@Override
	public void delete(PaymentAccount entity) throws DBConnectionException {

	}
}
