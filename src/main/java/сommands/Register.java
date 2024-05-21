package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storageInterface.StorageInterface;

import java.util.ArrayList;

public class Register implements Command {
    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        try {
            String login = (String) request.getArgs().get(0);
            String passwd = (String) request.getArgs().get(1);
            storage.register(login, passwd);
            ArrayList<String> response = new ArrayList<>();
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "register {login} {password} : зарегистрировать нового пользователя";
    }

    @Override
    public Boolean getNeedObject() {
        return false;
    }
}
