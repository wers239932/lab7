package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.db.NotAnOwnerException;
import storage.objectExceptions.IdException;
import storage.objects.City;
import storage.objectExceptions.CarCodeException;
import storageInterface.StorageInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class RemoveAllByCarCode implements Command {


    @Override
    public ArrayList<String> execute(Request request, StorageInterface storage) throws CommandException {
        Long carCode;
        try {
            carCode = City.parseCarCode((String) request.getArgs().get(0));
        } catch (CarCodeException e) {
            throw new CommandException(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException("не введен аргумент");
        }
        ArrayList<String> response = new ArrayList<>();
        for(City city : storage.getCitiesList()) {
            if(city.getCarCode().equals(carCode) && city.getOwnerLogin().equals(request.getLogin())) {
                try {
                    storage.remove(request.getLogin() ,city.getId());
                } catch (NotAnOwnerException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        response.add("объекты удалены");
        return response;
    }

    @Override
    public String getName() {
        return "remove_all_by_car_code";
    }

    @Override
    public String getDescription() {
        return "remove_all_by_car_code carCode : удалить из коллекции все элементы, значение поля carCode которого эквивалентно заданному";
    }

    @Override
    public Boolean getNeedObject() {
        return false;
    }
    @Override
    public Boolean validateParameter(ArrayList<String> commandLine) throws CommandException {
        try {
            Long carCode = City.parseCarCode(commandLine.get(0));
            return true;
        } catch (CarCodeException e) {
            throw new RuntimeException(e);
        }
        catch (IndexOutOfBoundsException e) {
            throw new CommandException("введен неверный набор данных");
        }
    }
}
