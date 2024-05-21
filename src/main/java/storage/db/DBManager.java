package storage.db;

import api.RequestStatus;
import dal.DataLoader;
import storage.objects.City;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DBManager implements DataLoader {
    private Connection connection;
    private final String tableName = "cities_list";
    public DBManager(Connection connection)
    {
        this.connection = connection;
    }
    private ResultSet executeQuery(String request) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        return resultSet;
    }
    private int executeUpdate(String request) throws SQLException {
        Statement statement = this.connection.createStatement();
        int changed = statement.executeUpdate(request);
        return changed;
    }
    public void add(City city) throws SQLException {
        String query = "INSERT INTO " + this.tableName +
                " (name, coordinate_x, coordinate_y, creation_date, area, population, meters_above_sea_level, capital, carCode, government, governor)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement ps = this.connection.prepareStatement(query);
        this.addCity(ps, city);
        int changed = ps.executeUpdate();
        if(changed!=1) throw new SQLException("неизвестная ошибка при взаимодействии с базой данных");
        ps = this.connection.prepareStatement("SELECT id FROM " + tableName + " ORDER BY id DESC LIMIT 1;");
        ResultSet resultSet = ps.executeQuery();
        int id;
        if (resultSet.next())
            id = resultSet.getInt("id");
        else
            throw new SQLException("нет получен id добавленного города");
        city.setId(id);
    }
    private void addCity(PreparedStatement ps, City city) throws SQLException {
        ps.setString(1, city.getName());
        ps.setString(2, String.valueOf(city.getCoordinates().getX()));
        ps.setString(3, String.valueOf(city.getCoordinates().getY()));
        ps.setString(4, String.valueOf(city.getCreationDate()));
        ps.setString(5, String.valueOf(city.getArea()));
        ps.setString(6, String.valueOf(city.getPopulation()));
        ps.setString(7, String.valueOf(city.getMetersAboveSeaLevel()));
        ps.setString(8, String.valueOf(city.getCapital()));
        ps.setString(9, String.valueOf(city.getCarCode()));
        ps.setString(10, String.valueOf(city.getGovernment()));
        ps.setString(11, String.valueOf(city.getGovernor()));
    }
    public void update(City city, int id) throws SQLException {
        String query = "UPDATE " + this.tableName + " SET " +
                "name = ?, coordinate_x = ?, coordinate_y = ?, creation_date = ?, area = ?, population = ?, meters_above_sea_level = ?, capital = ?, carCode = ?, government = ?, governor = ?" +
                "WHERE id = ?";
        PreparedStatement ps = this.connection.prepareStatement(query);
        this.addCity(ps, city);
        ps.setInt(12, id);
        System.out.println(ps.executeUpdate());
    }
    public void remove(int id) throws SQLException {
        String query = "DELETE FROM " + this.tableName +" WHERE id = ?";
        PreparedStatement ps = this.connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }
    public void clear() throws SQLException {
        String query = "TRUNCATE " + tableName;
        PreparedStatement ps = this.connection.prepareStatement(query);
        ps.executeUpdate();
    }
    public ArrayList<String[]> getCitiesList() throws SQLException {
        String query = "SELECT * FROM " + tableName + ";";
        PreparedStatement ps = this.connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();
        resultSet.first();
        ArrayList<String[]> citiesList = new ArrayList<>();
        while(!resultSet.isAfterLast()) {
            int id = resultSet.getInt("id");
            ArrayList<String> codedCity = new ArrayList<>();
            codedCity.add(Integer.valueOf(id).toString());
            codedCity.add(resultSet.getString("name"));
            codedCity.add(resultSet.getString("coordinate_x"));
            codedCity.add(resultSet.getString("coordinate_y"));
            codedCity.add(resultSet.getString("creation_date"));
            codedCity.add(resultSet.getString("area"));
            codedCity.add(resultSet.getString("population"));
            codedCity.add(resultSet.getString("meters_above_sea_level"));
            codedCity.add(resultSet.getString("capital"));
            codedCity.add(resultSet.getString("carCode"));
            codedCity.add(resultSet.getString("government"));
            codedCity.add(resultSet.getString("governor"));
            citiesList.add((String[]) codedCity.toArray());
        }
        return citiesList;
    }

    @Override
    public ArrayList<String[]> readAllRecords() throws IOException, SQLException {
        String query = "SELECT * FROM " + tableName + "";
        PreparedStatement ps = this.connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = ps.executeQuery();
        ArrayList<String[]> citiesList = new ArrayList<>();
        while(resultSet.next()) {
            int id = resultSet.getInt("id");
            ArrayList<String> codedCity = new ArrayList<>();
            codedCity.add(Integer.toString(id));
            codedCity.add(resultSet.getString("name"));
            codedCity.add(resultSet.getString("coordinate_x"));
            codedCity.add(resultSet.getString("coordinate_y"));
            codedCity.add(resultSet.getString("creation_date"));
            codedCity.add(resultSet.getString("area"));
            codedCity.add(resultSet.getString("population"));
            codedCity.add(resultSet.getString("meters_above_sea_level"));
            codedCity.add(resultSet.getString("capital"));
            codedCity.add(resultSet.getString("carCode"));
            codedCity.add(resultSet.getString("government"));
            codedCity.add(resultSet.getString("governor"));
            String[] codedCity1 = new String[codedCity.size()];
            codedCity1 = codedCity.toArray(codedCity1);
            citiesList.add(codedCity1);
        }
        return citiesList;
    }
}
