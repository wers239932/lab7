package app;

import dal.DataAccessLayer;
import storage.Storage;
import сommands.CommandArrayFiller;
import сommands.Save;

import java.util.ArrayList;

public class App {
    public static void run() {
        DataAccessLayer dataAccessLayer = new DataAccessLayer(System.getenv("SAVEFILE"));
        Storage storage = null;
        storage = new Storage(dataAccessLayer);
        ArrayList commandArray = CommandArrayFiller.setBasicCommands();
        commandArray.add(new Save());
        //CommandSender commandExecuter = new CommandSender(new Terminal());
        //commandExecuter.start();
    }
}
