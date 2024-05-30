package storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUserManager {
    private Connection connection;
    private final String tableName = "users";
    public DBUserManager(Connection connection)
    {
        this.connection = connection;
    }
    public Boolean register(String login, String passwd) throws SQLException {
        String query1 = "SELECT * FROM " + this.tableName + " WHERE login = ?";
        PreparedStatement ps1 = this.connection.prepareStatement(query1);
        ps1.setString(1, login);
        ResultSet resultSet = ps1.executeQuery();
        if(resultSet.next()) return false;
        String query2 = "INSERT INTO " + this.tableName +
                " (login, passwd)" +
                " VALUES (?,?)";
        PreparedStatement ps = this.connection.prepareStatement(query2);
        ps.setString(1, login);
        ps.setString(2, passwd);
        int changed = ps.executeUpdate();
        if(changed!=1) throw new SQLException("неизвестная ошибка при взаимодействии с базой данных");
        return true;
    }
    public Boolean auth(String login, String passwd) throws SQLException {
        String query = "SELECT * FROM " + this.tableName + " WHERE login = ? AND passwd = ?";
        PreparedStatement ps = this.connection.prepareStatement(query);
        ps.setString(1, login);
        ps.setString(2, passwd);
        ResultSet resultSet = ps.executeQuery();
        return resultSet.next();
    }
    public void createTableIfNeeded() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS " + this.tableName +
                " (login TEXT PRIMARY KEY, passwd TEXT)";
        PreparedStatement ps = this.connection.prepareStatement(query);
        ps.execute();
    }
}

