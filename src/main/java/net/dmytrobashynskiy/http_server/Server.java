package net.dmytrobashynskiy.http_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

interface Server {
    boolean stopMark = false;

    default void run(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening to the port: " + port);
            while (!stopMark) {
                try (Socket localSocket = serverSocket.accept()) {
                    System.out.println("Connection established.");
                    try (BufferedReader input = new BufferedReader(
                            new InputStreamReader(localSocket.getInputStream(), StandardCharsets.US_ASCII));
                         PrintWriter output = new PrintWriter(localSocket.getOutputStream())) {
                        //array to pack the request into
                        ArrayList<String> incomingRequest = new ArrayList<>();
                        do {
                            String inpStr = input.readLine();
                            if (inpStr == null) {
                                System.out.println("Incoming ping processed, reopening socket for actual data.");
                                break;
                            } else if (inpStr.equals("") || inpStr.equals("\r\n") || inpStr.equals("\n")) {
                                incomingRequest.add("");
                                continue;
                            }
                            incomingRequest.add(inpStr);
                        } while (input.ready());
                        if (incomingRequest.isEmpty()) continue;
                        analyzeRequest(incomingRequest);//this is the way to the endpoints
                        //output stream
                        respondWith(output);
                        System.out.println("Client disconnected!");
                    }
                }
            }
        }
    }

    void analyzeRequest(List<String> inputRequest);

    void respondWith(PrintWriter output);
}
