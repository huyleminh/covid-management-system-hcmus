package covid_management.dao;

import covid_management.models.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.db.DAO;
import shared.db.SingletonDBConnection;
import shared.exceptions.DBConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class LocationDAO implements DAO<Location, Integer> {
	private static final Logger logger = LogManager.getLogger(LocationDAO.class);

	@Override
	public ArrayList<Location> getAll() throws DBConnectionException  {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Location> locationList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM COVID_MANAGEMENT.Location;");

			while (resultSet.next()) {
				locationList.add(new Location(
						resultSet.getInt("locationId"),
						resultSet.getNString("locationName"),
						resultSet.getShort("capacity"),
						resultSet.getShort("currentSlots")
				));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return locationList;
	}

	public ArrayList<Location> getAllAvailable(boolean isIncludedNoneLocation) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Location> locationList = new ArrayList<>();
		Statement statement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Location WHERE currentSlots < capacity";
			if (isIncludedNoneLocation)
				sqlStatement += " OR locationName = 'Không có'";

			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);

			while (resultSet.next()) {
				locationList.add(new Location(
						resultSet.getInt("locationId"),
						resultSet.getNString("locationName"),
						resultSet.getShort("capacity"),
						resultSet.getShort("currentSlots")
				));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return locationList;
	}

	@Override
	public Optional<Location> get(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<Location> locationOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Location WHERE locationId = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				locationOptional = Optional.of(new Location(
						resultSet.getInt("locationId"),
						resultSet.getNString("locationName"),
						resultSet.getShort("capacity"),
						resultSet.getShort("currentSlots")
				));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return locationOptional;
	}

	public Optional<Object> getOneField(Integer id, String fieldName) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<Object> optionalValue = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT " + fieldName + " FROM COVID_MANAGEMENT.Location WHERE locationId = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Object value = null;

				switch (fieldName) {
					case "locationName" -> value = resultSet.getNString("locationName");
					case "capacity" -> value = resultSet.getShort("capacity");
					case "currentSlots" -> value = resultSet.getShort("currentSlots");
				}

				optionalValue = Optional.ofNullable(value);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return optionalValue;
	}

	@Override
	public void create(Location entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "INSERT INTO COVID_MANAGEMENT.Location" +
					" (locationName, capacity, currentSlots) VALUES (?, ?, ?);";

			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values.
			preparedStatement.setNString(1, entity.getLocationName());
			preparedStatement.setShort(2, entity.getCapacity());
			preparedStatement.setShort(3, entity.getCurrentSlots());

			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());

			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

	@Override
	public void update(Location entity) throws DBConnectionException {

	}

	public void updateExceptCurrentSlots(Location entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "UPDATE COVID_MANAGEMENT.Location" +
					" SET locationName = ?, capacity = ? WHERE locationId = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values.
			preparedStatement.setNString(1, entity.getLocationName());
			preparedStatement.setShort(2, entity.getCapacity());
			preparedStatement.setInt(3, entity.getLocationId());

			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());

			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

	@Override
	public void delete(Location entity) throws DBConnectionException {

	}

	// This method check whether a location id or location name is existing or not.
	public boolean isExisting(String field, Object value) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		if (connection == null)
			throw DBConnectionException.INSTANCE;

		boolean isExisting;
		PreparedStatement preparedStatement = null;

		try {
			ResultSet resultSet;
			if (field.equals("locationId")) {
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Location WHERE locationId = ?";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setInt(1, (int) value);
			} else {  // locationName
				String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Location WHERE BINARY locationName = ?";

				preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setNString(1, (String) value);
			}

			resultSet = preparedStatement.executeQuery();
			isExisting = resultSet.next();  // next() return true if this value is existing, otherwise return false.
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return isExisting;
	}

	public boolean hasEnoughOneSlot(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		boolean isEnough;
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Location " +
					"WHERE locationId = ? AND capacity - currentSlots > 0";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			isEnough = resultSet.next();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			throw DBConnectionException.INSTANCE;
		} finally {

			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			}
		}

		return isEnough;
	}
}
