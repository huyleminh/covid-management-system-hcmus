package covid_management.dao;

import covid_management.models.Account;
import covid_management.models.User;
import covid_management.models.UserHistory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;
import shared.utilities.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class UserDAO implements DAO<User, Integer> {
	private static final Logger logger = LogManager.getLogger(UserDAO.class);

	public boolean isExistingIdentifierNumber(String identifierNumber) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		boolean isExisting;
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.User WHERE identifierNumber = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setString(1, identifierNumber);
			ResultSet resultSet = preparedStatement.executeQuery();
			isExisting = resultSet.next();
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

		return isExisting;
	}

	@Override
	public ArrayList<User> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<User> userList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM COVID_MANAGEMENT.User;");
			addUserList(userList, resultSet);
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

		return userList;
	}

	public ArrayList<User> getAllByStatus(byte status) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<User> userList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.User WHERE status = ?;";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setByte(1, status);

			ResultSet resultSet = preparedStatement.executeQuery();
			addUserList(userList, resultSet);
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

		return userList;
	}

	public ArrayList<Pair<User, String>> getAllWithLocationName() throws DBConnectionException {
		return getAllWithLocationNameUsingSearchBy("", "");
	}

	public ArrayList<User> getAllByInfectiousUserId(Integer infectiousUserId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<User> userList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.User WHERE infectiousUserId = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, infectiousUserId);
			ResultSet resultSet = preparedStatement.executeQuery();
			addUserList(userList, resultSet);
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

		return userList;
	}

	@Override
	public Optional<User> get(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<User> userOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.User WHERE userId = ?;";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				userOptional = Optional.of(new User(
						resultSet.getInt("userId"),
						resultSet.getString("identifierNumber"),
						resultSet.getNString("fullname"),
						resultSet.getShort("yearOfBirth"),
						resultSet.getInt("locationId"),
						resultSet.getByte("status"),
						resultSet.getInt("infectiousUserId"),
						resultSet.getNString("address")
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

		return userOptional;
	}

	public Optional<Object> getOneField(Integer id, String fieldName) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<Object> optionalValue = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT " + fieldName + " FROM COVID_MANAGEMENT.User WHERE userId = ?;";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Object value = null;

				switch (fieldName) {
					case "identifierNumber" -> value = resultSet.getString("identifierNumber");
					case "fullname" -> value = resultSet.getNString("fullname");
					case "yearOfBirth" -> value = resultSet.getShort("yearOfBirth");
					case "locationId" -> value = resultSet.getInt("locationId");
					case "status" -> value = resultSet.getByte("status");
					case "infectiousUserId" -> value = resultSet.getInt("infectiousUserId");
					case "wardId" -> value = resultSet.getInt("wardId");
					case "districtId" -> value = resultSet.getInt("districtId");
					case "provinceId" -> value = resultSet.getInt("provinceId");
				}

				optionalValue = Optional.ofNullable(value);
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

		return optionalValue;
	}

	@Override
	public void create(User entity) throws DBConnectionException {

	}

	public void create(User entity, String managerUsername, String description, short newCurrentSlots)
	throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementInsertUser = null;
		PreparedStatement preparedStatementInsertUserHistory = null;
		PreparedStatement preparedStatementInsertAccount = null;
		PreparedStatement preparedStatementUpdateLocation = null;

		try {
			String insertUserStatement = "INSERT COVID_MANAGEMENT.User " +
					"(identifierNumber, fullname, yearOfBirth, locationId, `status`, infectiousUserId, address) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?)";

			preparedStatementInsertUser = connection.prepareStatement(
					insertUserStatement,
					Statement.RETURN_GENERATED_KEYS
			);
			preparedStatementInsertUser.setString(1, entity.getIdentifierNumber());
			preparedStatementInsertUser.setNString(2, entity.getFullname());
			preparedStatementInsertUser.setShort(3, entity.getYearOfBirth());
			preparedStatementInsertUser.setInt(4, entity.getLocationId());
			preparedStatementInsertUser.setByte(5, entity.getStatus());
			preparedStatementInsertUser.setNString(7, entity.getAddress());
			if (entity.getInfectiousUserId() == -1)
				preparedStatementInsertUser.setNull(6, Types.INTEGER);
			else
				preparedStatementInsertUser.setInt(6, entity.getInfectiousUserId());


			connection.setAutoCommit(false);
			preparedStatementInsertUser.executeUpdate();
			ResultSet generatedKeys = preparedStatementInsertUser.getGeneratedKeys();

			if (generatedKeys.next()) {
				int userId = generatedKeys.getInt(1);
				Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

				String insertUserHistoryStatement = "INSERT COVID_MANAGEMENT.UserHistory " +
						"(managerUsername, userId, `date`, `description`, operationType) VALUES (?, ?, ?, ?, ?)";
				String insertAccountStatement = "INSERT COVID_MANAGEMENT.Account " +
						"(username, `password`, `role`, isActive, userId) VALUES (?, ?, ?, ?, ?)";
				String updateLocationStatement = "UPDATE COVID_MANAGEMENT.Location " +
						"SET currentSlots = ? WHERE locationId = ?";

				preparedStatementInsertUserHistory = connection.prepareStatement(insertUserHistoryStatement);
				preparedStatementInsertAccount = connection.prepareStatement(insertAccountStatement);
				preparedStatementUpdateLocation = connection.prepareStatement(updateLocationStatement);

				preparedStatementInsertUserHistory.setString(1, managerUsername);
				preparedStatementInsertUserHistory.setInt(2, userId);
				preparedStatementInsertUserHistory.setTimestamp(3, currentTimestamp);
				preparedStatementInsertUserHistory.setNString(4, description);
				preparedStatementInsertUserHistory.setByte(5, UserHistory.ADD_NEW_USER);

				preparedStatementInsertAccount.setString(1, entity.getIdentifierNumber());
				preparedStatementInsertAccount.setNull(2, Types.VARCHAR);
				preparedStatementInsertAccount.setByte(3, Account.USER);
				preparedStatementInsertAccount.setByte(4, Account.ACTIVE);
				preparedStatementInsertAccount.setInt(5, userId);

				preparedStatementUpdateLocation.setShort(1, newCurrentSlots);
				preparedStatementUpdateLocation.setInt(2, entity.getLocationId());

				preparedStatementInsertUserHistory.executeUpdate();
				preparedStatementInsertAccount.executeUpdate();
				preparedStatementUpdateLocation.executeUpdate();

				connection.commit();
			} else {
				connection.rollback();
			}
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
				if (preparedStatementInsertUser != null)
					preparedStatementInsertUser.close();
				if (preparedStatementInsertUserHistory != null)
					preparedStatementInsertUserHistory.close();
				if (preparedStatementInsertAccount != null)
					preparedStatementInsertAccount.close();
				if (preparedStatementUpdateLocation != null)
					preparedStatementUpdateLocation.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

	@Override
	public void update(User entity) throws DBConnectionException {

	}

	public void updateStatus(
			Integer id,
			String managerUsername,
			Byte newStatus,
			boolean changeInfectiousUserId,
			Integer newInfectiousUserId,
			String description,
			ArrayList<ArrayList<User>> involvedUserList,
			ArrayList<Byte> newStatusList,
			ArrayList<Integer> infectiousUserIdList,
			ArrayList<ArrayList<String>> descriptionList
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementUpdateInfectiousUser = null;
		PreparedStatement preparedStatementInsertInfectiousUserHistory = null;
		PreparedStatement preparedStatementUpdateInvolvedUsers = null;
		PreparedStatement preparedStatementInsertInvolvedUserHistories = null;

		try {
			// Update user (infectious person)
			String updateInfectiousUserStatement = "UPDATE COVID_MANAGEMENT.User SET status = ?";
			if (changeInfectiousUserId) {
				updateInfectiousUserStatement += ", infectiousUserId = " +
						(newInfectiousUserId == -1 ? "NULL" : newInfectiousUserId.toString());
			}
			updateInfectiousUserStatement += " WHERE userId = ?;";

			// Insert UserHistory for infectious person
			String insertInfectiousUserHistoryStatement = "INSERT INTO COVID_MANAGEMENT.UserHistory" +
					" (managerUsername, userId, `date`, `description`, operationType) VALUES (?, ?, ?, ?, ?);";

			String updateInvolvedUsersStatement = "UPDATE COVID_MANAGEMENT.User SET status = ?, infectiousUserId = ?" +
					" WHERE infectiousUserId = ?;";
			String insertInvolvedUserHistoriesStatement = "INSERT INTO COVID_MANAGEMENT.UserHistory" +
					" (managerUsername, userId, `date`, `description`, operationType) VALUES (?, ?, ?, ?, ?);";

			preparedStatementUpdateInfectiousUser = connection.prepareStatement(updateInfectiousUserStatement);
			preparedStatementInsertInfectiousUserHistory = connection.prepareStatement(
					insertInfectiousUserHistoryStatement);
			preparedStatementUpdateInvolvedUsers = connection.prepareStatement(updateInvolvedUsersStatement);
			preparedStatementInsertInvolvedUserHistories = connection.prepareStatement(
					insertInvolvedUserHistoriesStatement);

			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

			// Set values infectious user
			preparedStatementUpdateInfectiousUser.setByte(1, newStatus);
			preparedStatementUpdateInfectiousUser.setInt(2, id);
			preparedStatementInsertInfectiousUserHistory.setString(1, managerUsername);
			preparedStatementInsertInfectiousUserHistory.setInt(2, id);
			preparedStatementInsertInfectiousUserHistory.setTimestamp(3, currentTimestamp);
			preparedStatementInsertInfectiousUserHistory.setNString(4, description);
			preparedStatementInsertInfectiousUserHistory.setByte(5, UserHistory.DIRECTLY_CHANGE_STATUS);

			// Set values for involved users
			int count = involvedUserList.size();
			for (int i = 0; i < count; i++) {
				int countUser = involvedUserList.get(i).size();

				preparedStatementUpdateInvolvedUsers.setByte(1, newStatusList.get(i));
				if (User.STATUS_NAMES[newStatusList.get(i)].equals("F0"))
					preparedStatementUpdateInvolvedUsers.setNull(2, Types.INTEGER);
				else
					preparedStatementUpdateInvolvedUsers.setInt(2, infectiousUserIdList.get(i));
				preparedStatementUpdateInvolvedUsers.setInt(3, infectiousUserIdList.get(i));
				preparedStatementUpdateInvolvedUsers.addBatch();

				for (int j = 0; j < countUser; j++) {
					preparedStatementInsertInvolvedUserHistories.setString(1, managerUsername);
					preparedStatementInsertInvolvedUserHistories.setInt(2, involvedUserList.get(i).get(j).getUserId());
					preparedStatementInsertInvolvedUserHistories.setTimestamp(3, currentTimestamp);
					preparedStatementInsertInvolvedUserHistories.setNString(4, descriptionList.get(i).get(j));
					preparedStatementInsertInvolvedUserHistories.setByte(5, UserHistory.INDIRECTLY_CHANGE_STATUS);
					preparedStatementInsertInvolvedUserHistories.addBatch();
				}
			}

			connection.setAutoCommit(false);
			preparedStatementUpdateInfectiousUser.executeUpdate();
			preparedStatementInsertInfectiousUserHistory.executeUpdate();
			preparedStatementUpdateInvolvedUsers.executeBatch();
			preparedStatementInsertInvolvedUserHistories.executeBatch();
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
				if (preparedStatementUpdateInfectiousUser != null)
					preparedStatementUpdateInfectiousUser.close();
				if (preparedStatementInsertInfectiousUserHistory != null)
					preparedStatementInsertInfectiousUserHistory.close();
				if (preparedStatementUpdateInvolvedUsers != null)
					preparedStatementUpdateInvolvedUsers.close();
				if (preparedStatementInsertInvolvedUserHistories != null)
					preparedStatementInsertInvolvedUserHistories.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

	public void updateLocation(
			Integer id,
			Integer newLocationId,
			Short newCurrentSlots,
			Integer oldLocationId,
			Short oldCurrentSlots,
			String managerName,
			String description
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementUpdateUser = null;
		PreparedStatement preparedStatementUpdateLocation = null;
		PreparedStatement preparedStatementInsertUserHistory = null;

		try {
			preparedStatementUpdateUser = connection.prepareStatement(
					"UPDATE COVID_MANAGEMENT.User SET locationId = ? WHERE userId = ?;");
			preparedStatementUpdateLocation = connection.prepareStatement(
					"UPDATE COVID_MANAGEMENT.Location SET currentSlots = ? WHERE locationId = ?;");
			preparedStatementInsertUserHistory = connection.prepareStatement(
					"INSERT INTO COVID_MANAGEMENT.UserHistory (managerUsername, userId, `description`, operationType) VALUES (?, ?, ?, ?);");

			// Set values
			preparedStatementUpdateUser.setInt(1, newLocationId);
			preparedStatementUpdateUser.setInt(2, id);

			// Set values
			preparedStatementUpdateLocation.setShort(1, newCurrentSlots);
			preparedStatementUpdateLocation.setInt(2, newLocationId);
			preparedStatementUpdateLocation.addBatch();

			// Set values
			preparedStatementUpdateLocation.setShort(1, oldCurrentSlots);
			preparedStatementUpdateLocation.setInt(2, oldLocationId);
			preparedStatementUpdateLocation.addBatch();

			// Set values
			preparedStatementInsertUserHistory.setString(1, managerName);
			preparedStatementInsertUserHistory.setInt(2, id);
			preparedStatementInsertUserHistory.setNString(3, description);
			preparedStatementInsertUserHistory.setByte(4, UserHistory.CHANGE_QUARANTINE);

			connection.setAutoCommit(false);
			preparedStatementUpdateUser.executeUpdate();
			preparedStatementUpdateLocation.executeBatch();
			preparedStatementInsertUserHistory.executeUpdate();
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
				if (preparedStatementUpdateUser != null)
					preparedStatementUpdateUser.close();
				if (preparedStatementUpdateLocation != null)
					preparedStatementUpdateLocation.close();
				if (preparedStatementInsertUserHistory != null)
					preparedStatementInsertUserHistory.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

	@Override
	public void delete(User entity) throws DBConnectionException {

	}

	// Search by either full name or id card.
	public ArrayList<User> searchByAndFilterByStatus(String field, String value, byte status)
	throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<User> userList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.User WHERE status = ? AND " + field + " LIKE ";
			if (field.equals("identifierNumber")) {
				sqlStatement += "'%" + value + "%'";
			} else {  // full name
				sqlStatement += "N'%" + value + "%'";
			}

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setByte(1, status);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userList.add(new User(
						resultSet.getInt("userId"),
						resultSet.getString("identifierNumber"),
						resultSet.getNString("fullname"),
						resultSet.getShort("yearOfBirth"),
						resultSet.getInt("locationId"),
						resultSet.getByte("status"),
						resultSet.getInt("infectiousUserId"),
						resultSet.getNString("address")
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

		return userList;
	}

	// Search by either full name or id card.
	public ArrayList<Pair<User, String>> searchByAndResultIncludedLocationName(String field, String value)
	throws DBConnectionException {
		return getAllWithLocationNameUsingSearchBy(field, value);
	}

	public ArrayList<Pair<User, String>> sortByOneFieldWithLocationName(String field, boolean isAscending)
	throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<User, String>> userList = new ArrayList<>();
		Statement statement = null;

		try {
			String orderType = isAscending ? "ASC" : "DESC";
			String sqlStatement = "SELECT u.*, l.locationName AS 'locationName'" +
					" FROM COVID_MANAGEMENT.User u JOIN COVID_MANAGEMENT.Location l ON u.locationId = l.locationId" +
					" ORDER BY u." + field + " " + orderType;

			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);

			while (resultSet.next()) {
				userList.add(new Pair<>(
						new User(
								resultSet.getInt("userId"),
								resultSet.getString("identifierNumber"),
								resultSet.getNString("fullname"),
								resultSet.getShort("yearOfBirth"),
								resultSet.getInt("locationId"),
								resultSet.getByte("status"),
								resultSet.getInt("infectiousUserId"),
								resultSet.getNString("address")
						),
						resultSet.getNString("locationName")
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

		return userList;
	}

	private ArrayList<Pair<User, String>> getAllWithLocationNameUsingSearchBy(String field, String value)
	throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<User, String>> userList = new ArrayList<>();
		Statement statement = null;

		try {
			StringBuilder sqlStatement = new StringBuilder();
			sqlStatement.append("SELECT u.*, l.locationName AS 'locationName' ")
						.append("FROM COVID_MANAGEMENT.User u JOIN COVID_MANAGEMENT.Location l ")
						.append("ON u.locationId = l.locationId");

			if (value != null && !value.isBlank()) {
				String whereStatement = " WHERE u." + field + " LIKE ";
				whereStatement += field.equals("identifierNumber") ? ("'%" + value + "%'") : ("N'%" + value + "%'");
				sqlStatement.append(whereStatement);
			}

			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement.toString());

			while (resultSet.next()) {
				userList.add(new Pair<>(
						new User(
								resultSet.getInt("userId"),
								resultSet.getString("identifierNumber"),
								resultSet.getNString("fullname"),
								resultSet.getShort("yearOfBirth"),
								resultSet.getInt("locationId"),
								resultSet.getByte("status"),
								resultSet.getInt("infectiousUserId"),
								resultSet.getNString("address")
						),
						resultSet.getNString("locationName")
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

		return userList;
	}

	private void addUserList(ArrayList<User> userList, ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			userList.add(new User(
					resultSet.getInt("userId"),
					resultSet.getString("identifierNumber"),
					resultSet.getNString("fullname"),
					resultSet.getShort("yearOfBirth"),
					resultSet.getInt("locationId"),
					resultSet.getByte("status"),
					resultSet.getInt("infectiousUserId"),
					resultSet.getNString("address")
			));
		}
	}
}
