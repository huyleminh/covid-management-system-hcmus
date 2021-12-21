package com.dao;

import com.models.User;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User, Integer> {
	@Override
	public List<User> getAll() {
		return null;
	}

	@Override
	public Optional<User> get(Integer id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<User> userOptional = Optional.empty();

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
							resultSet.getInt("userInvolvedId"),
							resultSet.getNString("street"),
							resultSet.getInt("wardId"),
							resultSet.getInt("districtId"),
							resultSet.getInt("provinceId")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - line 50 <<<");
				e.printStackTrace();
				userOptional = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - line 58 <<<");
						userOptional = Optional.empty();
					}
				}
			}
		}

		return userOptional;
	}

	public Optional<Object> getOneField(Integer id, String fieldName) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Object> optionalValue = Optional.empty();

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
						case "userInvolvedId" 	-> value = resultSet.getInt("userInvolvedId");
						case "street" 			-> value = resultSet.getNString("street");
						case "wardId" 			-> value = resultSet.getInt("wardId");
						case "districtId" 		-> value = resultSet.getInt("districtId");
						case "provinceId" 		-> value = resultSet.getInt("provinceId");
					}

					optionalValue = Optional.ofNullable(value);
				}
			} catch (SQLException e) {
				System.out.println(">>> UserDAO.java - line 101 <<<");
				e.printStackTrace();
				optionalValue = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> UserDAO.java - line 109 <<<");
						optionalValue = Optional.empty();
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

	@Override
	public boolean update(User entity) {
		return false;
	}

	@Override
	public boolean delete(User entity) {
		return false;
	}
}
