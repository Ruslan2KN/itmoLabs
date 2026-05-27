package dao;

import models.*;
import java.sql.*;
import java.util.ArrayDeque;

/**
 * Класс для управления объектами учебных групп в базе данных.
 */
public class StudyGroupDAO {
    private final DatabaseConnection databaseConnection;
    private final UserDAO userDAO;

    public StudyGroupDAO(DatabaseConnection databaseConnection, UserDAO userDAO) {
        this.databaseConnection = databaseConnection;
        this.userDAO = userDAO;
    }

    /**
     * Загружает всю коллекцию из базы данных при старте сервера.
     */
    public ArrayDeque<StudyGroup> loadCollection() {
        ArrayDeque<StudyGroup> collection = new ArrayDeque<>();
        String sql = "SELECT sg.*, u.username as owner_name, " +
                "a.name as admin_name, a.height as admin_height, a.eye_color as admin_eye_color, a.nationality as admin_nationality, " +
                "l.x as loc_x, l.y as loc_y, l.name as loc_name " +
                "FROM study_groups sg " +
                "JOIN users u ON sg.owner_id = u.id " +
                "LEFT JOIN admins a ON sg.id_admin = a.id " +
                "LEFT JOIN locations l ON a.id_location = l.id " +
                "ORDER BY sg.id ASC";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                StudyGroup group = new StudyGroup();
                group.setId(rs.getLong("id"));
                group.setName(rs.getString("name"));
                group.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                group.setStudentsCount(rs.getInt("students_count"));

                int transferred = rs.getInt("transferred_students");
                group.setTransferredStudents(rs.wasNull() ? null : transferred);

                double avgMark = rs.getDouble("average_mark");
                group.setAverageMark(rs.wasNull() ? null : avgMark);

                String formStr = rs.getString("form_of_education");
                group.setFormOfEducation(formStr == null ? null : FormOfEducation.valueOf(formStr));
                group.setCoordinates(new Coordinates(rs.getFloat("coord_x"), rs.getDouble("coord_y")));
                group.setOwner(rs.getString("owner_name"));

                // Админ группы
                String adminName = rs.getString("admin_name");
                if (adminName != null) {
                    Person admin = new Person();
                    admin.setName(adminName);

                    double height = rs.getDouble("admin_height");
                    admin.setHeight(rs.wasNull() ? null : height);

                    admin.setEyeColor(Color.valueOf(rs.getString("admin_eye_color")));
                    admin.setNationality(Country.valueOf(rs.getString("admin_nationality")));

                    // Локация админа
                    double locX = rs.getDouble("loc_x");
                    if (!rs.wasNull()) {
                        Location loc = new Location();
                        loc.setX(locX);
                        loc.setY(rs.getLong("loc_y"));
                        loc.setName(rs.getString("loc_name"));
                        admin.setLocation(loc);
                    }
                    group.setGroupAdmin(admin);
                }
                collection.add(group);
            }
        } catch (SQLException e) {
            System.out.println("Критическая ошибка при загрузке коллекции из БД: " + e.getMessage());
        }
        return collection;
    }

    /**
     * Сохраняет новую группу в БД.
     */
    public long insertStudyGroup(StudyGroup group, String username) throws SQLException {
        Connection conn = null;
        try {
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false);

            int userId = userDAO.getUserId(conn, username);

            // Локация
            Integer locId = null;
            Person admin = group.getGroupAdmin();
            if (admin != null && admin.getLocation() != null) {
                Location loc = admin.getLocation();
                String sqlLoc = "INSERT INTO locations (x, y, name) VALUES (?, ?, ?) RETURNING id";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlLoc)) {
                    pstmt.setDouble(1, loc.getX());
                    pstmt.setLong(2, loc.getY());
                    pstmt.setString(3, loc.getName());
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) locId = rs.getInt(1);
                }
            }

            // Админ
            Integer adminId = null;
            if (admin != null) {
                String sqlAdmin = "INSERT INTO admins (name, height, eye_color, nationality, id_location) VALUES (?, ?, ?::color, ?::country, ?) RETURNING id";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlAdmin)) {
                    pstmt.setString(1, admin.getName());
                    if (admin.getHeight() != null) pstmt.setDouble(2, admin.getHeight());
                    else pstmt.setNull(2, Types.DOUBLE);
                    pstmt.setString(3, admin.getEyeColor().name());
                    pstmt.setString(4, admin.getNationality().name());
                    if (locId != null) pstmt.setInt(5, locId);
                    else pstmt.setNull(5, Types.INTEGER);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) adminId = rs.getInt(1);
                }
            }

            // Группа
            long generatedId = -1;
            String sqlGroup = "INSERT INTO study_groups (name, coord_x, coord_y, students_count, transferred_students, average_mark, form_of_education, id_admin, owner_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?::form_of_education, ?, ?) RETURNING id, creation_date";

            try (PreparedStatement pstmt = conn.prepareStatement(sqlGroup)) {
                pstmt.setString(1, group.getName());
                pstmt.setFloat(2, group.getCoordinates().getX());
                pstmt.setDouble(3, group.getCoordinates().getY());
                pstmt.setInt(4, group.getStudentsCount());

                if (group.getTransferredStudents() != null) pstmt.setInt(5, group.getTransferredStudents());
                else pstmt.setNull(5, Types.INTEGER);

                if (group.getAverageMark() != null) pstmt.setDouble(6, group.getAverageMark());
                else pstmt.setNull(6, Types.DOUBLE);

                if (group.getFormOfEducation() != null) pstmt.setString(7, group.getFormOfEducation().name());
                else pstmt.setNull(7, Types.OTHER);

                if (adminId != null) pstmt.setInt(8, adminId);
                else pstmt.setNull(8, Types.INTEGER);

                pstmt.setInt(9, userId);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    generatedId = rs.getLong("id");
                    group.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                }
            }

            conn.commit();
            return generatedId;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    public boolean deleteStudyGroupById(long id, String username) throws SQLException {
        String sql = "DELETE FROM study_groups WHERE id = ? AND owner_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    public void clearStudyGroups(String username) throws SQLException {
        String sql = "DELETE FROM study_groups WHERE owner_id = (SELECT id FROM users WHERE username = ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }

    public boolean updateStudyGroup(long id, StudyGroup group, String username) throws SQLException {
        // ... (Сюда просто полностью и без изменений перенеси свой старый код метода updateStudyGroup)
        // Только замени `dataSource.getConnection()` на `databaseConnection.getConnection()`
        return true;
    }

    public int deleteGreaterStudyGroups(int studentsCount, String username) throws SQLException {
        String sql = "DELETE FROM study_groups WHERE owner_id = (SELECT id FROM users WHERE username = ?) AND students_count > ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, studentsCount);
            return pstmt.executeUpdate();
        }
    }

    public int deleteLowerStudyGroups(int studentsCount, String username) throws SQLException {
        String sql = "DELETE FROM study_groups WHERE owner_id = (SELECT id FROM users WHERE username = ?) AND students_count < ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, studentsCount);
            return pstmt.executeUpdate();
        }
    }
}