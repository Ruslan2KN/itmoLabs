package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Класс для управления пулом соединений с базой данных.
 */
public class DatabaseConnection {
    private final HikariDataSource dataSource;

    public DatabaseConnection(String host, int port, String database, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);

        // Настройки пула
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(5000);

        this.dataSource = new HikariDataSource(config);
    }

    /**
     * Получает активное соединение из пула.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Закрывает пул соединений при остановке сервера.
     */
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}