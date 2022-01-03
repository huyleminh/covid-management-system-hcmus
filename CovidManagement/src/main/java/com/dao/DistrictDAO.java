package com.dao;

import com.models.District;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DistrictDAO implements DAO<District, Integer> {
	@Override
	public List<District> getAll() {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<District> districtList = new ArrayList<>();

		if (connection != null) {
			Statement statement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.District;";
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);

				while (resultSet.next()) {
					districtList.add(new District(
							resultSet.getInt("districtId"),
							resultSet.getNString("districtName"),
							resultSet.getInt("provinceId")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> DistrictDAO.java - getAll() method - catch block <<<");
				e.printStackTrace();
				districtList.clear();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> DistrictDAO.java - getAll() method - finally block <<<");
						districtList.clear();
					}
				}
			}
		}

		return districtList;
	}

	@Override
	public Optional<District> get(Integer id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<District> districtOptional = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.District WHERE districtId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					districtOptional = Optional.of(new District(
							resultSet.getInt("districtId"),
							resultSet.getNString("districtName"),
							resultSet.getInt("provinceId")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> DistrictDAO.java - line 42 <<<");
				e.printStackTrace();
				districtOptional = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> DistrictDAO.java - line 50 <<<");
						districtOptional = Optional.empty();
					}
				}
			}
		}

		return districtOptional;
	}

	public Optional<Object> getOneField(Integer id, String fieldName) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Object> optionalValue = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT " + fieldName + " FROM COVID_MANAGEMENT.District WHERE districtId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					switch (fieldName) {
						case "districtName" -> optionalValue = Optional.of(resultSet.getNString("districtName"));
						case "provinceId" -> optionalValue = Optional.of(resultSet.getInt("provinceId"));
					}
				}
			} catch (SQLException e) {
				System.out.println(">>> DistrictDAO.java - line 81 <<<");
				e.printStackTrace();
				optionalValue = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> DistrictDAO.java - line 89 <<<");
						optionalValue = Optional.empty();
					}
				}
			}
		}

		return optionalValue;
	}

	@Override
	public boolean create(District entity) {
		return false;
	}

	@Override
	public boolean update(District entity) {
		return false;
	}

	@Override
	public boolean delete(District entity) {
		return false;
	}
}
