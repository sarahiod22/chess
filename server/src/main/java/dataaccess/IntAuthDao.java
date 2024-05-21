package dataaccess;

import model.AuthData;
import model.UserData;

public interface IntAuthDao {
    void createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
}
