package dataaccess;

import dataaccess.exceptions.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;

public class SQLAuthDaoTest {
    private final SQLAuthDao authDao = new SQLAuthDao();
    AuthData validAuthData = new AuthData("token", "sarahiod22");
    AuthData invalidAuthData = new AuthData(null, null);

    @BeforeEach
    void setUp() throws ResponseException {
        authDao.clear();
    }

    @AfterEach
    void tearDown() throws ResponseException {
        authDao.clear();
    }

    @Test
    void positiveCreateAuthTest() throws ResponseException {
        authDao.createAuth(validAuthData.username());
    }

    @Test
    void negativeCreateAuthTest() {
        ResponseException e = Assertions.assertThrows(ResponseException.class, () -> authDao.createAuth(invalidAuthData.username()));
        Assertions.assertEquals(500, e.getStatusCode());
    }

    @Test
    void positiveGetAuthTest() throws ResponseException {
        authDao.createAuth(validAuthData.username());
        AuthData auth = authDao.getAuth(validAuthData.authToken());
        Assertions.assertNotNull(auth);
    }

    @Test
    void negativeGetAuthTest() throws ResponseException {
        //user does not exist, so should return null
        Assertions.assertNull(authDao.getAuth(validAuthData.authToken()));
    }

    @Test
    void positiveClearTest() throws ResponseException {
        authDao.createAuth(validAuthData.username());
        authDao.clear();
    }

}
