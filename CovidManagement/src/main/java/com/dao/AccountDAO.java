package com.dao;

import com.models.Account;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAO implements DAO<Account, String> {
	@Override
	public List<Account> getAll() {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Account> accountList = new ArrayList<>();

		if (connection != null) {
			Statement statement = null;

			try {
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM COVID_MANAGEMENT.Account;");

				while (resultSet.next()) {
					accountList.add(new Account(
							resultSet.getString("username"),
							resultSet.getString("password"),
							resultSet.getByte("role"),
							resultSet.getByte("isActive"),
							resultSet.getInt("userId")
					));
				}
			} catch (SQLException e) {
				System.out.println(">>> AccountDAO.java - line 33 <<<");
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> AccountDAO.java - line 40 <<<");
					}
				}
			}
		}

		return accountList;
	}

	@Override
	public Optional<Account> get(String id) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Account> accountOptional = Optional.empty();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Account WHERE username = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setString(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					accountOptional = Optional.of(new Account(
							resultSet.getString("username"),
							resultSet.getString("password"),
							resultSet.getByte("role"),
							resultSet.getByte("isActive"),
							resultSet.getInt("userId")
					));
				}
			} catch (SQLException e) {
				accountOptional = Optional.of(Account.emptyAccount);
				System.out.println(">>> AccountDAO.java - line 76 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						accountOptional = Optional.of(Account.emptyAccount);
						System.out.println(">>> AccountDAO.java - line 84 <<<");
					}
				}
			}
		}

		return accountOptional;
	}

	@Override
	public boolean create(Account entity) {
		return false;
	}

	@Override
	public boolean update(Account entity) {
		return false;
	}

	@Override
	public boolean delete(Account entity) {
		return false;
	}

	public boolean updateOneField(Account entity, String fieldName) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		int rowsAffected = 0;

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "UPDATE COVID_MANAGEMENT.Account SET password = ? WHERE username = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setString(1, entity.getPassword());

				switch (fieldName) {
					case "password" -> preparedStatement.setString(1, entity.getPassword());
					case "role" -> preparedStatement.setByte(1, entity.getRole());
					case "isActive" -> preparedStatement.setByte(1, entity.getIsActive());
					case "userId" -> preparedStatement.setInt(1, entity.getUserId());
				}

				preparedStatement.setString(2, entity.getUsername());
				rowsAffected = preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println(">>> AccountDAO.java - line 91 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> AccountDAO.java - line 98 <<<");
					}
				}
			}
		}

		return rowsAffected == 1;
	}
}
