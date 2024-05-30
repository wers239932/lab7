package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.objects.City;
import storageInterface.StorageInterface;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Show implements Command {

    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        ArrayList<String> response = new ArrayList<>();
        for (City city : storage.getCitiesStream().collect(Collectors.toCollection(ArrayList::new))) {
            response.add((city).toString());
        }
        return response;
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public Boolean getNeedObject() {
        return false;
    }
    @Override
    public  Boolean validateParameter(ArrayList<String> commandline) {
        return true;
    }
}
