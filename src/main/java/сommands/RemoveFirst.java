package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.db.NotAnOwnerException;
import storageInterface.StorageInterface;

import java.util.ArrayList;

public class RemoveFirst implements Command {


    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        ArrayList<String> response = new ArrayList<>();
        if(storage.getCitiesList().size()>=1) {
            try {
                storage.remove(request.getLogin() ,0);
                response.add("объект удален");
            } catch (NotAnOwnerException e) {
                response.add("нет права на удаление объекта");
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
}
