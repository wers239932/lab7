package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storageInterface.StorageInterface;

import java.util.ArrayList;

public class Help implements Command {
    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        return null;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "help : вывести справку по доступным командам";
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
