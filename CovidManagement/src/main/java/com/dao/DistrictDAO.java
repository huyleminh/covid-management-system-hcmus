package com.dao;

import com.models.District;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DistrictDAO implements DAO<District, Integer> {
	@Override
	public List<District> getAll() {
		return null;
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
							resultSet.getString("districtName"),
							resultSet.getInt("provinceId")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> DistrictDAO.java - line 42 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> DistrictDAO.java - line 49 <<<");
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
						case "districtName" -> optionalValue = Optional.of(resultSet.getString("districtName"));
						case "provinceId" -> optionalValue = Optional.of(resultSet.getInt("provinceId"));
					}
				}
			} catch (SQLException e) {
				System.out.println(">>> DistrictDAO.java - line 79 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> DistrictDAO.java - line 86 <<<");
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
