package com.dao;

import com.models.Account;
import com.models.User;
import com.models.UserHistory;
import com.utilities.Pair;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User, Integer> {
	public static final byte CONNECTION_ERROR = 0;
	public static final byte NON_EXISTENCE = 1;
	public static final byte EXISTING = 2;

	public byte isExistingIdentifierNumber(String identifierNumber) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		byte state = CONNECTION_ERROR;

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.User WHERE identifierNumber = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setString(1, identifierNumber);
				ResultSet resultSet = preparedStatement.executeQuery();
				state = (resultSet.next()) ? EXISTING : NON_EXISTENCE;
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - line 40 <<<");
				e.printStackTrace();
				state = CONNECTION_ERROR;
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - line 50 <<<");
						state = CONNECTION_ERROR;
					}
				}
			}
		}

		return state;
	}

	@Override
	public List<User> getAll() {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<User> userList = new ArrayList<>();

		if (connection != null) {
			Statement statement = null;

			try {
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM COVID_MANAGEMENT.User;");
				addUserList(userList, resultSet);
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - line 40 <<<");
				e.printStackTrace();

				userList.clear();
				userList.add(User.emptyInstance);
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - line 50 <<<");

						userList.clear();
						userList.add(User.emptyInstance);
					}
				}
			}
		} else {
			userList.add(User.emptyInstance);
		}

		return userList;
	}

	public List<User> getAllByStatus(byte status) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<User> userList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.User WHERE status = ?;";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setByte(1, status);

				ResultSet resultSet = preparedStatement.executeQuery();
				addUserList(userList, resultSet);
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - getAllByStatus(byte) - catch block <<<");
				e.printStackTrace();

				userList.clear();
				userList.add(User.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - getAllByStatus(byte) - finally block <<<");

						userList.clear();
						userList.add(User.emptyInstance);
					}
				}
			}
		} else {
			userList.add(User.emptyInstance);
		}

		return userList;
	}

	public List<Pair<User, String>> getAllWithLocationName() {
		return getAllWithLocationNameUsingSearchBy("", "");
	}

	public List<User> getAllByInfectiousUserId(Integer infectiousUserId) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<User> userList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.User WHERE infectiousUserId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, infectiousUserId);
				ResultSet resultSet = preparedStatement.executeQuery();
				addUserList(userList, resultSet);
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - line 72 <<<");
				e.printStackTrace();

				userList.clear();
				userList.add(User.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - line 82 <<<");

						userList.clear();
						userList.add(User.emptyInstance);
					}
				}
			}
		} else {
			userList.add(User.emptyInstance);
		}

		return userList;
	}

	@Override
	public Optional<User> get(Integer id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<User> userOptional = Optional.of(User.emptyInstance);

		if (connection != null) {
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
				} else {
					userOptional = Optional.empty();
				}
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - line 128 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - line 135 <<<");
						userOptional = Optional.of(User.emptyInstance);
					}
				}
			}
		}

		return userOptional;
	}

	public Optional<Object> getOneField(Integer id, String fieldName) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Object> optionalValue = Optional.of(User.getEmptyAttribute(fieldName));

		if (connection != null) {
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
						case "fullname" 		-> value = resultSet.getNString("fullname");
						case "yearOfBirth" 		-> value = resultSet.getShort("yearOfBirth");
						case "locationId" 		-> value = resultSet.getInt("locationId");
						case "status" 			-> value = resultSet.getByte("status");
						case "infectiousUserId" -> value = resultSet.getInt("infectiousUserId");
						case "wardId" 			-> value = resultSet.getInt("wardId");
						case "districtId" 		-> value = resultSet.getInt("districtId");
						case "provinceId" 		-> value = resultSet.getInt("provinceId");
					}

					optionalValue = Optional.ofNullable(value);
				} else {
					optionalValue = Optional.empty();
				}
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - line 179 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - line 186 <<<");
						optionalValue = Optional.of(User.getEmptyAttribute(fieldName));
					}
				}
			}
		}

		return optionalValue;
	}

	@Override
	public boolean create(User entity) {
		return false;
	}

	public boolean create(User entity, String managerUsername, String description, short newCurrentSlots) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		boolean isCreated = false;

		if (connection != null) {
			PreparedStatement preparedStatementInsertUser = null;
			PreparedStatement preparedStatementInsertUserHistory = null;
			PreparedStatement preparedStatementInsertAccount = null;
			PreparedStatement preparedStatementUpdateLocation = null;

			try {
				String insertUserStatement = "INSERT COVID_MANAGEMENT.User " +
						"(identifierNumber, fullname, yearOfBirth, locationId, `status`, infectiousUserId, address) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?)";

				preparedStatementInsertUser = connection.prepareStatement(insertUserStatement, Statement.RETURN_GENERATED_KEYS);
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
				int insertUserRowAffected = preparedStatementInsertUser.executeUpdate();
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

					int insertUserHistoryRowAffected = preparedStatementInsertUserHistory.executeUpdate();
					int insertAccountRowAffected = preparedStatementInsertAccount.executeUpdate();
					int updateLocationRowAffected = preparedStatementUpdateLocation.executeUpdate();

					connection.commit();
					isCreated = true;
				} else {
					connection.rollback();
				}
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - create(User, String, String) method - catch block <<<");
				e.printStackTrace();

				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				isCreated = false;
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
					e.printStackTrace();
					System.out.println(">>> UserDAO.java - create(User, String, String) method - finally block <<<");
					isCreated = false;
				}
			}
		}

		return isCreated;
	}

	@Override
	public boolean update(User entity) {
		return false;
	}

	public boolean updateStatus(
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
	) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		boolean isUpdated = false;

		if (connection != null) {
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
				String insertInfectiousUserHistoryStatement = "INSERT INTO COVID_MANAGEMENT.UserHistory " +
						"(managerUsername, userId, `date`, `description`, operationType) VALUES (?, ?, ?, ?, ?);";

				String updateInvolvedUsersStatement = "UPDATE COVID_MANAGEMENT.User SET status = ?, infectiousUserId = ? WHERE infectiousUserId = ?;";
				String insertInvolvedUserHistoriesStatement = "INSERT INTO COVID_MANAGEMENT.UserHistory " +
						"(managerUsername, userId, `date`, `description`, operationType) VALUES (?, ?, ?, ?, ?);";

				preparedStatementUpdateInfectiousUser = connection.prepareStatement(updateInfectiousUserStatement);
				preparedStatementInsertInfectiousUserHistory = connection.prepareStatement(insertInfectiousUserHistoryStatement);
				preparedStatementUpdateInvolvedUsers = connection.prepareStatement(updateInvolvedUsersStatement);
				preparedStatementInsertInvolvedUserHistories = connection.prepareStatement(insertInvolvedUserHistoriesStatement);

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
				int updateInfectiousUserRowAffected = preparedStatementUpdateInfectiousUser.executeUpdate();
				int insertInfectiousUserHistoryRowAffected = preparedStatementInsertInfectiousUserHistory.executeUpdate();
				int[] updateInvolvedUsersRowAffected = preparedStatementUpdateInvolvedUsers.executeBatch();
				int[] insertInvolvedUserHistoriesRowAffected = preparedStatementInsertInvolvedUserHistories.executeBatch();

				connection.commit();
				isUpdated = true;
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - updateStatus() method - catch block <<<");
				e.printStackTrace();

				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				isUpdated = false;
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
					e.printStackTrace();
					System.out.println(">>> UserDAO.java - updateStatus() method - finally block <<<");
					isUpdated = false;
				}
			}
		}

		return isUpdated;
	}

	public boolean updateLocation(
			Integer id,
			Integer newLocationId,
			Short newCurrentSlots,
			Integer oldLocationId,
			Short oldCurrentSlots,
			String managerName,
			String description
	) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		boolean isUpdated = false;

		if (connection != null) {
			PreparedStatement preparedStatementUpdateUser = null;
			PreparedStatement preparedStatementUpdateLocation = null;
			PreparedStatement preparedStatementInsertUserHistory = null;

			try {
				preparedStatementUpdateUser = connection.prepareStatement("UPDATE COVID_MANAGEMENT.User SET locationId = ? WHERE userId = ?;");
				preparedStatementUpdateLocation = connection.prepareStatement("UPDATE COVID_MANAGEMENT.Location SET currentSlots = ? WHERE locationId = ?;");
				preparedStatementInsertUserHistory = connection.prepareStatement("INSERT INTO COVID_MANAGEMENT.UserHistory (managerUsername, userId, `description`, operationType) VALUES (?, ?, ?, ?);");

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

				int updateUserRowAffected = preparedStatementUpdateUser.executeUpdate();
				int[] updateLocationRowAffected = preparedStatementUpdateLocation.executeBatch();
				int insertUserHistoryRowAffected = preparedStatementInsertUserHistory.executeUpdate();

				isUpdated = true;
				connection.commit();
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - updateLocation() method - catch block <<<");
				e.printStackTrace();

				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				isUpdated = false;
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
					e.printStackTrace();
					System.out.println(">>> UserDAO.java - updateLocation() method - finally block <<<");
					isUpdated = false;
				}
			}
		}

		return isUpdated;
	}

	@Override
	public boolean delete(User entity) {
		return false;
	}

	// Search by either full name or id card.
	public List<User> searchByAndFilterByStatus(String field, String value, byte status) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<User> userList = new ArrayList<>();

		if (connection != null) {
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
				System.out.println(">>> UserDAO.java - searchByAndFilterByStatus(String, String, byte) method - catch block <<<");
				e.printStackTrace();

				userList.clear();
				userList.add(User.emptyInstance);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - searchByAndFilterByStatus(String, String, byte) method - finally block <<<");

						userList.clear();
						userList.add(User.emptyInstance);
					}
				}
			}
		} else {
			userList.add(User.emptyInstance);
		}

		return userList;
	}

	// Search by either full name or id card.
	public List<Pair<User, String>> searchByAndResultIncludedLocationName(String field, String value) {
		return getAllWithLocationNameUsingSearchBy(field, value);
	}

	public List<Pair<User, String>> sortByOneFieldWithLocationName(String field, boolean isAscending) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Pair<User, String>> userList = new ArrayList<>();

		if (connection != null) {
			Statement statement = null;

			try {
				String orderType = isAscending ? "ASC" : "DESC";
				String sqlStatement = "SELECT u.*, l.locationName AS 'locationName'" +
						" FROM COVID_MANAGEMENT.User u JOIN COVID_MANAGEMENT.Location l ON u.locationId = l.locationId" +
						" ORDER BY u." + field + " " + orderType;

				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);

				while (resultSet.next()) {
					userList.add(new Pair<> (
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
				System.out.println(">>> UserDAO.java - sortByOneFieldWithLocationName() method - catch block <<<");
				e.printStackTrace();

				userList.clear();
				userList.add(new Pair<>(User.emptyInstance, ""));
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - sortByOneFieldWithLocationName() method - finally block <<<");

						userList.clear();
						userList.add(new Pair<>(User.emptyInstance, ""));
					}
				}
			}
		} else {
			userList.add(new Pair<>(User.emptyInstance, ""));
		}

		return userList;
	}

	private List<Pair<User, String>> getAllWithLocationNameUsingSearchBy(String field, String value) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Pair<User, String>> userList = new ArrayList<>();

		if (connection != null) {
			Statement statement = null;

			try {
				StringBuilder sqlStatement = new StringBuilder();
				sqlStatement.append("SELECT u.*, l.locationName AS 'locationName' ")
							.append("FROM COVID_MANAGEMENT.User u JOIN COVID_MANAGEMENT.Location l ON u.locationId = l.locationId");

				if (value != null && !value.isBlank()) {
					String whereStatement = " WHERE u." + field + " LIKE ";
					whereStatement += field.equals("identifierNumber") ? ("'%" + value + "%'") : ("N'%" + value + "%'");
					sqlStatement.append(whereStatement);
				}

				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement.toString());

				while (resultSet.next()) {
					userList.add(new Pair<> (
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
				System.out.println(">>> UserDAO.java - getAllWithLocationNameUsingSearchBy(String, String) - catch block <<<");
				e.printStackTrace();

				userList.clear();
				userList.add(new Pair<>(User.emptyInstance, ""));
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - getAllWithLocationNameUsingSearchBy(String, String) - finally block <<<");

						userList.clear();
						userList.add(new Pair<>(User.emptyInstance, ""));
					}
				}
			}
		} else {
			userList.add(new Pair<>(User.emptyInstance, ""));
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
