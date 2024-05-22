package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.db.NotAnOwnerException;
import storage.objects.City;
import storage.objectExceptions.IdException;
import storageInterface.StorageInterface;

import java.sql.SQLException;
import java.util.ArrayList;

public class Update implements Command {

    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        int id;
        try {
            id = City.parseId((String) request.getArgs().get(0));
        } catch (IdException e) {
            throw new CommandException(e.getMessage());
        }
        City city = (City) request.getData();
        ArrayList<String> response = new ArrayList<>();
        try {
            storage.update(city, Integer.parseInt((String) request.getArgs().get(0)));
            response.add("объект обновлен");
        } catch (NotAnOwnerException e) {
            response.add("нет права на изменение объекта");
        } catch (SQLException e) {
            throw new CommandException("ошибка при работе с базой данных");
        }
        return response;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public Boolean getNeedObject() {
        return true;
    }
}
