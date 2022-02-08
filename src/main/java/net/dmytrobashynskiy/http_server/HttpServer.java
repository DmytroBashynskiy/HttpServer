package net.dmytrobashynskiy.http_server;


import net.dmytrobashynskiy.http_util.HttpParser;
import net.dmytrobashynskiy.http_util.HttpResponder;
import net.dmytrobashynskiy.http_util.Response;
import net.dmytrobashynskiy.http_util.User;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class HttpServer implements Server{
    private List<User> users = new ArrayList<>();
    List<String> responseText;
    @Override
    public void analyzeRequest(List<String> inputRequest) {
        HttpParser parser = new HttpParser(inputRequest);
        HttpResponder responder = new HttpResponder(parser.parse(), users);
        Response response = responder.analyze();
        //update the user list
        users = response.getUsers();
        //response text array and code
        responseText = response.getResponseLines();
    }
    @Override
    public void respondWith(PrintWriter output) {
        for (String responseLine: responseText){
            output.println(responseLine);
        }
        output.flush();
    }
}
