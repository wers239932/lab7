package storageInterface;

import storage.db.NotAnOwnerException;
import storage.objects.City;
import storage.objects.StorageInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public interface StorageInterface {
    CopyOnWriteArrayList<City> getCitiesList();

    void add(City city) throws SQLException;

    void update(City city, int id) throws NotAnOwnerException, SQLException;

    void clear(String login) throws SQLException;

    void save() throws IOException;

    void load() throws IOException, SQLException;

    StorageInfo getInfo();

    Stream<City> getCitiesStream();

    void remove(String login, int id) throws NotAnOwnerException, SQLException;

    Boolean register(String login, String passwd) throws SQLException;

    Boolean auth(String login, String passwd) throws SQLException;
}
