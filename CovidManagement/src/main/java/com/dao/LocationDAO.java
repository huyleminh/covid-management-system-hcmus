package com.dao;

import com.models.Location;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LocationDAO implements DAO<Location, Integer> {
	@Override
	public List<Location> getAll() {
		return null;
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
							resultSet.getString("locationName"),
							resultSet.getShort("capacity"),
							resultSet.getShort("availableSlots")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> LocationDAO.java - line 43 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> LocationDAO.java - line 50 <<<");
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
						case "locationName" -> value = resultSet.getString("locationName");
						case "capacity" -> value = resultSet.getShort("capacity");
						case "availableSlots" -> value = resultSet.getShort("availableSlots");
					}

					optionalValue = Optional.ofNullable(value);
				}
			} catch (SQLException e) {
				System.out.println(">>> LocationDAO.java - line 85 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> LocationDAO.java - line 92 <<<");
					}
				}
			}
		}

		return optionalValue;
	}

	@Override
	public boolean create(Location entity) {
		return false;
	}

	@Override
	public boolean update(Location entity) {
		return false;
	}

	@Override
	public boolean delete(Location entity) {
		return false;
	}
}
