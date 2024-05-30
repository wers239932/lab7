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
        try {
            storage.remove(request.getLogin() , id);
            response.add("объект удален");
        } catch (NotAnOwnerException e) {
            response.add("нет права на удаление объекта");
        } catch (SQLException e) {
            throw new CommandException("ошибка при работе с базой данных");
        }
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
    @Override
    public Boolean validateParameter(ArrayList<String> commandLine) throws CommandException {
        try {
            int id = City.parseId(commandLine.get(0));
            return true;
        } catch (IdException e) {
            return false;
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException("введен неверный набор данных");
        }
    }
}
