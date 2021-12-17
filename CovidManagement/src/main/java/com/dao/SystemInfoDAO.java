package com.dao;

import com.models.SystemInfo;
import com.utilities.SingletonDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SystemInfoDAO implements DAO<SystemInfo, Integer> {

	@Override
	public List<SystemInfo> getAll() {
		return null;
	}

	@Override
	public Optional<SystemInfo> get(Integer id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<SystemInfo> systemInfoOptional = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.SystemInfo WHERE id = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					systemInfoOptional = Optional.of(new SystemInfo(
							resultSet.getInt("id"),
							resultSet.getByte("firstLoggedIn"),
							resultSet.getString("bankAccountNumber"),
							resultSet.getInt("balance")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> SystemInfoDAO.java - line 44 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> SystemInfoDAO.java - line 51 <<<");
					}
				}
			}
		}

		return systemInfoOptional;
	}

	@Override
	public boolean create(SystemInfo entity) {
		return false;
	}

	@Override
	public boolean update(SystemInfo entity) {
		return false;
	}

	@Override
	public boolean delete(SystemInfo entity) {
		return false;
	}

	public boolean updateOneField(SystemInfo entity, String fieldName) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		int rowsAffected = 0;

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "UPDATE COVID_MANAGEMENT.SystemInfo SET " + fieldName + " = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				switch (fieldName) {
					case "firstLoggedIn" -> preparedStatement.setByte(1, entity.getFirstLoggedIn());
					case "bankAccountNumber" -> preparedStatement.setString(1, entity.getBankAccountNumber());
					case "balance" -> preparedStatement.setInt(1, entity.getBalance());
				}

				rowsAffected = preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println(">>> SystemInfoDAO.java - line 59 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> SystemInfoDAO.java - line 66 <<<");
					}
				}
			}
		}

		return rowsAffected == 1;
	}
}
