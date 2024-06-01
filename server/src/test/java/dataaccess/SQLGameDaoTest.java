package dataaccess;

import dataaccess.exceptions.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDaoTest {
    private final SQLGameDao gameDao = new SQLGameDao();
    GameData validGameData = new GameData(0, "white0", "black0", "game0", null);
    GameData invalidGameData = new GameData(-1, null, null, null, null);

    @BeforeEach
    void setUp() throws ResponseException {
        gameDao.clear();
    }

    @AfterEach
    void tearDown() throws ResponseException {
        gameDao.clear();
    }

    @Test
    void positiveCreateGameTest() throws ResponseException {
        gameDao.createGame(validGameData);
    }

    @Test
    void negativeCreateGameTest(){
        ResponseException e = Assertions.assertThrows(ResponseException.class, () -> gameDao.createGame(invalidGameData));
        Assertions.assertEquals(500, e.getStatusCode());
    }

    @Test
    void positiveGetGameTest() throws ResponseException {
        int gameId = gameDao.createGame(validGameData);
        Assertions.assertNotNull(gameDao.getGame((gameId)));
    }

    @Test
    void negativeGetGameTest() throws ResponseException {
        //game does not exist, so should return null
        Assertions.assertNull(gameDao.getGame(validGameData.gameID()));
    }

    @Test
    void positiveListGamesTest() throws ResponseException {
        gameDao.createGame(validGameData);
        Collection<GameData> games = gameDao.listGames();
        Assertions.assertNotNull(games);
    }

    @Test
    void negativeListGamesTest() throws ResponseException {
        //making sure that when there's no games in the database, it returns the correct empty result
        Collection<GameData>games = gameDao.listGames();
        Assertions.assertEquals(games, new ArrayList<>());
    }

    @Test
    void positiveUpdateGameTest() throws ResponseException {
        int gameId = gameDao.createGame(validGameData);
        GameData updatedGameData = new GameData(gameId, "white1", "black1", "game1", null);
        gameDao.updateGame(updatedGameData);
        GameData retrievedGame = gameDao.getGame(gameId);
        Assertions.assertEquals(updatedGameData.whiteUsername(), retrievedGame.whiteUsername());
        Assertions.assertEquals(updatedGameData.blackUsername(), retrievedGame.blackUsername());
        Assertions.assertEquals(updatedGameData.gameName(), retrievedGame.gameName());
    }

    @Test
    void negativeUpdateGameTest() throws ResponseException {
        Assertions.assertThrows(Exception.class, () -> gameDao.updateGame(null));
    }

    @Test
    void positiveClearTest() throws ResponseException {
        gameDao.createGame(validGameData);
        gameDao.clear();
    }
}
