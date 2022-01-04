package com.dao;

import com.models.Necessaries;
import com.utilities.Pair;
import com.utilities.SingletonDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NecessariesDAO implements DAO<Necessaries, Integer> {
    @Override
    public List<Necessaries> getAll() {
        return null;
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
                        " GROUP BY n.necessariesId) AS pnc ON n1.necessariesId = pnc.necessariesId";

                if (necessariesName != null && !necessariesName.isBlank()) {
                    sqlStatement += " WHERE n1.necessariesName LIKE N'%" + necessariesName + "%'";
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
                System.out.println(">>> UserDAO.java - getAllCanPurchaseByUserId(Integer) - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> UserDAO.java - getAllCanPurchaseByUserId(Integer) - finally block <<<");

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

    @Override
    public boolean update(Necessaries entity) {
        return false;
    }

    @Override
    public boolean delete(Necessaries entity) {
        return false;
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
                System.out.println(">>> UserDAO.java - getAllCanPurchaseByUserIdAndSortBy(String, String, Integer) - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> UserDAO.java - getAllCanPurchaseByUserIdAndSortBy(String, String, Integer) - finally block <<<");

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
                        " GROUP BY n.necessariesId) AS pnc ON n1.necessariesId = pnc.necessariesId";

                if (!fields.isEmpty()) {
                    StringBuilder whereStatement = new StringBuilder(" WHERE");
                    boolean isAddAndStatement = false;

                    for (String field : fields) {
                        if (isAddAndStatement)
                            whereStatement.append(" AND");

                        if (field.equals("date")) {
                            whereStatement.append(" ? <= n1.startDate AND n1.expiredDate <= ?");
                            isAddAndStatement = true;
                        } else {
                            whereStatement.append(" ? <= n1." + field + " AND n1." + field + " <= ?");
                            isAddAndStatement = true;
                        }
                    }

                    sqlStatement += whereStatement.toString();
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
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getLeftValue());
                                parameterIndex += 1;
                                preparedStatement.setTimestamp(parameterIndex, (Timestamp) values.get(i).getRightValue());
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
                System.out.println(">>> UserDAO.java - getAllCanPurchaseByUserIdAndFilterBy(ArrayList, ArrayList, Integer) - catch block <<<");
                e.printStackTrace();

                necessariesList.clear();
                necessariesList.add(Necessaries.emptyInstance);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println(">>> UserDAO.java - getAllCanPurchaseByUserIdAndFilterBy(ArrayList, ArrayList, Integer) - finally block <<<");

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
}
