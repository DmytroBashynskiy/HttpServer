package net.dmytrobashynskiy.http_server.server_utils.http_util;


import java.util.List;

public class Response {


    private List<String> responseLines;

    private List<User> users;

    public Response(List<User> users) {
        this.users = users;
    }

    public void setResponseLines(List<String> responseLines) {
        this.responseLines = responseLines;
    }

    public List<String> getResponseLines() {
        return responseLines;
    }

    public List<User> getUsers() {
        return users;
    }


}
