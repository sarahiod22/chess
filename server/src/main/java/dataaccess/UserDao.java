package dataaccess;

import model.UserData;
import java.util.HashMap;

public class UserDao implements IntUserDao {
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData newUser) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
