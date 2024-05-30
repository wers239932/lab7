package cli;

import api.Request;
import cli.commandExceptions.CommandException;
import storageInterface.StorageInterface;

import java.io.Serializable;
import java.util.ArrayList;

public interface Command extends Serializable {
    ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException;

    String getName();

    String getDescription();

    Boolean getNeedObject();

    default Boolean validateParameter(ArrayList<String> commandline) throws CommandException {
        return true;
    }
}
