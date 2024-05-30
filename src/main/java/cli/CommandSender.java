package cli;

import api.Request;
import api.Response;
import cli.commandExceptions.CommandDoesntExistException;
import cli.commandExceptions.CommandException;
import client.Client;
import client.Encrypter;
import client.InteractiveCityParser;
import storage.objects.City;
import сommands.Authorise;
import сommands.ExecuteScript;
import сommands.Exit;
import сommands.Register;

import java.io.IOException;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class CommandSender {
    private HashMap<String, Command> commandArray;
    private IOInterface terminal;
    private HashSet<String> runningScripts;
    private Client client;
    private String login;
    private String passwd;
    private Boolean loggedIn = false;

    public CommandSender(IOInterface terminal, Client client) {
        this.terminal = terminal;
        this.commandArray = new HashMap<>();
        this.runningScripts = new HashSet<String>();
        this.client = client;
    }

    public CommandSender(IOInterface terminal, HashSet<String> runningScripts, Client client) {
        this.terminal = terminal;
        this.commandArray = new HashMap<>();
        this.runningScripts = runningScripts;
        this.client = client;
    }

    private Collection<Command> getCommandArray() {
        return new ArrayList<Command>(commandArray.values());
    }

    private void addCommandArray(Collection<Command> commandArrayList) {
        for (Command command : commandArrayList) {
            this.addCommand(command);
        }
    }


    public void start() {
        ArrayList<Command> commands = (ArrayList<Command>) this.client.sendRequest(new Request("getCommands")).getData();
        this.addCommandArray(commands);
        this.getToLogIn();
        while (true) {
            try {
                ArrayList<String> commandLine = new ArrayList<>(List.of(this.terminal.readLine().split(" +")));
                String commandName = commandLine.get(0);
                commandLine.remove(0);
                Command command = this.getCommand(commandName);
                if(!command.validateParameter(commandLine)) {
                    throw new CommandException("введен неверный набор данных");
                }
                if(command instanceof Authorise) {
                    String login = commandLine.get(0);
                    String passwd = Encrypter.encrypt(commandLine.get(1));
                    commandLine.set(1, passwd);
                    Response response = this.client.sendRequest(new Request<>(commandName, null, commandLine, this.login, this.passwd));
                    if(Boolean.parseBoolean(((ArrayList<String>) response.getData()).get(0))) {
                        this.login = login;
                        this.passwd = passwd;
                        this.terminal.writeLine("доступ получен");
                        this.loggedIn = true;
                    }
                    else {
                        terminal.writeLine("доступ запрещен");
                        terminal.writeLine(((ArrayList<String>) response.getData()).get(0));
                    }
                } else if (command instanceof Register) {
                    String login = commandLine.get(0);
                    String passwd = Encrypter.encrypt(commandLine.get(1));
                    commandLine.set(1, passwd);
                    Response response = this.client.sendRequest(new Request<>(commandName, null, commandLine));
                    if (Boolean.parseBoolean(((ArrayList<String>) response.getData()).get(0))) {
                        this.login = login;
                        this.passwd = passwd;
                        this.terminal.writeLine("доступ получен");
                        this.loggedIn = true;
                    } else terminal.writeLine("доступ запрещен");
                } else if(command instanceof Exit)
                {
                    System.exit(1);
                } else if (command instanceof ExecuteScript) {
                    String filename = commandLine.get(0);
                    if (this.runningScripts.contains(filename))
                        break;
                    FileTerminal fileIO = new FileTerminal(filename);
                    this.runningScripts.add(filename);
                    CommandSender commandSender = new CommandSender(fileIO, this.runningScripts, this.client);
                    commandSender.addCommandArray(this.getCommandArray());
                    commandSender.start();
                    this.runningScripts.remove(filename);
                }  else {
                    City city = null;
                    if (command.getNeedObject()) {
                        city = InteractiveCityParser.parseCity(this.terminal);
                        Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));
                        city.setCreationDate(ZonedDateTime.now(clock));
                        city.setOwnerLogin(this.login);
                    }
                    Response response = this.client.sendRequest(new Request<>(commandName, city, commandLine, this.login, this.passwd));
                    if(response.getError()!=null)
                        this.terminal.writeLine(response.getError());
                    else
                        this.terminal.writeResponse((ArrayList<String>) response.getData());
                }
            } catch (CommandDoesntExistException e) {
                this.terminal.writeLine("такой команды не существует");
            } catch (NullPointerException e) {
                this.terminal.writeLine("команда возвращает null набор строк");
            } catch (CommandException e) {
                this.terminal.writeLine(e.getMessage());
            } catch (NoSuchElementException e) {
                return;
            } catch (Exception e) {
                terminal.writeLine(e.getMessage() + "\n" + e.getClass());
            }

        }
    }


    public void addCommand(Command command) {
        this.commandArray.put(command.getName(), command);
    }

    private Command getCommand(Object name) throws CommandDoesntExistException {

        Command command = this.commandArray.get((String) name);
        if (command == null) throw new CommandDoesntExistException();
        return command;

    }

    private void getToLogIn() {
        this.terminal.writeLine("пожалуйста, залогиньтесь используя команду authorise {login} {password}, или register {login} {password}");
        while (!this.loggedIn) {
            try {
                ArrayList<String> commandLine = new ArrayList<>(List.of(this.terminal.readLine().split(" +")));
                String commandName = commandLine.get(0);
                Command command = this.getCommand(commandName);
                commandLine.remove(0);
                if (command instanceof Authorise) {
                    String login = commandLine.get(0);
                    String passwd = Encrypter.encrypt(commandLine.get(1));
                    commandLine.set(1, passwd);
                    Response response = this.client.sendRequest(new Request<>(commandName, null, commandLine));
                    if (Boolean.parseBoolean(((ArrayList<String>) response.getData()).get(0))) {
                        this.login = login;
                        this.passwd = passwd;
                        this.terminal.writeLine("доступ получен");
                        this.loggedIn = true;
                    } else terminal.writeLine("доступ запрещен");
                } else if (command instanceof Register) {
                    String login = commandLine.get(0);
                    String passwd = Encrypter.encrypt(commandLine.get(1));
                    commandLine.set(1, passwd);
                    Response response = this.client.sendRequest(new Request<>(commandName, null, commandLine));
                    if (Boolean.parseBoolean(((ArrayList<String>) response.getData()).get(0))) {
                        this.login = login;
                        this.passwd = passwd;
                        this.terminal.writeLine("доступ получен");
                        this.loggedIn = true;
                    } else terminal.writeLine("доступ запрещен");
                } else {
                    this.terminal.writeLine("пожалуйста, залогиньтесь используя команду authorise {login} {password}, или register {login} {password}, вы не можете использовать другие команды");
                }
            } catch (CommandDoesntExistException e) {
                this.terminal.writeLine("такой команды не существует");
            } catch (IndexOutOfBoundsException e) {
                this.terminal.writeLine("не введен логин или пароль");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
