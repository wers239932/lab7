package storage;

import dal.DataAccessLayer;
import dal.DataLoader;
import storage.objects.City;
import storage.objects.StorageInfo;
import storageInterface.StorageInterface;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class Storage implements StorageInterface, Serializable {
    private final CopyOnWriteArrayList<City> objects;
    private final Date creationDate;
    private final DataLoader dataAccessLayer;

    public Storage(DataLoader dataAccessLayer) {
        this.objects = new CopyOnWriteArrayList<>();
        this.dataAccessLayer = dataAccessLayer;
        this.creationDate = new Date();
    }

    public Storage(DataAccessLayer dal) {
        this.objects = new CopyOnWriteArrayList<>();
        this.dataAccessLayer = dal;
        this.creationDate = new Date();
    }

    @Override
    public void load() throws IOException, SQLException {
        ArrayList<String[]> records = this.dataAccessLayer.readAllRecords();
        City city;
        for (String[] record : records) {
            try {
                city = City.parseCity(record);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            this.objects.add(city);
        }
    }

    @Override
    public void save() throws IOException {

    }

    @Override
    public CopyOnWriteArrayList<City> getCitiesList() {
        CityNameComparator comparator = new CityNameComparator();
        this.objects.sort(comparator);
        return this.objects;
    }


    @Override
    public void add(City city) {
        this.objects.add(city);
    }

    @Override
    public void update(City city, int id) {
        for (City cityStored : this.objects) {
            if (cityStored.getId() == id) {
                cityStored.setName(city.getName());
                cityStored.setArea(city.getArea());
                cityStored.setGovernment(city.getGovernment());
                cityStored.setGovernor(city.getGovernor());
                cityStored.setCapital(city.getCapital());
                cityStored.setCarCode(city.getCarCode());
                cityStored.setMetersAboveSeaLevel(city.getMetersAboveSeaLevel());
                cityStored.setPopulation(city.getPopulation());
                cityStored.setCoordinates(city.getCoordinates());
            }
        }
    }


    @Override
    public void clear(String login) {
        this.objects.removeIf(city -> login.equals(city.getOwnerLogin()));
    }


    @Override
    public StorageInfo getInfo() {
        return new StorageInfo(this.objects.size(), this.creationDate);
    }


    @Override
    public Stream<City> getCitiesStream() {
        return this.objects.stream();
    }


    @Override
    public void remove(String login, int id) {
        this.objects.removeIf(city -> city.getId() == id);
    }

    @Override
    public Boolean register(String login, String passwd) {
        return true;
    }

    @Override
    public Boolean auth(String login, String passwd) {
        return true;
    }

}
