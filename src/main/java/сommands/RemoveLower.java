package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.db.NotAnOwnerException;
import storage.objects.City;
import storageInterface.StorageInterface;

import java.sql.SQLException;
import java.util.ArrayList;

public class RemoveLower implements Command {

    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        City city = (City) request.getData();
        ArrayList<String> response = new ArrayList<>();
        for(City city1: storage.getCitiesList()) {
            if(city1.compareTo(city)<0 && city1.getOwnerLogin().equals(request.getLogin())) {
                try {
                    storage.remove(request.getLogin(), city1.getId());
                } catch (NotAnOwnerException ignored) {
                } catch (SQLException e) {
                    throw new CommandException("ошибка при работе с базой данных");
                }
            }
        }
        response.add("элементы удалены");
        return response;
    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String getDescription() {
        return "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный";
    }

    @Override
    public Boolean getNeedObject() {
        return true;
    }
    @Override
    public  Boolean validateParameter(ArrayList<String> commandline) {
        return true;
    }
}
