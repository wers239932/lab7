package сommands;

import api.Request;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.objects.City;
import storage.objectExceptions.CarCodeException;
import storageInterface.StorageInterface;

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
        storage.getToCollect(storage.getCitiesStream().filter(city -> !Objects.equals(city.getCarCode(), carCode)));
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
}
