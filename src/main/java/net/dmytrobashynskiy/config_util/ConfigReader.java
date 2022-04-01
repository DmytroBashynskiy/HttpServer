package net.dmytrobashynskiy.config_util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;
    private static ConfigReader configReader;

    private ConfigReader() {
        properties = new Properties();
    }

    public static ConfigReader getInstance(){
        if(configReader==null){
            return new ConfigReader();
        }
        return configReader;
    }

    public int getPort() {
        try (FileReader fileReader = new FileReader("serverConfig/config.properties", StandardCharsets.UTF_8)) {
            properties.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(properties.getProperty("port"));
    }
}
