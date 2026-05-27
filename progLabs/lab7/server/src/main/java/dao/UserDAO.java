package dao;

import utils.PasswordHasher;
import java.sql.*;

/**
 * Класс для работы с таблицей пользователей (users) в БД.
 */
public class UserDAO {
    private final DatabaseConnection databaseConnection;

    public UserDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Проверяет учетные данные пользователя. Если пользователя нет — регистрирует его.
     */
    public boolean authenticateUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null) return false;

        String sqlSelect = "SELECT password_hash FROM users WHERE username = ?";
        String sqlInsert = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        String passwordHash = PasswordHasher.hashSHA384(password);

        try (Connection conn = databaseConnection.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    return rs.getString("password_hash").equals(passwordHash);
                }
            }

            // Регистрация нового пользователя
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setString(1, username);
                pstmt.setString(2, passwordHash);
                pstmt.executeUpdate();
                System.out.println("Зарегистрирован новый пользователь: " + username);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при аутентификации/регистрации: " + e.getMessage());
            return false;
        }
    }

    /**
     * Вспомогательный метод для получения ID пользователя по его логину.
     */
    public int getUserId(Connection conn, String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("Пользователь " + username + " не найден.");
        }
    }
}