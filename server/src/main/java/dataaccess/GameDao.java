package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.GameData;
import java.util.Collection;

public interface GameDao {
    int createGame(GameData newGame) throws ResponseException;
    GameData getGame(int gameId) throws ResponseException;
    Collection<GameData> listGames();
    void updateGame(GameData game) throws ResponseException;
    void clear() throws ResponseException;
}
