package storage;

import storage.db.DBManager;
import storage.objects.City;
import storage.objects.StorageInfo;
import storageInterface.StorageInterface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ProxyStorage implements StorageInterface {
    private Storage storage;
    private DBManager dbManager;

    public ProxyStorage(DBManager dbManager) {
        this.dbManager = dbManager;
        this.storage = new Storage(this.dbManager);
    }

    @Override
    public ArrayList<City> getCitiesList() {
        return storage.getCitiesList();
    }

    @Override
    public void add(City city) {
        try {
            this.dbManager.add(city);
            this.storage.add(city);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(City city, int id) {
        try {
            this.dbManager.update(city, id);
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
            this.dbManager.clear();
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

    //useless
    @Override
    public void removeFirst() {

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
    public void remove(int id) {
        try {
            this.dbManager.remove(id);
            this.storage.remove(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
