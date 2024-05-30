package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.db.NotAnOwnerException;
import storageInterface.StorageInterface;

import java.sql.SQLException;
import java.util.ArrayList;

public class RemoveFirst implements Command {


    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        ArrayList<String> response = new ArrayList<>();
        if (storage.getCitiesList().size() >= 1) {
            try {
                storage.remove(request.getLogin(), 0);
                response.add("объект удален");
            } catch (NotAnOwnerException e) {
                response.add("нет права на удаление объекта");
            } catch (SQLException e) {
                throw new CommandException("ошибка при работе с базой данных");
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "remove_first";
    }

    @Override
    public String getDescription() {
        return "remove_first : удалить первый элемент из коллекции";
    }

    @Override
    public Boolean getNeedObject() {
        return false;
    }

    @Override
    public Boolean validateParameter(ArrayList<String> commandline) {
        return true;
    }
}
