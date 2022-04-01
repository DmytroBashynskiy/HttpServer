package net.dmytrobashynskiy.http_server.server_utils.http_util;

public class User {
    private String name = null;
    private String email = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User has name: {" +
                name + "} and email {" + email + '}';
    }
}
