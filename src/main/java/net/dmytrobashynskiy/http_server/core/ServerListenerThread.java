package net.dmytrobashynskiy.http_server.core;

import net.dmytrobashynskiy.http_server.server_utils.HttpParser;
import net.dmytrobashynskiy.http_server.server_utils.HttpResponder;
import net.dmytrobashynskiy.http_server.server_utils.http_util.User;
import net.dmytrobashynskiy.http_server.server_utils.processor.InputDTO;
import net.dmytrobashynskiy.http_server.server_utils.processor.InputProcessor;
import net.dmytrobashynskiy.http_server.server_utils.http_util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerListenerThread extends Thread{
    private int port;
    private ServerSocket serverSocket;
    private List<User> users;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ServerListenerThread.class);

    public ServerListenerThread(int port, List<User> users) throws IOException {
        this.port = port;
        this.users = users;
        serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        LOGGER.info("Listening to the port: {}", port);
        while (true) {
            try (Socket localSocket = serverSocket.accept()) {

                LOGGER.info("Connection established.");
                ConnectionProcessorThread connectionProcessorThread = new ConnectionProcessorThread(localSocket, users);
                connectionProcessorThread.run();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
