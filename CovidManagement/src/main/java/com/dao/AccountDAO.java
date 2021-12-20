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
				System.out.println(">>> AccountDAO.java - line 34 <<<");
				e.printStackTrace();

				accountList.clear();
				accountList.add(Account.emptyAccount);
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						System.out.println(">>> AccountDAO.java - line 44 <<<");

						accountList.clear();
						accountList.add(Account.emptyAccount);
					}
				}
			}
		} else {
			accountList.add(Account.emptyAccount);
		}

		return accountList;
	}

	public List<Account> getAllByRole(byte role) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		ArrayList<Account> accountList = new ArrayList<>();

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				final String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Account WHERE role = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setByte(1, role);
				ResultSet resultSet = preparedStatement.executeQuery();

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
				System.out.println(">>> AccountDAO.java - line 82 <<<");
				e.printStackTrace();

				accountList.clear();
				accountList.add(Account.emptyAccount);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> AccountDAO.java - line 92 <<<");

						accountList.clear();
						accountList.add(Account.emptyAccount);
					}
				}
			}
		} else {
			accountList.add(Account.emptyAccount);
		}

		return accountList;
	}

	@Override
	public Optional<Account> get(String username) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		Optional<Account> accountOptional = Optional.of(Account.emptyAccount);

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Account WHERE username = ?;";
				preparedStatement = connection.prepareStatement(sqlStatement);

				preparedStatement.setString(1, username);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					accountOptional = Optional.of(new Account(
							resultSet.getString("username"),
							resultSet.getString("password"),
							resultSet.getByte("role"),
							resultSet.getByte("isActive"),
							resultSet.getInt("userId")
					));
				} else {
					accountOptional = Optional.empty();
				}
			} catch (SQLException e) {
				System.out.println(">>> AccountDAO.java - line 133 <<<");
				e.printStackTrace();
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> AccountDAO.java - line 140 <<<");
						accountOptional = Optional.of(Account.emptyAccount);
					}
				}
			}
		}

		return accountOptional;
	}

	@Override
	public boolean create(Account entity) {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		int rowAffected = 0;

		if (connection != null) {
			PreparedStatement preparedStatement = null;

			try {
				String sqlStatement = "INSERT INTO COVID_MANAGEMENT.Account (username, `password`, `role`, isActive, userId) " +
						"VALUES (?, ?, ?, ?, ?)";

				preparedStatement = connection.prepareStatement(sqlStatement);

				// Set values for non-null fields.
				preparedStatement.setString(1, entity.getUsername());
				preparedStatement.setByte(3, entity.getRole());
				preparedStatement.setByte(4, entity.getIsActive());

				// Set values for nullable fields.
				if (entity.getPassword() == null)
					preparedStatement.setNull(2, Types.VARCHAR);
				else
					preparedStatement.setString(2, entity.getPassword());

				if (entity.getUserId() == -1)
					preparedStatement.setNull(5, Types.INTEGER);
				else
					preparedStatement.setInt(5, entity.getUserId());

				rowAffected = preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println(">>> AccountDAO.java - line 182 <<<");
				e.printStackTrace();
				rowAffected = 0;
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> AccountDAO.java - line 190 <<<");
						rowAffected = 0;
					}
				}
			}
		}

		return rowAffected == 1;
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
				String sqlStatement = "UPDATE COVID_MANAGEMENT.Account SET " + fieldName + " = ? WHERE username = ?;";
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
				System.out.println(">>> AccountDAO.java - line 233 <<<");
				e.printStackTrace();
				rowsAffected = 0;
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						System.out.println(">>> AccountDAO.java - line 241 <<<");
						rowsAffected = 0;
					}
				}
			}
		}

		return rowsAffected == 1;
	}
}
