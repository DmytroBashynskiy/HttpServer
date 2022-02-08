package net.dmytrobashynskiy;

import net.dmytrobashynskiy.config_util.ConfigReader;
import net.dmytrobashynskiy.http_server.HttpServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer();
        ConfigReader configReader = new ConfigReader();
        server.run(configReader.getPort());
    }
}
