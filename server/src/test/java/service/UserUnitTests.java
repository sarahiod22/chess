package service;

import dataaccess.*;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserUnitTests {
    private UserService userService;
    UserData validUserData = new UserData("sarahiod22", "password", "sarahi@gmail.com");
    UserData invalidUserData = new UserData(null, "password", null);

    @BeforeEach
    public void setUp() throws ResponseException {
        UserDao userDao = new SQLUserDao();
        userDao.clear();
        AuthDao authDao = new MemAuthDao();
        userService = new UserService(userDao, authDao);
    }

    @Test
    public void positiveRegisterTest() throws ResponseException{
            userService.register(validUserData);
            Assertions.assertNotNull(validUserData);
    }

    @Test
    public void negativeRegisterTest(){
        Assertions.assertThrows(ResponseException.class, () -> userService.register(invalidUserData));
    }

    @Test
    public void positiveLoginTest() throws ResponseException{
        userService.register(validUserData);
        var authData = userService.login(validUserData);
        Assertions.assertNotNull(authData);
    }

    @Test
    public void negativeLoginTest(){
        Assertions.assertThrows(ResponseException.class, () -> userService.login(invalidUserData));
    }

    @Test
    public void positiveLogoutTest() throws ResponseException{
        AuthData authData = userService.register(validUserData);
        String authToken = authData.authToken();
        userService.logout(authToken);
    }

    @Test
    public void negativeLogoutTest(){
        Assertions.assertThrows(ResponseException.class, () -> userService.logout(null));
    }
}
