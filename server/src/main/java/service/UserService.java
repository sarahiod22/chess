package service;

import dataaccess.AuthDao;
import dataaccess.UserDao;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private UserDao userDao;
    private AuthDao authDao;

    public UserService(UserDao userDao , AuthDao authDao){
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public AuthData register(UserData user) throws ResponseException {
        if (user == null || user.username() == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty() || user.email() == null || user.email().isEmpty()){
            throw new ResponseException(400, "Error: Bad Request");
        }

        UserData currentUser = userDao.getUser(user.username());
        if (currentUser != null){
            throw new ResponseException(403, "Error: already taken");
        }

        try {
            userDao.createUser(user);
            return authDao.createAuth(user.username());
        } catch (Exception e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public AuthData login(UserData user) throws ResponseException {
        if (user == null || user.username() == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty()){
            throw new ResponseException(401, "Error: unauthorized");
        }

        UserData currentUser = userDao.getUser(user.username());
        var hashedPassword = currentUser.password();

        if (currentUser == null || !BCrypt.checkpw(user.password(), hashedPassword)){
            throw new ResponseException(401, "Error: unauthorized");
        }

        try {
            return authDao.createAuth(user.username());
        } catch (Exception e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public void logout(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty() || authDao.getAuth(authToken) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        try {
            authDao.deleteAuth(authToken);
        }catch (Exception e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }

    }

}
