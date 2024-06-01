package dataaccess;

import dataaccess.exceptions.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;

public class SQLUserDaoTest {
    private final SQLUserDao userDao = new SQLUserDao();
    UserData validUserData = new UserData("sarahiod22", "password", "sarahi@gmail.com");
    UserData invalidUserData = new UserData(null, null, "sarahi@gmail.com");

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
    void negativeCreateUserTest() {
        ResponseException e = Assertions.assertThrows(ResponseException.class, () -> userDao.createUser(invalidUserData));
        Assertions.assertEquals(500, e.getStatusCode());
    }

    @Test
    void positiveGetUserTest() throws ResponseException {
        userDao.createUser(validUserData);
        UserData user = userDao.getUser(validUserData.username());
        Assertions.assertNotNull(user);
    }

    @Test
    void negativeGetUserTest() throws ResponseException {
        //user does not exist, so should return null
        Assertions.assertNull(userDao.getUser(invalidUserData.username()));
    }

    @Test
    void positiveClearTest() throws ResponseException {
        userDao.createUser(validUserData);
        userDao.clear();
    }

}
