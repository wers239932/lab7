package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.objects.City;
import storageInterface.StorageInterface;

import java.sql.SQLException;
import java.util.ArrayList;

public class Add implements Command {

    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        ArrayList<String> response = new ArrayList<>();
        try {
            storage.add((City) request.getData());
            response.add("добавлен город");
        } catch (SQLException e) {
            throw new CommandException("ошибка при работе с базой данных");
        }
        return response;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "add {element} : добавить новый элемент в коллекцию";
    }

    @Override
    public Boolean getNeedObject() {
        return true;
    }

}
