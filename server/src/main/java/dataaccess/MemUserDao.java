package dataaccess;

import model.UserData;
import java.util.HashMap;

public class MemUserDao implements UserDao {
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData newUser) throws DataAccessException {
        if (newUser.username() == null || newUser.username().isEmpty()) {
            throw new DataAccessException("Username not provided");
        }
        if(users.containsKey(newUser.username())) {
            throw new DataAccessException("User already exists");
        }
        users.put(newUser.username(), newUser);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try {
            return users.get(username);
        }catch(Exception e) {
            throw new DataAccessException("User not found");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try {
            users.clear();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
