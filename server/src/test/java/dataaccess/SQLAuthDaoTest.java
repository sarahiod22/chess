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
        AuthData auth1 = authDao.createAuth(validAuthData.username());
        AuthData auth2 = authDao.getAuth(auth1.authToken());
        Assertions.assertNotNull(auth2);
    }

    @Test
    void negativeGetAuthTest() throws ResponseException {
        //user does not exist, so should return null
        Assertions.assertNull(authDao.getAuth(validAuthData.authToken()));
    }

    @Test
    void positiveDeleteAuthTest() throws ResponseException {
        AuthData auth = authDao.createAuth(validAuthData.username());
        authDao.deleteAuth(auth.authToken());
        Assertions.assertNull(authDao.getAuth(auth.authToken()));
    }

    @Test
    void negativeDeleteAuthTest() {
        Assertions.assertDoesNotThrow(() -> authDao.deleteAuth(invalidAuthData.authToken()));
    }

    @Test
    void positiveClearTest() throws ResponseException {
        authDao.createAuth(validAuthData.username());
        authDao.clear();
    }

}
