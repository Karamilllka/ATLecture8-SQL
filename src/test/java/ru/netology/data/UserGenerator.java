package ru.netology.data;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserGenerator {
    public static Connection getConnection() throws SQLException {
        final Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");
        return connection;
    }

    @SneakyThrows
    public static String getVerificationCode(String login) {
        String userId = null;
        var dataSQL = "SELECT id FROM users WHERE login = ?;";
        try (var conn = getConnection();
             var idStmt = conn.prepareStatement(dataSQL);
        ) {
            idStmt.setString(1, login);
            try (var rs = idStmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getString("id");
                }
            }
        }
        String code = null;
        var authCode = "SELECT code FROM auth_codes WHERE user_id = ? order by created desc limit 1;";
        try (var conn = getConnection();
             var codeStmt = conn.prepareStatement(authCode);
        ) {
            codeStmt.setString(1, userId);
            try (var rs = codeStmt.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        }
        return code;
    }

    @SneakyThrows
    public static String getStatusFromDb(String login) {
        String statusSQL = "SELECT status FROM users WHERE login = ?;";
        String status = null;
        try (var conn = getConnection();
             var statusStmt = conn.prepareStatement(statusSQL);) {
            statusStmt.setString(1, login);
            try (var rs = statusStmt.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        }
        return status;
    }

    @SneakyThrows
    public static void cleanDb() {
        String deleteCards = "DELETE FROM cards; ";
        String deleteAuthCodes = "DELETE FROM auth_codes; ";
        String deleteUsers = "DELETE FROM users; ";
        try (var conn = UserGenerator.getConnection();
             var deleteCardsStmt = conn.createStatement();
             var deleteAuthCodesStmt = conn.createStatement();
             var deleteUsersStmt = conn.createStatement();
        ) {
            deleteCardsStmt.executeUpdate(deleteCards);
            deleteAuthCodesStmt.executeUpdate(deleteAuthCodes);
            deleteUsersStmt.executeUpdate(deleteUsers);
        }
    }
}
