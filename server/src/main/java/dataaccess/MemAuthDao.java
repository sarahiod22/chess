package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemAuthDao implements AuthDao{
    private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null || username.isEmpty()) {
            throw new DataAccessException("Username cannot be null or empty");
        }
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        authTokens.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try {
            return authTokens.get(authToken);
        } catch (Exception e){
            throw new DataAccessException("Auth token not found");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (!authTokens.containsKey(authToken)) {
            throw new DataAccessException("Auth token not found");
        }
        try {
            authTokens.remove(authToken);
        } catch (Exception e){
            throw new DataAccessException("Auth token not found");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try {
            authTokens.clear();
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
