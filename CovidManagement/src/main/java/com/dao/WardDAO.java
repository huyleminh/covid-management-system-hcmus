package com.dao;

import com.models.Ward;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class WardDAO implements DAO<Ward, Integer> {
	@Override
	public List<Ward> getAll() {
		return null;
	}

	@Override
	public Optional<Ward> get(Integer id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Ward> wardOptional = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Ward WHERE wardId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					wardOptional = Optional.of(new Ward(
							resultSet.getInt("wardId"),
							resultSet.getString("wardName"),
							resultSet.getInt("districtId")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> WardDAO.java - line 42 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> WardDAO.java - line 49 <<<");
					}
				}
			}
		}

		return wardOptional;
	}

	public Optional<Object> getOneField(Integer id, String fieldName) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Object> optionalValue = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT " + fieldName + " FROM COVID_MANAGEMENT.Ward WHERE wardId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					switch (fieldName) {
						case "wardName" -> optionalValue = Optional.of(resultSet.getString("wardName"));
						case "districtId" -> optionalValue = Optional.of(resultSet.getInt("districtId"));
					}
				}
			} catch (SQLException e) {
				System.out.println(">>> WardDAO.java - line 79 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> WardDAO.java - line 86 <<<");
					}
				}
			}
		}

		return optionalValue;
	}

	@Override
	public boolean create(Ward entity) {
		return false;
	}

	@Override
	public boolean update(Ward entity) {
		return false;
	}

	@Override
	public boolean delete(Ward entity) {
		return false;
	}
}
