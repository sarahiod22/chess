package service;

import dataaccess.*;
import dataaccess.exceptions.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SystemUnitTests {
    private SystemService systemService;

    @BeforeEach
    public void setUp() {
        UserDao userDao = new MemUserDao();
        GameDao gameDao = new MemGameDao();
        AuthDao authDao = new MemAuthDao();
        systemService = new SystemService(userDao, authDao, gameDao);
    }

    @Test
    public void positiveClearTest() throws ResponseException {
        systemService.clear();
    }
}
