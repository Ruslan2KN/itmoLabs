package dao;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

/**
 * Класс, отвечающий за проверку и миграцию структуры базы данных при старте приложения.
 */
public class DatabaseInitializer {
    private final HikariDataSource dataSource;

    public DatabaseInitializer(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Запускает процесс миграции таблиц через Flyway.
     */
    public void initialize() {
        try {
            System.out.println("Проверка миграции таблиц");
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .load();

            flyway.migrate();
            System.out.println("Таблицы существуют");
        } catch (Exception e) {
            System.err.println("Ошибка, не удалось мигрировать таблицы: " + e.getMessage());
            System.exit(1);
        }
    }
}