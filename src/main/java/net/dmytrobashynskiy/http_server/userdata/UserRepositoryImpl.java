package net.dmytrobashynskiy.http_server.userdata;

import net.dmytrobashynskiy.http_server.server_utils.http_util.User;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public boolean isUserPresent(String name) {
        return false;
    }

    @Override
    public boolean saveNewUser() {
        return false;
    }

    @Override
    public boolean overwriteUser() {
        return false;
    }

    @Override
    public boolean deleteUser() {
        return false;
    }

    @Override
    public List<User> getUsers() {
        return null;
    }
}
