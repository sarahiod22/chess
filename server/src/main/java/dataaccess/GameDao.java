package dataaccess;

import model.GameData;
import java.util.Collection;

public interface GameDao {
    int createGame(GameData newGame) throws DataAccessException;
    GameData getGame(int gameId) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException; // not sure about parameter?
    void clear() throws DataAccessException;
}
