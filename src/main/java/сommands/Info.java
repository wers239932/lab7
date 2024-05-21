package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.objects.StorageInfo;
import storageInterface.StorageInterface;

import java.util.ArrayList;

public class Info implements Command {


    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        ArrayList<String> response = new ArrayList<>();
        StorageInfo storageInfo = storage.getInfo();
        response.add("Дата созданиия: " + storageInfo.getCreationDate().toString());
        response.add("количество элементов в памяти: " + storageInfo.getSize());
        return response;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public Boolean getNeedObject() {
        return false;
    }
}
