package storage;

import storage.db.DBStorageManager;
import storage.db.DBUserManager;
import storage.db.NotAnOwnerException;
import storage.objects.City;
import storage.objects.StorageInfo;
import storageInterface.StorageInterface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ProxyStorage implements StorageInterface {
    private Storage storage;
    private DBStorageManager dbStorageManager;
    private DBUserManager dbUserManager;

    public ProxyStorage(DBStorageManager dbStorageManager, DBUserManager dbUserManager) {
        this.dbStorageManager = dbStorageManager;
        this.storage = new Storage(this.dbStorageManager);
        this.dbUserManager = dbUserManager;
    }

    @Override
    public ArrayList<City> getCitiesList() {
        return storage.getCitiesList();
    }

    @Override
    public void add(City city) throws SQLException {
        this.dbStorageManager.add(city);
        this.storage.add(city);
    }

    @Override
    public void update(City city, int id) throws NotAnOwnerException, SQLException {
        this.dbStorageManager.update(city, id);
        this.storage.update(city, id);

    }

    @Override
    public void clear(String login) throws SQLException {
        this.dbStorageManager.clear(login);
        this.storage.clear(login);

    }

    //useless
    @Override
    public void save() throws IOException {

    }

    @Override
    public void load() throws IOException, SQLException {
        this.storage.load();
    }

    @Override
    public StorageInfo getInfo() {
        return this.storage.getInfo();
    }


    @Override
    public Stream<City> getCitiesStream() {
        return this.storage.getCitiesList().stream();
    }

    @Override
    public void getToCollect(Stream<City> cityStream) {
        this.storage.getToCollect(cityStream);
    }

    @Override
    public void remove(String login, int id) throws NotAnOwnerException, SQLException {
        this.dbStorageManager.remove(login, id);
        this.storage.remove(login, id);

    }

    @Override
    public Boolean register(String login, String passwd) throws SQLException {
        return this.dbUserManager.register(login, passwd);

    }

    @Override
    public Boolean auth(String login, String passwd) throws SQLException {
        return this.dbUserManager.auth(login, passwd);
    }
}
