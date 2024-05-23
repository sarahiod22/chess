package service;

import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import dataaccess.UserDao;

public class SystemService {
    private UserDao userDao;
    private AuthDao authDao;
    private GameDao gameDao;

    public SystemService(UserDao userDao , AuthDao authDao, GameDao gameDao){
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }
    public void clear() throws DataAccessException {
        try {
            userDao.clear();
            authDao.clear();
            gameDao.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed to clear data");
        }
    }
}
