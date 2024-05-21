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

    public ProxyStorage(DBStorageManager dbStorageManager) {
        this.dbStorageManager = dbStorageManager;
        this.storage = new Storage(this.dbStorageManager);
    }

    @Override
    public ArrayList<City> getCitiesList() {
        return storage.getCitiesList();
    }

    @Override
    public void add(City city) {
        try {
            this.dbStorageManager.add(city);
            this.storage.add(city);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(City city, int id) throws NotAnOwnerException {
        try {
            this.dbStorageManager.update(city, id);
            this.storage.update(city, id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try {
            this.dbStorageManager.clear();
            this.storage.clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public void remove(String login, int id) throws NotAnOwnerException {
        try {
            this.dbStorageManager.remove(login, id);
            this.storage.remove(login, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(String login, String passwd) {
        try {
            this.dbUserManager.register(login, passwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean auth(String login, String passwd) {
        try {
            return this.dbUserManager.auth(login, passwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
