package com.dao;

import com.models.Province;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProvinceDAO implements DAO<Province, Integer> {
	@Override
	public List<Province> getAll() {
		return null;
	}

	@Override
	public Optional<Province> get(Integer id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Province> provinceOptional = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Province WHERE provinceId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					provinceOptional = Optional.of(new Province(
							resultSet.getInt("provinceId"),
							resultSet.getString("provinceName")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> ProvinceDAO.java - line 41 <<<");
				e.printStackTrace();
				provinceOptional = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> ProvinceDAO.java - line 49 <<<");
						provinceOptional = Optional.empty();
					}
				}
			}
		}

		return provinceOptional;
	}

	public Optional<String> getProvinceName(Integer id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<String> provinceNameOptional = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT provinceName FROM COVID_MANAGEMENT.Province WHERE provinceId = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					provinceNameOptional = Optional.of(resultSet.getString("provinceName"));
				}
			} catch (SQLException e) {
				System.out.println(">>> ProvinceDAO.java - line 77 <<<");
				e.printStackTrace();
				provinceNameOptional = Optional.empty();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> ProvinceDAO.java - line 85 <<<");
						provinceNameOptional = Optional.empty();
					}
				}
			}
		}

		return provinceNameOptional;
	}

	@Override
	public boolean create(Province entity) {
		return false;
	}

	@Override
	public boolean update(Province entity) {
		return false;
	}

	@Override
	public boolean delete(Province entity) {
		return false;
	}
}
