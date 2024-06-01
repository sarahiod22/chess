package dataaccess;

import dataaccess.exceptions.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;

public class SQLUserDaoTest {
    private final SQLUserDao userDao = new SQLUserDao();
    UserData validUserData = new UserData("sarahiod22", "password", "sarahi@gmail.com");
    UserData invalidUserData = new UserData(null, "password", "sarahi@gmail.com");

    @BeforeEach
    void setUp() throws ResponseException {
        userDao.clear();
    }

    @AfterEach
    void tearDown() throws ResponseException {
        userDao.clear();
    }

    @Test
    void positiveCreateUserTest() throws ResponseException {
        userDao.createUser(validUserData);
    }

    @Test
    void negativeCreateUserTest() throws ResponseException {
        try {
            userDao.createUser(invalidUserData);
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.getStatusCode());
        }
    }

    @Test
    void positiveGetUserTest() throws ResponseException {
        userDao.createUser(validUserData);
        UserData user = userDao.getUser(validUserData.username());
        Assertions.assertNotNull(user);
    }

    @Test
    void negativeGetUserTest() throws ResponseException {
        try {
            userDao.getUser(invalidUserData.username());
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.getStatusCode());
        }
    }

    @Test
    void positiveClearTest() throws ResponseException {
        userDao.createUser(validUserData);
        userDao.clear();
    }

}
