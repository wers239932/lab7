package api;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * класс запроса клиента для отправки по сети
 *
 * @param <T>
 */

public class Request<T> implements Serializable {

    /**
     * название команды серверу
     */
    private String commandName;
    /**
     * информация для запроса
     */
    private T data;
    private ArrayList<String> args;
    private String login;
    private String passwd;

    public Request(String commandName, T data, ArrayList<String> args, String login, String passwd) {
        this.commandName = commandName;
        this.data = data;
        this.args = args;
        this.login = login;
        this.passwd = passwd;
    }
    public Request(String commandName, T data, ArrayList<String> args) {
        this.commandName = commandName;
        this.data = data;
        this.args = args;
    }

    public Request(String commandName) {
        this.commandName = commandName;
        this.data = null;
    }

    public String getCommandName() {
        return commandName;
    }

    public T getData() {
        return data;
    }

    public ArrayList<String> getArgs() {
        return this.args;
    }

    public String getLogin() {
        return this.login;
    }
    public String getPasswd() {
        return this.passwd;
    }
}