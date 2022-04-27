package net.dmytrobashynskiy.http_server.userdata;

import net.dmytrobashynskiy.http_server.server_utils.http_util.User;

import java.util.List;

public interface UserRepository {

    boolean isUserPresent(String name);
    boolean saveNewUser();
    boolean overwriteUser();
    boolean deleteUser();

    List<User> getUsers();

}
