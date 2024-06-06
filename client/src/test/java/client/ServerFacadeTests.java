package client;

import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    UserData validUser = new UserData("player1", "password", "p1@email.com");
    UserData invalidUser = new UserData(null, "password", "p2@email.com");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws Exception {
        serverFacade.clear();
    }

    @Test
    void positiveRegister() throws Exception {
        var authData = serverFacade.register(validUser);
        Assertions.assertNotNull(authData);
    }

    @Test
    void negativeRegister() throws Exception {
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(invalidUser));

    }

    @Test
    void positiveLogin() throws Exception {
        serverFacade.register(validUser);
        var authData = serverFacade.login(validUser);
        Assertions.assertNotNull(authData);
    }

    @Test
    void negativeLogin() throws Exception {
        //user is not registered -> fail
        Assertions.assertThrows(Exception.class, () -> serverFacade.login(validUser));
    }

    @Test
    void positiveLogout() throws Exception {
        var authData = serverFacade.register(validUser);
        Assertions.assertDoesNotThrow(()-> serverFacade.logout(authData.authToken()));
    }

    @Test
    void negativeLogout() throws Exception {
        Assertions.assertThrows(Exception.class, () -> serverFacade.logout(null));
    }

    @Test
    void positiveListGames() throws Exception {
        var authData = serverFacade.register(validUser);
        var games = serverFacade.listGames(authData.authToken());
    }



}
