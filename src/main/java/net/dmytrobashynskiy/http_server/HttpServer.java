package net.dmytrobashynskiy.http_server;
import net.dmytrobashynskiy.config_util.ConfigReader;
import net.dmytrobashynskiy.http_server.core.ServerListenerThread;
import net.dmytrobashynskiy.http_server.server_utils.http_util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class HttpServer implements Server {
    //TODO change into User Repository
    private List<User> users = new ArrayList<>();
    private boolean stopMark = false;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(HttpServer.class);
    private ConfigReader configReader = ConfigReader.getInstance();

    @Override
    public void run(){
        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(configReader.getPort(), users);
            serverListenerThread.start();
        } catch (IOException e) {
            LOGGER.error("Error in listening thread: ", e);
        }
    }



    @Override
    public void stop() {
        stopMark = true;
    }


}
