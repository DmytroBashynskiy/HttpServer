package net.dmytrobashynskiy.http_server;
import net.dmytrobashynskiy.http_server.server_utils.HttpParser;
import net.dmytrobashynskiy.http_server.server_utils.HttpResponder;
import net.dmytrobashynskiy.http_server.server_utils.processor.InputDTO;
import net.dmytrobashynskiy.http_server.server_utils.processor.InputProcessor;
import net.dmytrobashynskiy.http_util.Response;
import net.dmytrobashynskiy.http_util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class HttpServer implements Server {
    private List<User> users = new ArrayList<>();
    private boolean stopMark = false;


    private static final Logger logger
            = LoggerFactory.getLogger("DmytroServer");

    @Override
    public void run(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Listening to the port: {}", port);
            while (!stopMark) {
                try (Socket localSocket = serverSocket.accept()) {

                    logger.info("Connection established.");
//                    try (BufferedReader input = new BufferedReader(
//                            new InputStreamReader(localSocket.getInputStream(), StandardCharsets.US_ASCII));
//                         PrintWriter output = new PrintWriter(localSocket.getOutputStream())) {
//                        //array to pack the request into
//                        ArrayList<String> incomingRequest = new ArrayList<>();
//                        do {
//                            String inpStr = input.readLine();
//                            if (inpStr == null) {
//                                System.out.println("Incoming ping processed, reopening socket for actual data.");
//                                break;
//                            } else if (inpStr.equals("") || inpStr.equals("\r\n") || inpStr.equals("\n")) {
//                                incomingRequest.add("");
//                                continue;
//                            }
//                            incomingRequest.add(inpStr);
//                        } while (input.ready());
//                        if (incomingRequest.isEmpty()) continue;
//                        analyzeRequest(incomingRequest);//this is the way to the endpoints
//                        //output stream
//                        respondWith(output);
//                        System.out.println("Client disconnected!");
//                    }
                    try (InputStream inputStream = localSocket.getInputStream();
                         PrintWriter output = new PrintWriter(localSocket.getOutputStream())) {
                        InputProcessor inputProcessor = new InputProcessor();
                        //logger.debug("Reading input");
                        InputDTO input = inputProcessor.readInputStream(inputStream);
                        if (input.isPing()) {
                            continue;
                        }
                        //logger.debug("Analyzing input");
                        analyzeRequest(input.getInputRequest(), output);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void analyzeRequest(List<String> incomingRequest, PrintWriter output) {
        List<String> response = parseRequest(incomingRequest);//this is the way to the endpoints
        //output stream
        respondWith(response, output);
        logger.info("Client disconnected!");
    }

    private List<String> parseRequest(List<String> inputRequest) {
        HttpParser parser = new HttpParser(inputRequest);
        HttpResponder responder = new HttpResponder(parser.parse(), users);
        Response response = responder.analyze();
        //update the user list
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

    @Override
    public void stop() {
        stopMark = true;
    }


}
