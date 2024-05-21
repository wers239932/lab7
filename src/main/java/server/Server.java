package server;

import api.ProtocolInfo;
import api.Request;
import api.RequestStatus;
import api.Response;
import cli.Command;
import cli.commandExceptions.CommandException;
import storage.db.AuthException;
import storageInterface.StorageInterface;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Server {
    private final StorageInterface storage;
    public final static Duration timeout = Duration.ofMillis(50);
    private final Scanner scanner;
    private final Logger logger = Logger.getLogger("MyLog");
    private final HashMap<String, Command> commandMap;
    private final DatagramChannel datagramChannel;
    private final static ExecutorService readers = Executors.newFixedThreadPool(10);

    private final static ExecutorService handlers = Executors.newFixedThreadPool(10);

    private final static ForkJoinPool senders = ForkJoinPool.commonPool();

    public Server(String host, int port, StorageInterface storage, HashMap<String, Command> commandMap) {
        this.commandMap = commandMap;
        this.scanner = new Scanner(System.in);
        this.storage = storage;
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
            this.datagramChannel = DatagramChannel.open();
            this.datagramChannel.bind(socketAddress);
            this.datagramChannel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Logger logger = Logger.getLogger("MyLog");
        try {
            FileHandler fh = new FileHandler("server_logger.log", true);
            logger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
            logger.info("сервер создан");
        } catch (SecurityException | IOException e) {
            logger.log(Level.SEVERE, "Произошла ошибка при работе с FileHandler.", e);
        }
    }

    public void handle() {
        while (true) {
            int available;
            try {
                available = System.in.available();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (available > 0) {
                String command = scanner.nextLine();
                switch (command) {
                    case ("exit"): {
                        try {
                            storage.save();
                            System.out.println("коллекция сохранена");
                            logger.fine("коллекция сохранена");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("завершение работы");
                        logger.info("выключение сервера");
                        System.exit(0);
                    }
                    case ("save"): {
                        try {
                            storage.save();
                            System.out.println("коллекция сохранена");
                            logger.fine("коллекция сохранена");
                        } catch (IOException e) {
                            logger.severe("ошибка сохранения");
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else {
                ByteBuffer data = ByteBuffer.allocate(ProtocolInfo.messageSize);
                try {
                    SocketAddress clientAddress = this.datagramChannel.receive(data);
                    if (clientAddress != null) {
                        this.logger.info("получен пакет");
                        readers.submit(()-> this.tryToReadRequest(data, clientAddress));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void sendReply(Response object, DatagramChannel datagramChannel, SocketAddress address) {
        try{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        byte[] sendData = byteArrayOutputStream.toByteArray();
        int len = sendData.length;
        Random random = new Random();
        int id = random.nextInt();
        int dataSize = ProtocolInfo.messageSize - ProtocolInfo.headerSize;
        byte total = (byte) ((len + dataSize - 1) / dataSize);
        byte index = 0;
        while (index < total) {
            ByteArrayOutputStream part = new ByteArrayOutputStream();
            byte[] bytes = ByteBuffer.allocate(4).putInt(id).array();
            part.write(bytes);
            part.write(total);
            part.write(index);
            int end = (index + 1) * dataSize;
            if (end > len) end = len;
            part.write(Arrays.copyOfRange(sendData, index * dataSize, end));
            index++;
            datagramChannel.send(ByteBuffer.wrap(part.toByteArray()), address);
        }
        logger.info("отправлен ответ");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request readRequest(ByteBuffer data) {
        ByteArrayInputStream dataStream = new ByteArrayInputStream(data.array());
        Request request;
        try {
            ObjectInputStream objectStream = new ObjectInputStream(dataStream);
            request = (Request) objectStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("сообщение клиента не может быть прочитано");
            throw new RuntimeException(e);
        }
        logger.info("прочитан запрос");
        return request;
    }
    public void tryToReadRequest(ByteBuffer data, SocketAddress clientAddress){
        Request request = this.readRequest(data);
        if (request != null) {
            handlers.submit(()->this.handleRequest(request, clientAddress));
        }
    }
    public void handleRequest(Request request, SocketAddress clientAddress) {
        String commandName = request.getCommandName();
        Response response = null;
        switch (commandName) {
            case ("getCommands"): {
                ArrayList<Command> commands = new ArrayList(this.commandMap.values());
                response = new Response<>(commands, RequestStatus.DONE, null);
                break;
            }
            case ("help"): {
                ArrayList<String> output = new ArrayList<>();
                for (Command command : this.commandMap.values()) {
                    output.add(command.getDescription());
                    response = new Response<>(output, RequestStatus.DONE, null);
                }
                break;
            }
            default: {
                Command command = this.commandMap.get(commandName);
                ArrayList<String> output = null;
                try {
                    if(!this.storage.auth(request.getLogin(), request.getPasswd()))
                        throw new AuthException();
                    output = command.execute(request, this.storage);
                    response = new Response<>(output, RequestStatus.DONE, null);
                } catch (CommandException e) {
                    response = new Response<>(RequestStatus.FAILED, e.getMessage());
                    logger.severe("запрос клиента не выполнен, ошибка: " + e.getMessage());
                } catch (AuthException e) {
                    response = new Response<>(RequestStatus.FAILED, "данные пользователя неверны");
                }
                break;
            }
        }
        Response finalResponse = response;
        senders.submit(()->this.sendReply(finalResponse, this.datagramChannel, clientAddress));
    }
}
