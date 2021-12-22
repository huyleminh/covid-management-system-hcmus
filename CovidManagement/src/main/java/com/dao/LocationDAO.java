package com.dao;

import com.models.Location;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDAO implements DAO<Location, Integer> {
	@Override
	public List<Location> getAll() {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Location> locationList = new ArrayList<>();

		if (connection != null) {
			Statement statement = null;

			try {
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM COVID_MANAGEMENT.Location;");

				while (resultSet.next()) {
					locationList.add(new Location(
							resultSet.getInt("locationId"),
							resultSet.getNString("locationName"),
							resultSet.getShort("capacity"),
							resultSet.getShort("currentSlots")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> LocationDAO.java - line 33 <<<");
				e.printStackTrace();

				locationList.clear();
				locationList.add(Location.emptyLocation);
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> LocationDAO.java - line 43 <<<");

						locationList.clear();
						locationList.add(Location.emptyLocation);
					}
				}
			}
		} else {
			locationList.add(Location.emptyLocation);
		}

		return locationList;
	}

	@Override
	public Optional<Location> get(Integer id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Location> locationOptional = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Location WHERE locationId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					locationOptional = Optional.of(new Location(
							resultSet.getInt("locationId"),
							resultSet.getNString("locationName"),
							resultSet.getShort("capacity"),
							resultSet.getShort("currentSlots")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> LocationDAO.java - line 81 <<<");
				e.printStackTrace();
				locationOptional = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> LocationDAO.java - line 90 <<<");
						locationOptional = Optional.empty();
					}
				}
			}
		}

		return locationOptional;
	}

	public Optional<Object> getOneField(Integer id, String fieldName) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Object> optionalValue = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT " + fieldName + " FROM COVID_MANAGEMENT.Location WHERE locationId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					Object value = null;

					switch (fieldName) {
						case "locationName" -> value = resultSet.getNString("locationName");
						case "capacity" -> value = resultSet.getShort("capacity");
						case "currentSlots" -> value = resultSet.getShort("currentSlots");
					}

					optionalValue = Optional.ofNullable(value);
				}
			} catch (SQLException e) {
				System.out.println(">>> LocationDAO.java - line 125 <<<");
				e.printStackTrace();
				optionalValue = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> LocationDAO.java - line 133 <<<");
						optionalValue = Optional.empty();
					}
				}
			}
		}

		return optionalValue;
	}

	@Override
	public boolean create(Location entity) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		int rowAffected = 0;

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				StringBuilder sqlStatement = new StringBuilder();
				sqlStatement.append("INSERT INTO COVID_MANAGEMENT.Location (locationName, capacity, currentSlots) ")
						.append("VALUES (?, ?, ?);");

				preparedStatement = connection.prepareStatement(sqlStatement.toString());

				// Set values.
				preparedStatement.setNString(1, entity.getLocationName());
				preparedStatement.setShort(2, entity.getCapacity());
				preparedStatement.setShort(3, entity.getCurrentSlots());

				rowAffected = preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println(">>> LocationDAO.java - line 165 <<<");
				e.printStackTrace();
				rowAffected = 0;
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> LocationDAO.java - line 173 <<<");
						rowAffected = 0;
					}
				}
			}
		}

		return rowAffected == 1;
	}

	@Override
	public boolean update(Location entity) {
		return false;
	}

	public boolean updateExceptCurrentSlots(Location entity) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		int rowAffected = 0;

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				StringBuilder sqlStatement = new StringBuilder();
				sqlStatement.append("UPDATE COVID_MANAGEMENT.Location ")
						.append("SET locationName = ?, capacity = ? ")
						.append("WHERE locationId = ?");

				preparedStatement = connection.prepareStatement(sqlStatement.toString());

				// Set values.
				preparedStatement.setNString(1, entity.getLocationName());
				preparedStatement.setShort(2, entity.getCapacity());
				preparedStatement.setInt(3, entity.getLocationId());

				rowAffected = preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println(">>> LocationDAO.java - line 210 <<<");
				e.printStackTrace();
				rowAffected = 0;
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> LocationDAO.java - line 218 <<<");
						rowAffected = 0;
					}
				}
			}
		}

		return rowAffected == 1;
	}

	@Override
	public boolean delete(Location entity) {
		return false;
	}

	public static final byte CONNECTION_ERROR = 0;
	public static final byte NON_EXISTENCE = 1;
	public static final byte EXISTING = 2;

	// This method check whether a location id or location name is existing or not.
	public byte isExisting(String field, Object value) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		byte state = CONNECTION_ERROR;

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				ResultSet resultSet;
				if (field.equals("locationId")) {
					String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Location WHERE locationId = ?";
					preparedStatement = connection.prepareStatement(sqlStatement);

					preparedStatement.setInt(1, (int) value);
					resultSet = preparedStatement.executeQuery();
				} else {  // locationName
					String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Location WHERE BINARY locationName = ?";
					preparedStatement = connection.prepareStatement(sqlStatement);

					preparedStatement.setNString(1, (String) value);
					resultSet = preparedStatement.executeQuery();
				}

				state = (resultSet.next()) ? EXISTING : NON_EXISTENCE;
			} catch (SQLException e) {
				System.out.println(">>> LocationDAO.java - line 263 <<<");
				e.printStackTrace();
				state = CONNECTION_ERROR;
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> LocationDAO.java - line 271 <<<");
						state = CONNECTION_ERROR;
					}
				}
			}
		}

		return state;
	}
}
