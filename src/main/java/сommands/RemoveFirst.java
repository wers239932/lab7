package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storageInterface.StorageInterface;

import java.util.ArrayList;

public class RemoveFirst implements Command {


    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        if(storage.getCitiesList().size()>=1)
            storage.remove(0);
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
