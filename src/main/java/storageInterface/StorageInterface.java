package storageInterface;

import storage.db.NotAnOwnerException;
import storage.objects.City;
import storage.objects.StorageInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public interface StorageInterface {
    public CopyOnWriteArrayList<City> getCitiesList();

    public void add(City city) throws SQLException;

    void update(City city, int id) throws NotAnOwnerException, SQLException;

    public void clear(String login) throws SQLException;

    public void save() throws IOException;

    public void load() throws IOException, SQLException;

    public StorageInfo getInfo();

    public Stream<City> getCitiesStream();

    void remove(String login, int id) throws NotAnOwnerException, SQLException;

    public Boolean register(String login, String passwd) throws SQLException;
    public Boolean auth(String login, String passwd) throws SQLException;
}
