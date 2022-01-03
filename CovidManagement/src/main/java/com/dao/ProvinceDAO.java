package com.dao;

import com.models.Province;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProvinceDAO implements DAO<Province, Integer> {
	@Override
	public List<Province> getAll() {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Province> provinceList = new ArrayList<>();

		if (connection != null) {
			Statement statement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Province;";
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlStatement);

				while (resultSet.next()) {
					provinceList.add(new Province(
							resultSet.getInt("provinceId"),
							resultSet.getNString("provinceName")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> ProvinceDAO.java - getAll() method - catch block <<<");
				e.printStackTrace();
				provinceList.clear();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> ProvinceDAO.java - getAll() method - finally block <<<");
						provinceList.clear();
					}
				}
			}
		}

		return provinceList;
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
							resultSet.getNString("provinceName")
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
					provinceNameOptional = Optional.of(resultSet.getNString("provinceName"));
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
