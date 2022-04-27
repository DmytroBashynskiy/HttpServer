package net.dmytrobashynskiy;

import net.dmytrobashynskiy.config_util.ConfigReader;
import net.dmytrobashynskiy.http_server.HttpServer;
import net.dmytrobashynskiy.http_server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new HttpServer();
        server.run();
    }
}
