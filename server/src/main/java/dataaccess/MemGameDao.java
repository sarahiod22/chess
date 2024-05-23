package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemGameDao implements GameDao{
    private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public int createGame(GameData newGame) throws DataAccessException {
        int gameId = games.size()+1;
        GameData existingGame = new GameData(gameId, newGame.whiteUsername(), newGame.blackUsername(), newGame.gameName(), newGame.game());
        games.put(gameId, existingGame);
        return gameId;
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try {
            return games.get(gameId);
        }catch (Exception e){
            throw new DataAccessException("Game not found");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (games.containsKey(game.gameID())) {
            games.put(game.gameID(), game);
        } else {
            throw new DataAccessException("Game not found");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try {
            games.clear();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
