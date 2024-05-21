package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.objects.City;
import storageInterface.StorageInterface;

import java.util.ArrayList;

public class RemoveGreater implements Command {

    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        City city = (City) request.getData();
        ArrayList<String> response = new ArrayList<>();
        for(City city1: storage.getCitiesList()) {
            if(city1.compareTo(city)>0)
                storage.remove(city1.getId());
        }
        response.add("элементы удалены");
        return response;
    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public Boolean getNeedObject() {
        return true;
    }
}
