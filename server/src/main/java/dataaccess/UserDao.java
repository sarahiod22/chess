package dataaccess;

import model.UserData;

public interface UserDao {
    void createUser(UserData newUser) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;

}
