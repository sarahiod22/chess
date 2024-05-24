package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.AuthData;

public interface AuthDao {
    AuthData createAuth(String username) throws ResponseException;
    AuthData getAuth(String authToken) throws ResponseException;
    void deleteAuth(String authToken) throws ResponseException;
    void clear() throws ResponseException;
}
