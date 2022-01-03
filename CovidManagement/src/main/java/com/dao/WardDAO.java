package com.dao;

import com.models.Ward;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WardDAO implements DAO<Ward, Integer> {
	@Override
	public List<Ward> getAll() {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Ward> wardList = new ArrayList<>();

		if (connection != null) {
			Statement statement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Ward;";
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);

				while (resultSet.next()) {
					wardList.add(new Ward(
							resultSet.getInt("wardId"),
							resultSet.getNString("wardName"),
							resultSet.getInt("districtId")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> WardDAO.java - getAll() method - catch block <<<");
				e.printStackTrace();
				wardList.clear();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> WardDAO.java - getAll() method - finally block <<<");
						wardList.clear();
					}
				}
			}
		}

		return wardList;
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
							resultSet.getNString("wardName"),
							resultSet.getInt("districtId")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> WardDAO.java - line 42 <<<");
				e.printStackTrace();
				wardOptional = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> WardDAO.java - line 50 <<<");
						wardOptional = Optional.empty();
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
						case "wardName" -> optionalValue = Optional.of(resultSet.getNString("wardName"));
						case "districtId" -> optionalValue = Optional.of(resultSet.getInt("districtId"));
					}
				}
			} catch (SQLException e) {
				System.out.println(">>> WardDAO.java - line 81 <<<");
				e.printStackTrace();
				optionalValue = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> WardDAO.java - line 89 <<<");
						optionalValue = Optional.empty();
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
