package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.objects.City;
import storage.objectExceptions.IdException;
import storageInterface.StorageInterface;

import java.util.ArrayList;

public class RemoveById implements Command {


    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        int id;
        try {
            id = City.parseId((String) request.getArgs().get(0));
        } catch (IdException e) {
            throw new CommandException(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException("не введен аргумент");
        }
        ArrayList<String> response = new ArrayList<>();
        storage.getToCollect(storage.getCitiesStream().filter(city -> city.getId() != id));
        response.add("объект удален");
        return response;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }

    @Override
    public Boolean getNeedObject() {
        return false;
    }
}
