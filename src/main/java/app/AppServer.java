package app;

import cli.Command;
import server.Server;
import storage.ProxyStorage;
import storage.db.DBStorageManager;
import storage.Storage;
import сommands.CommandArrayFiller;

import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class AppServer {
    public static void run() {
        Storage storage = null;
        DBStorageManager dbStorageManager = null;
        try {
            Properties info = new Properties();
            info.load(new FileReader("src/main/java/db.cfg"));
            dbStorageManager = new DBStorageManager(DriverManager.getConnection("jdbc:postgresql://db:5432/studs", info));
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
        ProxyStorage proxyStorage = new ProxyStorage(dbStorageManager);
        try {
            proxyStorage.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*DataAccessLayer dataAccessLayer = new DataAccessLayer(System.getenv("SAVEFILE"));
        storage = new Storage(dataAccessLayer);*/
        String host = System.getenv("SERVER_HOST");
        int port = Integer.parseInt(System.getenv("SERVER_PORT"));
        CommandArrayFiller.setBasicCommands();
        HashMap<String, Command> commandMap = CommandArrayFiller.getCommandMap();
        Server server = new Server(host, port, proxyStorage, commandMap);
        server.handle();
    }
}
