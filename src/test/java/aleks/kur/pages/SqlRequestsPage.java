package aleks.kur.pages;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static ru.progredis.helpers.JdbcUrlSetting.*;

public class SqlRequestsPage {


    // обновление поля с типом Long по id объекта в таблице БД апп4
    public static void updateDbTableV4LongFieldByObjectId(String tableName, String tableFieldName, Long fieldValue, Long objectId) {

        final String
                UPDATE_QUERY =
                "UPDATE " + tableName +
                        " SET " + tableFieldName + " = ? " +
                        " WHERE id = ?";
        final String JDBC_URL = JDBC_URL_V4();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME_POSTGRES, PASSWORD_POSTGRES)) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setLong(1, fieldValue);
            preparedStatement.setLong(2, objectId);
            // Выполняем запрос
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Значение поля " + tableFieldName + " успешно обновлено на " + fieldValue +
                        " для объекта с ID " + objectId + " в таблице " + tableName);
            } else {
                System.out.println("Объект с ID " + objectId + " не найден в базе данных");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении значения поля " + tableFieldName + " " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // обновление поля с типом Long по id объекта в таблице БД апп2
    public static void updateDbTableV2LongFieldByObjectId(String tableName, String tableFieldName, Long fieldValue, Long objectId) {

        final String UPDATE_QUERY = "UPDATE " + tableName +
                " SET " + tableFieldName + " = ? " +
                " WHERE id = ?";

        final String JDBC_URL = JDBC_URL_V2(); // Убедитесь, что это правильный URL для вашей СУБД

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME_MYSQL, PASSWORD_MYSQL)) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setLong(1, fieldValue);
            preparedStatement.setLong(2, objectId);

            // Выполняем запрос
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Значение поля " + tableFieldName + " успешно обновлено на " + fieldValue +
                        " для объекта с ID " + objectId + " в таблице " + tableName);
            } else {
                System.out.println("Объект с ID " + objectId + " не найден в базе данных");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении значения поля " + tableFieldName + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // обновление поля с типом String по id объекта в таблице БД апп4
    public static void updateDbTableV4StringFieldByObjectId(String tableName, String tableFieldName, String fieldValue, int objectId) {

        final String
                UPDATE_QUERY =
                "UPDATE " + tableName +
                        " SET " + tableFieldName + " = ? " +
                        " WHERE id = ?";
        final String JDBC_URL = JDBC_URL_V4();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME_POSTGRES, PASSWORD_POSTGRES)) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);

            preparedStatement.setString(1, fieldValue);
            preparedStatement.setLong(2, objectId);

            // Выполняем запрос
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Значение поля " + tableFieldName + " успешно обновлено на " + fieldValue +
                        " для объекта с ID " + objectId + " в таблице " + tableName);
            } else {
                System.out.println("Объект с ID " + objectId + " не найден в базе данных");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении значения поля " + tableFieldName + " " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // получить массив id объектов по условиям в таблице БД апп2
    public static Set<Integer> selectIdsByConditionsDbTableV2(String tableName, String fieldName, String joinCondition, String whereConditions) {
//    public static int[] getIdsByConditionsDbTableV2(String tableName, String conditions) {

//        final String query = "SELECT x." + fieldName +" FROM " + tableName + " x WHERE " + conditions;
        final String query = "SELECT x." + fieldName +" FROM " + tableName + " x " + joinCondition + " WHERE " + whereConditions;

        Set<Integer> idSet = new HashSet<>();
//        List<Integer> idList = new ArrayList<>();
        final String JDBC_URL = JDBC_URL_V2(); // Убедитесь, что это правильный URL для вашей СУБД

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME_MYSQL, PASSWORD_MYSQL);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             // Выполняем запрос
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(fieldName);
                idSet.add(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idSet;
//        return idArray;
    }

    // получить массив id объектов по условиям в таблице БД апп4
    public static Set<Integer> selectIdsByConditionsDbTableV4(String tableName, String fieldName, String joinCondition, String whereConditions) {

        final String query = "SELECT x." + fieldName +" FROM " + tableName + " x " + joinCondition + " WHERE " + whereConditions;
        Set<Integer> idSet = new HashSet<>();
        final String JDBC_URL = JDBC_URL_V4(); // Убедитесь, что это правильный URL для вашей СУБД

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME_POSTGRES, PASSWORD_POSTGRES);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             // Выполняем запрос
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(fieldName);
                idSet.add(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idSet;
    }

}
