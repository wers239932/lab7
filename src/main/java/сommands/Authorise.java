package сommands;

import api.Request;
import cli.commandExceptions.CommandException;
import storageInterface.StorageInterface;

import java.util.ArrayList;

public class Authorise implements AuthentificationCommand {
    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        try {
            String login = (String) request.getArgs().get(0);
            String passwd = (String) request.getArgs().get(1);
            Boolean access = storage.auth(login, passwd);
            ArrayList<String> response = new ArrayList<>();
            response.add(String.valueOf(access));
            return response;
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "authorise";
    }

    @Override
    public String getDescription() {
        return "authorise {login} {password} : авторизоваться в качестве пользователя";
    }

    @Override
    public Boolean getNeedObject() {
        return false;
    }
}
