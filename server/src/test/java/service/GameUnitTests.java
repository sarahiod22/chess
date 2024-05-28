package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class GameUnitTests {
    private GameService gameService;
    private UserService userService;
    UserData userData = new UserData("sarahiod22", "password", "sarahi@gmail.com");
    GameData validGameData = new GameData(1, null, null, "game0", new ChessGame());

    @BeforeEach
    void setUp() {
        UserDao userDao = new MemUserDao();
        GameDao gameDao = new MemGameDao();
        AuthDao authDao = new MemAuthDao();
        gameService = new GameService(gameDao, authDao);
        userService = new UserService(userDao, authDao);
    }

    @Test
    public void positiveCreateGameTest() throws ResponseException {
        AuthData authData = userService.register(userData);
        Integer gameId = gameService.createGame(authData.authToken(), validGameData);
        Assertions.assertNotNull(gameId);
    }

    @Test
    public void negativeCreateGameTest() {
        Assertions.assertThrows(ResponseException.class, () -> gameService.createGame(null, validGameData));
    }

    @Test
    public void positiveListGamesTest() throws ResponseException {
        AuthData authData = userService.register(userData);
        gameService.createGame(authData.authToken(), validGameData);
        Collection<GameData> games = gameService.listGames(authData.authToken());
        Assertions.assertNotNull(games);
    }

    @Test
    public void negativeListGamesTest() {
        Assertions.assertThrows(ResponseException.class, () -> gameService.listGames(null));
    }

    @Test
    public void positiveJoinGameTest() throws ResponseException {
        AuthData authData = userService.register(userData);
        Integer gameId = gameService.createGame(authData.authToken(), validGameData);
        gameService.joinGame(authData.authToken(), "BLACK", gameId);
    }

    @Test
    public void negativeJoinGameTest() {
        Assertions.assertThrows(ResponseException.class, () -> gameService.joinGame(null, "WHITE", 20));
    }
}
