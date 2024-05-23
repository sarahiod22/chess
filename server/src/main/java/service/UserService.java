package service;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;

public class UserService {
    private UserDao userDao;
    private AuthDao authDao;

    public UserService(UserDao userDao , AuthDao authDao){
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public AuthData register(UserData user) throws DataAccessException, IllegalArgumentException {
        if (user == null || user.username() == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty() || user.email() == null || user.email().isEmpty()){
            throw new IllegalArgumentException("Invalid data provided");
        }

        try {
            userDao.createUser(user);
            return authDao.createAuth(user.username()); //maybe outside?
        } catch (Exception e){
            throw new DataAccessException("Failed to create user");
        }
    }

    public AuthData login(UserData user) throws DataAccessException, IllegalArgumentException {
        if (user == null || user.username() == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty()){
            throw new IllegalArgumentException("Not enough data provided");
        }
        UserData currentUser = userDao.getUser(user.username());

        if (currentUser == null || !currentUser.password().equals(user.password())){
            throw new IllegalArgumentException("Wrong user or password");
        }

        try {
            return authDao.createAuth(user.username());
        } catch (Exception e){
            throw new DataAccessException("Failed to login");
        }
    }

    public void logout(String authToken) throws DataAccessException, IllegalArgumentException {
        if (authToken == null || authToken.isEmpty()) {
            throw new IllegalArgumentException("Invalid auth token");
        }
        try {
            authDao.deleteAuth(authToken);
        }catch (Exception e){
            throw new DataAccessException("Failed to logout");
        }

    }

}
