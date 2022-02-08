package net.dmytrobashynskiy.config_util;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigReader {
    Properties properties;
    public ConfigReader() throws IOException {
        properties = new Properties();
    }
    public int getPort(){
        try(FileReader fileReader = new FileReader("serverConfig/config.properties", StandardCharsets.UTF_8)){
            properties.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(properties.getProperty("port"));
    }
}
