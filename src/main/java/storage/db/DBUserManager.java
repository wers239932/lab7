package storage.db;

import сommands.Register;

import java.io.StringReader;
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
    public void register(String login, String passwd) throws SQLException {
        String query = "INSERT INTO " + this.tableName +
                " (login, passwd)" +
                " VALUES (?,?)";
        PreparedStatement ps = this.connection.prepareStatement(query);
        ps.setString(1, login);
        ps.setString(2, passwd);
        int changed = ps.executeUpdate();
        if(changed!=1) throw new SQLException("неизвестная ошибка при взаимодействии с базой данных");
    }
    public Boolean auth(String login, String passwd) throws SQLException {
        String query = "SELECT id FROM " + this.tableName + " WHERE login = ? AND passwd = ?";
        PreparedStatement ps = this.connection.prepareStatement(query);
        ps.setString(1, login);
        ps.setString(2, passwd);
        ResultSet resultSet = ps.executeQuery();
        return resultSet.next();
    }
}
