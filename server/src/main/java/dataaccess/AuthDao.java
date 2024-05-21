package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDao implements IntAuthDao{
    private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void createAuth(String username) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
