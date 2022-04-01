package net.dmytrobashynskiy.http_server.core;

import net.dmytrobashynskiy.http_server.server_utils.HttpParser;
import net.dmytrobashynskiy.http_server.server_utils.HttpResponder;
import net.dmytrobashynskiy.http_server.server_utils.http_util.Response;
import net.dmytrobashynskiy.http_server.server_utils.http_util.User;
import net.dmytrobashynskiy.http_server.server_utils.processor.InputDTO;
import net.dmytrobashynskiy.http_server.server_utils.processor.InputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ConnectionProcessorThread extends Thread{
    private Socket localSocket;
    private List<User> users;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ConnectionProcessorThread.class);

    public ConnectionProcessorThread (Socket localSocket, List<User> users){
        this.localSocket = localSocket;
        this.users = users;
    }

    @Override
    public void run() {
        try (InputStream inputStream = localSocket.getInputStream();
             PrintWriter output = new PrintWriter(localSocket.getOutputStream())) {
            InputProcessor inputProcessor = new InputProcessor();
            //logger.debug("Reading input");
            InputDTO input = inputProcessor.readInputStream(inputStream);
            if (!input.isPing()) {
                analyzeRequest(input.getInputRequest(), output);
            }
            //logger.debug("Analyzing input");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void analyzeRequest(List<String> incomingRequest, PrintWriter output) {
        List<String> response = parseRequest(incomingRequest);//this is the way to the endpoints
        //output stream
        respondWith(response, output);
        LOGGER.info("Client disconnected!");
    }
    private List<String> parseRequest(List<String> inputRequest) {
        HttpParser parser = new HttpParser(inputRequest);
        HttpResponder responder = new HttpResponder(parser.parse(), users);
        Response response = responder.analyze();
        //update the user list

        //TODO needs some global user storage, independent of anything
        users = response.getUsers();
        //response text array and code
        return response.getResponseLines();
    }


    private void respondWith(List<String> response, PrintWriter output) {
        for (String responseLine : response) {
            output.println(responseLine);
        }
        output.flush();
    }
}
