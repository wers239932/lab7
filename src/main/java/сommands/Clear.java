package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storageInterface.StorageInterface;

import java.sql.SQLException;
import java.util.ArrayList;

public class Clear implements Command {


    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        ArrayList<String> response = new ArrayList<>();
        try {
            storage.clear(request.getLogin());
        } catch (SQLException e) {
            throw new CommandException("ошибка при работе с базой данных");
        }
        response.add("коллекция очищена");
        return response;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "clear : очистить коллекцию";
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
