package app;

import cli.Command;
import server.Server;
import storage.ProxyStorage;
import storage.db.DBStorageManager;
import storage.Storage;
import storage.db.DBUserManager;
import сommands.CommandArrayFiller;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class AppServer {
    public static void run() {
        Storage storage = null;
        DBStorageManager dbStorageManager = null;
        DBUserManager dbUserManager = null;
        try {
            Properties info = new Properties();
            info.load(new FileReader("src/main/java/db.cfg"));
            Connection connectionUsers = DriverManager.getConnection("jdbc:postgresql://db:5432/studs", info);
            Connection connectionStorage = DriverManager.getConnection("jdbc:postgresql://db:5432/studs", info);
            dbStorageManager = new DBStorageManager(connectionStorage);
            dbStorageManager.createTableIfNeeded();
            dbUserManager = new DBUserManager(connectionUsers);
            dbUserManager.createTableIfNeeded();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
        ProxyStorage proxyStorage = new ProxyStorage(dbStorageManager, dbUserManager);
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
