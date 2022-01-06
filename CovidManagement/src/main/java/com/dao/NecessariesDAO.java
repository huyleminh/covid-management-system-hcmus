package com.dao;

import com.models.Necessaries;
import com.models.NecessariesHistory;
import com.utilities.Pair;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.*;

public class NecessariesDAO implements DAO<Necessaries, Integer> {
    public static final byte CONNECTION_ERROR = 0;
    public static final byte NON_EXISTENCE = 1;
    public static final byte EXISTING = 2;

    @Override
    public List<Necessaries> getAll() {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        ArrayList<Necessaries> necessariesList = new ArrayList<>();

        if (connection != null) {
            Statement statement = null;

            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM COVID_MANAGEMENT.Necessaries");

                while (resultSet.next()) {
                    necessariesList.add(
                            new Necessaries(
                                    resultSet.getInt("necessariesId"),
                                    resultSet.getNString("necessariesName"),
                                    resultSet.getByte("limit"),
                                    resultSet.getTimestamp("startDate"),
                                    resultSet.getTimestamp("expiredDate"),
                                    resultSet.getInt("price")
                            )
                    );
                }
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - getAll() - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> NecessariesDAO.java - getAll() - finally block <<<");

                        necessariesList.clear();
                        necessariesList.add(Necessaries.emptyInstance);
                    }
                }
            }
        } else {
            necessariesList.add(Necessaries.emptyInstance);
        }

        return necessariesList;
    }

    public List<Necessaries> getAllCanPurchaseByUserIdAndNecessariesName(Integer userId, String necessariesName) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        ArrayList<Necessaries> necessariesList = new ArrayList<>();

        if (connection != null) {
            PreparedStatement preparedStatement = null;

            try {
                String sqlStatement = "SELECT n1.*, pnc.purchasedCount" +
                        " FROM COVID_MANAGEMENT.Necessaries n1 LEFT JOIN" +
                        " (SELECT n.necessariesId , count(*) AS purchasedCount" +
                        " FROM COVID_MANAGEMENT.Necessaries n LEFT JOIN COVID_MANAGEMENT.OrderDetail od ON od.necessariesId = n.necessariesId" +
                        " JOIN COVID_MANAGEMENT.Order o ON od.orderId = o.orderId" +
                        " WHERE o.userId = ? AND n.startDate <= od.purchasedAt AND od.purchasedAt <= n.expiredDate" +
                        " GROUP BY n.necessariesId) AS pnc ON n1.necessariesId = pnc.necessariesId" +
                        " WHERE current_timestamp <= n1.expiredDate";

                if (necessariesName != null && !necessariesName.isBlank()) {
                    sqlStatement += " AND n1.necessariesName LIKE N'%" + necessariesName + "%'";
                }

                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    byte limit = (byte) (resultSet.getByte("limit") - resultSet.getByte("purchasedCount"));
                    necessariesList.add(
                            new Necessaries(
                                    resultSet.getInt("necessariesId"),
                                    resultSet.getNString("necessariesName"),
                                    limit,
                                    resultSet.getTimestamp("startDate"),
                                    resultSet.getTimestamp("expiredDate"),
                                    resultSet.getInt("price")
                            )
                    );
                }
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - getAllCanPurchaseByUserId(Integer) - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> NecessariesDAO.java - getAllCanPurchaseByUserId(Integer) - finally block <<<");

                        necessariesList.clear();
                        necessariesList.add(Necessaries.emptyInstance);
                    }
                }
            }
        } else {
            necessariesList.add(Necessaries.emptyInstance);
        }

        return necessariesList;
    }

    @Override
    public Optional<Necessaries> get(Integer id) {
        return Optional.empty();
    }

    @Override
    public boolean create(Necessaries entity) {
        return false;
    }

    public boolean create(Necessaries entity, String managerUsername, String description) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        boolean isCreated = false;

        if (connection != null) {
            PreparedStatement preparedStatementInsertNecessaries = null;
            PreparedStatement preparedStatementInsertNecessariesHistory = null;

            try {
                // create sql insert statement of existing necessaries
                String insertNecessariesStatement = "INSERT COVID_MANAGEMENT.Necessaries" +
                        " (necessariesName, `limit`, startDate, expiredDate, price) VALUES (?, ?, ?, ?, ?)";

                // create sql insert statement of new necessaries histories.
                String insertNecessariesHistoryStatement = "INSERT COVID_MANAGEMENT.NecessariesHistory" +
                        " (managerUsername, date, description, operationType) VALUES (?, ?, ?, ?)";

                // create PreparedStatement
                preparedStatementInsertNecessaries = connection.prepareStatement(insertNecessariesStatement);
                preparedStatementInsertNecessariesHistory = connection.prepareStatement(insertNecessariesHistoryStatement);

                // Set values of preparedStatementInsertNecessaries
                preparedStatementInsertNecessaries.setNString(1, entity.getNecessariesName());
                preparedStatementInsertNecessaries.setByte(2, entity.getLimit());
                preparedStatementInsertNecessaries.setTimestamp(3, entity.getStartDate());
                preparedStatementInsertNecessaries.setTimestamp(4, entity.getExpiredDate());
                preparedStatementInsertNecessaries.setInt(5, entity.getPrice());

                // Set values of preparedStatementInsertNecessariesHistory
                Timestamp current = new Timestamp(System.currentTimeMillis());
                preparedStatementInsertNecessariesHistory.setString(1, managerUsername);
                preparedStatementInsertNecessariesHistory.setTimestamp(2, current);
                preparedStatementInsertNecessariesHistory.setNString(3, description);
                preparedStatementInsertNecessariesHistory.setByte(4, NecessariesHistory.ADD_NEW_NECESSARIES);

                // execute query
                connection.setAutoCommit(false);
                preparedStatementInsertNecessaries.executeUpdate();
                preparedStatementInsertNecessariesHistory.executeUpdate();
                connection.commit();
                isCreated = true;
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - create(Necessaries, String, String) - catch block <<<");
                e.printStackTrace();

                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                isCreated = false;
            } finally {
                try {
                    connection.setAutoCommit(true);
                    if (preparedStatementInsertNecessaries != null)
                        preparedStatementInsertNecessaries.close();
                    if (preparedStatementInsertNecessariesHistory != null)
                        preparedStatementInsertNecessariesHistory.close();
                } catch (SQLException e) {
                    System.out.println(">>> NecessariesDAO.java - create(Necessaries, String, String) - finally block <<<");
                    e.printStackTrace();
                    isCreated = false;
                }
            }
        }

        return isCreated;
    }

    @Override
    public boolean update(Necessaries entity) {
        return false;
    }

    public boolean update(
            Integer id,
            ArrayList<String> fields,
            ArrayList<Object> values,
            String managerUsername,
            ArrayList<String> descriptionList,
            ArrayList<Byte> operationTypes
    ) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        boolean isUpdated = false;

        if (connection != null) {
            PreparedStatement preparedStatementUpdateNecessaries = null;
            PreparedStatement preparedStatementInsertNecessariesHistory = null;

            try {
                int count = fields.size();

                // create sql update statement of existing necessaries
                String updateNecessariesStatement = "UPDATE COVID_MANAGEMENT.Necessaries SET " + fields.get(0) + " = ?";
                for (int i = 1; i < count; i++)
                    updateNecessariesStatement += ", " + fields.get(i) + " = ?";
                updateNecessariesStatement += " WHERE necessariesId = ?";

                // create sql insert statement of new necessaries histories.
                String insertNecessariesHistoryStatement = "INSERT COVID_MANAGEMENT.NecessariesHistory" +
                        " (managerUsername, date, description, operationType) VALUES (?, ?, ?, ?)";

                // create PreparedStatement
                preparedStatementUpdateNecessaries = connection.prepareStatement(updateNecessariesStatement);
                preparedStatementInsertNecessariesHistory = connection.prepareStatement(insertNecessariesHistoryStatement);

                // Set values
                preparedStatementUpdateNecessaries.setInt(count + 1, id);
                for (int i = 0; i < count; i++) {
                    switch (fields.get(i)) {
                        case "necessariesName" -> preparedStatementUpdateNecessaries.setNString(i + 1, String.valueOf(values.get(i)));
                        case "limit" -> preparedStatementUpdateNecessaries.setByte(i + 1, (Byte) values.get(i));
                        case "price" -> preparedStatementUpdateNecessaries.setInt(i + 1, (Integer) values.get(i));
                        case "startDate", "expiredDate" -> preparedStatementUpdateNecessaries.setTimestamp(i + 1, (Timestamp) values.get(i));
                    }
                }

                Timestamp current = new Timestamp(System.currentTimeMillis());
                for (int i = 0; i < descriptionList.size(); i++) {
                    preparedStatementInsertNecessariesHistory.setString(1, managerUsername);
                    preparedStatementInsertNecessariesHistory.setTimestamp(2, current);
                    preparedStatementInsertNecessariesHistory.setNString(3, descriptionList.get(i));
                    preparedStatementInsertNecessariesHistory.setByte(4, operationTypes.get(i));
                    preparedStatementInsertNecessariesHistory.addBatch();
                }

                // execute query
                connection.setAutoCommit(false);
                preparedStatementUpdateNecessaries.executeUpdate();
                preparedStatementInsertNecessariesHistory.executeBatch();
                connection.commit();
                isUpdated = true;
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - update(Integer, ArrayList<String>, ArrayList<Object>, String, ArrayList<String>, ArrayList<Byte>) - catch block <<<");
                e.printStackTrace();

                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                isUpdated = false;
            } finally {
                try {
                    connection.setAutoCommit(true);
                    if (preparedStatementUpdateNecessaries != null)
                        preparedStatementUpdateNecessaries.close();
                    if (preparedStatementInsertNecessariesHistory != null)
                        preparedStatementInsertNecessariesHistory.close();
                } catch (SQLException e) {
                    System.out.println(">>> NecessariesDAO.java - update(Integer, ArrayList<String>, ArrayList<Object>, String, ArrayList<String>, ArrayList<Byte>) - finally block <<<");
                    e.printStackTrace();
                    isUpdated = false;
                }
            }
        }

        return isUpdated;
    }

    @Override
    public boolean delete(Necessaries entity) {
        return false;
    }

    public boolean delete(Integer id, String managerUsername, String description) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        boolean isDeleted = false;

        if (connection != null) {
            PreparedStatement preparedStatementSetNullNecessariesId = null;
            PreparedStatement preparedStatementDeleteNecessaries = null;
            PreparedStatement preparedStatementInsertNecessariesHistory = null;

            try {
                String setNullNecessariesIdStatement = "UPDATE COVID_MANAGEMENT.OrderDetail SET necessariesId = ? " +
                        "WHERE necessariesId = ?";
                String deleteNecessariesStatement = "DELETE FROM COVID_MANAGEMENT.Necessaries WHERE necessariesId = ?";
                String insertNecessariesHistoryStatement = "INSERT COVID_MANAGEMENT.NecessariesHistory" +
                        " (managerUsername, date, description, operationType) VALUES (?, ?, ?, ?)";

                preparedStatementSetNullNecessariesId = connection.prepareStatement(setNullNecessariesIdStatement);
                preparedStatementDeleteNecessaries = connection.prepareStatement(deleteNecessariesStatement);
                preparedStatementInsertNecessariesHistory = connection.prepareStatement(insertNecessariesHistoryStatement);

                preparedStatementSetNullNecessariesId.setNull(1, Types.INTEGER);
                preparedStatementSetNullNecessariesId.setInt(2, id);
                preparedStatementDeleteNecessaries.setInt(1, id);

                Timestamp current = new Timestamp(System.currentTimeMillis());
                preparedStatementInsertNecessariesHistory.setString(1, managerUsername);
                preparedStatementInsertNecessariesHistory.setTimestamp(2, current);
                preparedStatementInsertNecessariesHistory.setNString(3, description);
                preparedStatementInsertNecessariesHistory.setByte(4, NecessariesHistory.ADD_NEW_NECESSARIES);

                connection.setAutoCommit(false);
                preparedStatementSetNullNecessariesId.executeUpdate();
                preparedStatementDeleteNecessaries.executeUpdate();
                preparedStatementInsertNecessariesHistory.executeUpdate();
                connection.commit();
                isDeleted = true;
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - delete(Integer, String, String) - catch block <<<");
                e.printStackTrace();

                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                isDeleted = false;
            } finally {
                try {
                    connection.setAutoCommit(true);
                    if (preparedStatementSetNullNecessariesId != null)
                        preparedStatementSetNullNecessariesId.close();
                    if (preparedStatementDeleteNecessaries != null)
                        preparedStatementDeleteNecessaries.close();
                    if (preparedStatementInsertNecessariesHistory != null)
                        preparedStatementInsertNecessariesHistory.close();
                } catch (SQLException e) {
                    System.out.println(">>> NecessariesDAO.java - delete(Integer, String, String) - finally block <<<");
                    e.printStackTrace();
                    isDeleted = false;
                }
            }
        }

        return isDeleted;
    }

    public List<Necessaries> getAllCanPurchaseByUserIdAndSortBy(String field, String orderType, Integer userId) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        ArrayList<Necessaries> necessariesList = new ArrayList<>();

        if (connection != null) {
            PreparedStatement preparedStatement = null;

            try {
                String sqlStatement = "SELECT n1.*, pnc.purchasedCount" +
                        " FROM COVID_MANAGEMENT.Necessaries n1 LEFT JOIN" +
                        " (SELECT n.necessariesId , count(*) AS purchasedCount" +
                        " FROM COVID_MANAGEMENT.Necessaries n LEFT JOIN COVID_MANAGEMENT.OrderDetail od ON od.necessariesId = n.necessariesId" +
                        " JOIN COVID_MANAGEMENT.Order o ON od.orderId = o.orderId" +
                        " WHERE o.userId = ? AND n.startDate <= od.purchasedAt AND od.purchasedAt <= n.expiredDate" +
                        " GROUP BY n.necessariesId) AS pnc ON n1.necessariesId = pnc.necessariesId" +
                        " WHERE current_timestamp <= n1.expiredDate" +
                        " ORDER BY n1." + field + " " + orderType;

                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    byte limit = (byte) (resultSet.getByte("limit") - resultSet.getByte("purchasedCount"));
                    necessariesList.add(
                            new Necessaries(
                                    resultSet.getInt("necessariesId"),
                                    resultSet.getNString("necessariesName"),
                                    limit,
                                    resultSet.getTimestamp("startDate"),
                                    resultSet.getTimestamp("expiredDate"),
                                    resultSet.getInt("price")
                            )
                    );
                }
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - getAllCanPurchaseByUserIdAndSortBy(String, String, Integer) - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> NecessariesDAO.java - getAllCanPurchaseByUserIdAndSortBy(String, String, Integer) - finally block <<<");

                        necessariesList.clear();
                        necessariesList.add(Necessaries.emptyInstance);
                    }
                }
            }
        } else {
            necessariesList.add(Necessaries.emptyInstance);
        }

        return necessariesList;
    }

    public List<Necessaries> getAllCanPurchaseByUserIdAndFilterBy(
            ArrayList<String> fields,
            ArrayList<Pair<Object, Object>> values,
            Integer userId
    ) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        ArrayList<Necessaries> necessariesList = new ArrayList<>();

        if (connection != null) {
            PreparedStatement preparedStatement = null;

            try {
                String sqlStatement = "SELECT n1.*, pnc.purchasedCount" +
                        " FROM COVID_MANAGEMENT.Necessaries n1 LEFT JOIN" +
                        " (SELECT n.necessariesId , count(*) AS purchasedCount" +
                        " FROM COVID_MANAGEMENT.Necessaries n LEFT JOIN COVID_MANAGEMENT.OrderDetail od ON od.necessariesId = n.necessariesId" +
                        " JOIN COVID_MANAGEMENT.Order o ON od.orderId = o.orderId" +
                        " WHERE o.userId = ? AND n.startDate <= od.purchasedAt AND od.purchasedAt <= n.expiredDate" +
                        " GROUP BY n.necessariesId) AS pnc ON n1.necessariesId = pnc.necessariesId" +
                        " WHERE current_timestamp <= n1.expiredDate";

                if (!fields.isEmpty()) {
                    StringBuilder extraWhereStatement = new StringBuilder();

                    for (String field : fields) {
                        if (field.equals("date")) {
                            extraWhereStatement.append(" AND ((? <= n.expiredDate AND n.startDate <= ?) OR (n.expiredDate < ? AND ? <= n.expiredDate))");
                        } else {
                            extraWhereStatement.append(" AND ? <= n1." + field + " AND n1." + field + " <= ?");
                        }
                    }

                    sqlStatement += extraWhereStatement.toString();
                }

                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setInt(1, userId);

                if (!fields.isEmpty()) {
                    int parameterIndex = 2;

                    for (int i = 0; i < fields.size(); i++) {
                        switch (fields.get(i)) {
                            case "limit" -> {
                                preparedStatement.setByte(parameterIndex, (byte) values.get(i).getLeftValue());
                                parameterIndex += 1;
                                preparedStatement.setByte(parameterIndex, (byte) values.get(i).getRightValue());
                                parameterIndex += 1;
                            }
                            case "price" -> {
                                preparedStatement.setInt(parameterIndex, (int) values.get(i).getLeftValue());
                                parameterIndex += 1;
                                preparedStatement.setInt(parameterIndex, (int) values.get(i).getRightValue());
                                parameterIndex += 1;
                            }
                            case "date" -> {
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getRightValue());
                                parameterIndex += 1;
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getRightValue());
                                parameterIndex += 1;
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getRightValue());
                                parameterIndex += 1;
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getLeftValue());
                                parameterIndex += 1;
                            }
                        }
                    }
                }

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    byte limit = (byte) (resultSet.getByte("limit") - resultSet.getByte("purchasedCount"));
                    necessariesList.add(
                            new Necessaries(
                                    resultSet.getInt("necessariesId"),
                                    resultSet.getNString("necessariesName"),
                                    limit,
                                    resultSet.getTimestamp("startDate"),
                                    resultSet.getTimestamp("expiredDate"),
                                    resultSet.getInt("price")
                            )
                    );
                }
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - getAllCanPurchaseByUserIdAndFilterBy(ArrayList, ArrayList, Integer) - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> NecessariesDAO.java - getAllCanPurchaseByUserIdAndFilterBy(ArrayList, ArrayList, Integer) - finally block <<<");

                        necessariesList.clear();
                        necessariesList.add(Necessaries.emptyInstance);
                    }
                }
            }
        } else {
            necessariesList.add(Necessaries.emptyInstance);
        }

        return necessariesList;
    }

    public List<Necessaries> searchByNecessariesName(String necessariesName) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        ArrayList<Necessaries> necessariesList = new ArrayList<>();

        if (connection != null) {
            Statement statement = null;

            try {
                String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Necessaries" +
                        " WHERE necessariesName LIKE '%" + necessariesName + "%'";

                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlStatement);

                while (resultSet.next()) {
                    necessariesList.add(
                            new Necessaries(
                                    resultSet.getInt("necessariesId"),
                                    resultSet.getNString("necessariesName"),
                                    resultSet.getByte("limit"),
                                    resultSet.getTimestamp("startDate"),
                                    resultSet.getTimestamp("expiredDate"),
                                    resultSet.getInt("price")
                            )
                    );
                }
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - searchByNecessariesName(String) - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> NecessariesDAO.java - searchByNecessariesName(String) - finally block <<<");
                        e.printStackTrace();
                        necessariesList.clear();
                        necessariesList.add(Necessaries.emptyInstance);
                    }
                }
            }
        } else {
            necessariesList.add(Necessaries.emptyInstance);
        }

        return necessariesList;
    }

    public List<Necessaries> sortBy(String field, boolean isAscending) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        ArrayList<Necessaries> necessariesList = new ArrayList<>();

        if (connection != null) {
            Statement statement = null;

            try {
                String orderType = isAscending ? "ASC" : "DESC";
                String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Necessaries n ORDER BY n." + field + " " + orderType;

                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlStatement);

                while (resultSet.next()) {
                    necessariesList.add(
                            new Necessaries(
                                    resultSet.getInt("necessariesId"),
                                    resultSet.getNString("necessariesName"),
                                    resultSet.getByte("limit"),
                                    resultSet.getTimestamp("startDate"),
                                    resultSet.getTimestamp("expiredDate"),
                                    resultSet.getInt("price")
                            )
                    );
                }
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - sort(String, boolean) method - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> NecessariesDAO.java - sort(String, boolean) method - finally block <<<");

                        necessariesList.clear();
                        necessariesList.add(Necessaries.emptyInstance);
                    }
                }
            }
        } else {
            necessariesList.add(Necessaries.emptyInstance);
        }

        return necessariesList;
    }

    public List<Necessaries> filterBy(ArrayList<String> fields, ArrayList<Pair<Object, Object>> values) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        ArrayList<Necessaries> necessariesList = new ArrayList<>();

        if (connection != null) {
            PreparedStatement preparedStatement = null;

            try {
                String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Necessaries n";

                int count = fields.size();
                if (count > 0) {
                    StringBuilder extraWhereStatement = new StringBuilder(" WHERE");

                    if (fields.get(0).equals("date")) {
                        extraWhereStatement.append(" ((? <= n.expiredDate AND n.startDate <= ?) OR (n.expiredDate < ? AND ? <= n.expiredDate))");
                    } else {
                        extraWhereStatement.append(" ? <= n." + fields.get(0) + " AND n." + fields.get(0) + " <= ?");
                    }
                    for (int i = 1; i < count; i++) {
                        if (fields.get(i).equals("date")) {
                            extraWhereStatement.append(" AND ((? <= n.expiredDate AND n.startDate <= ?) OR (n.expiredDate < ? AND ? <= n.expiredDate))");
                        } else {
                            extraWhereStatement.append(" AND ? <= n." + fields.get(i) + " AND n." + fields.get(i) + " <= ?");
                        }
                    }

                    sqlStatement += extraWhereStatement.toString();
                }

                preparedStatement = connection.prepareStatement(sqlStatement);

                if (count > 0) {
                    int parameterIndex = 1;

                    for (int i = 0; i < count; i++) {
                        switch (fields.get(i)) {
                            case "limit" -> {
                                preparedStatement.setByte(parameterIndex, (byte) values.get(i).getLeftValue());
                                parameterIndex += 1;
                                preparedStatement.setByte(parameterIndex, (byte) values.get(i).getRightValue());
                                parameterIndex += 1;
                            }
                            case "price" -> {
                                preparedStatement.setInt(parameterIndex, (int) values.get(i).getLeftValue());
                                parameterIndex += 1;
                                preparedStatement.setInt(parameterIndex, (int) values.get(i).getRightValue());
                                parameterIndex += 1;
                            }
                            case "date" -> {
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getRightValue());
                                parameterIndex += 1;
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getRightValue());
                                parameterIndex += 1;
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getRightValue());
                                parameterIndex += 1;
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getLeftValue());
                                parameterIndex += 1;
                            }
                        }
                    }
                }

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    necessariesList.add(
                            new Necessaries(
                                    resultSet.getInt("necessariesId"),
                                    resultSet.getNString("necessariesName"),
                                    resultSet.getByte("limit"),
                                    resultSet.getTimestamp("startDate"),
                                    resultSet.getTimestamp("expiredDate"),
                                    resultSet.getInt("price")
                            )
                    );
                }
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - filterBy(ArrayList<String>, ArrayList<Pair<Object, Object>>)- catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> NecessariesDAO.java - filterBy(ArrayList<String>, ArrayList<Pair<Object, Object>>)- finally block <<<");

                        necessariesList.clear();
                        necessariesList.add(Necessaries.emptyInstance);
                    }
                }
            }
        } else {
            necessariesList.add(Necessaries.emptyInstance);
        }

        return necessariesList;
    }

    // This method check whether a necessaries name is existing or not.
    public byte isExistingNecessariesName(String necessariesName) {
        Connection connection = SingletonDBConnection.getInstance().getConnection();
        byte state = CONNECTION_ERROR;

        if (connection != null) {
            PreparedStatement preparedStatement = null;

            try {
                String sqlStatement = "SELECT * FROM COVID_MANAGEMENT.Necessaries WHERE necessariesName = ?";

                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setNString(1, necessariesName);

                ResultSet resultSet = preparedStatement.executeQuery();
                state = (resultSet.next()) ? EXISTING : NON_EXISTENCE;
            } catch (SQLException e) {
                System.out.println(">>> NecessariesDAO.java - isExistingNecessariesName(String) - catch block <<<");
                e.printStackTrace();
                state = CONNECTION_ERROR;
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> NecessariesDAO.java - isExistingNecessariesName(String) - finally block <<<");
                        state = CONNECTION_ERROR;
                    }
                }
            }
        }

        return state;
    }
}
