package cli;

import api.Request;
import api.Response;
import cli.commandExceptions.CommandDoesntExistException;
import cli.commandExceptions.CommandException;
import client.Client;
import client.InteractiveCityParser;
import storage.objects.City;
import сommands.Authorise;
import сommands.ExecuteScript;
import сommands.Exit;
import сommands.Register;

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
        while (true) {
            try {
                ArrayList<String> commandLine = new ArrayList<>(List.of(this.terminal.readLine().split(" +")));
                String commandName = commandLine.get(0);
                Command command = this.getCommand(commandName);
                if(command instanceof Exit)
                {
                    System.exit(1);
                } else if (command instanceof ExecuteScript) {
                    String filename = commandLine.get(1);
                    if (this.runningScripts.contains(filename))
                        break;
                    FileTerminal fileIO = new FileTerminal(filename);
                    this.runningScripts.add(filename);
                    CommandSender commandSender = new CommandSender(fileIO, this.runningScripts, this.client);
                    commandSender.addCommandArray(this.getCommandArray());
                    commandSender.start();
                    this.runningScripts.remove(filename);
                } else if(command instanceof Authorise) {
                    String login = commandLine.get(0);
                    String passwd = commandLine.get(1);
                    Response response = this.client.sendRequest(new Request<>(commandName, null, commandLine));
                    if(Boolean.parseBoolean(((ArrayList<String>) response.getData()).get(0))) {
                        this.login = login;
                        this.passwd = passwd;
                        this.terminal.writeLine("доступ получен");
                    }
                    else terminal.writeLine("доступ запрещен");
                }else if(command instanceof Register) {
                    this.login = commandLine.get(0);
                    this.passwd = commandLine.get(1);
                } else {
                    City city = null;
                    if (command.getNeedObject()) {
                        city = InteractiveCityParser.parseCity(this.terminal);
                        Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));
                        city.setCreationDate(ZonedDateTime.now(clock));
                        city.setOwnerLogin(this.login);
                    }
                    commandLine.remove(0);
                    Response response = this.client.sendRequest(new Request<>(commandName, city, commandLine));
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

    /*public ArrayList<String> help() {
        ArrayList<String> response = new ArrayList<>();
        for (Command command : this.commandArray.values()) {
            response.add(command.getDescription());
        }
        return response;
    }*/
}
