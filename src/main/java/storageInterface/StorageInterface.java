package storageInterface;

import storage.objects.City;
import storage.objects.StorageInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public interface StorageInterface {
    public ArrayList<City> getCitiesList();

    public void add(City city);

    void update(City city, int id);

    public void clear();

    public void save() throws IOException;

    public void load() throws IOException, SQLException;

    public StorageInfo getInfo();

    public void removeFirst();

    public Stream<City> getCitiesStream();

    public void getToCollect(Stream<City> cityStream);
    public void remove(int id);
}
