package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.UserData;

public interface UserDao {
    void createUser(UserData newUser) throws ResponseException;
    UserData getUser(String username) throws ResponseException;
    void clear() throws ResponseException;

}
