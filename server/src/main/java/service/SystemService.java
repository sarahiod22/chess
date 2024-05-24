package service;

import dataaccess.AuthDao;
import dataaccess.GameDao;
import dataaccess.UserDao;
import dataaccess.exceptions.ResponseException;

public class SystemService {
    private UserDao userDao;
    private AuthDao authDao;
    private GameDao gameDao;

    public SystemService(UserDao userDao , AuthDao authDao, GameDao gameDao){
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }
    public void clear() throws ResponseException {
        try {
            userDao.clear();
            authDao.clear();
            gameDao.clear();
        } catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
}
