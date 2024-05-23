package service;

import chess.ChessGame;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {

    private GameDao gameDao;
    private AuthDao authDao;

    public GameService(GameDao gameDao , AuthDao authDao){
        this.gameDao = gameDao;
        this.authDao = authDao;
    }
    public Collection<GameData> listGames(String authToken) throws IllegalArgumentException, DataAccessException {
        if (authToken == null || authToken.isEmpty() || authDao.getAuth(authToken) == null ) {
            throw new IllegalArgumentException("Invalid auth token");
        }
        try{
            return gameDao.listGames();
        } catch (Exception e) {
            throw new DataAccessException("Failed to list games");
        }
    }

    public Integer createGame(String authToken, GameData game) throws DataAccessException {
        if (authToken == null || authToken.isEmpty() || authDao.getAuth(authToken) == null || game == null || game.gameName() == null || game.gameName().isEmpty()) {
            throw new IllegalArgumentException("Invalid auth token or game name empty");
        }
        try {
            return gameDao.createGame(game);
        } catch (Exception e) {
            throw new DataAccessException("Failed to create game");
        }
    }

    public void joinGame(String authToken, String playerColor, int gameId) throws DataAccessException {
        AuthData auth = authDao.getAuth(authToken);
        GameData game = gameDao.getGame(gameId);

        if (authToken == null || authToken.isEmpty() || auth == null) {
            throw new DataAccessException("Invalid auth token");
        }
        if (playerColor == null || playerColor.isEmpty() || gameId < -1 || game == null){
            throw new DataAccessException("Invalid game");
        }
        if (playerColor.equals("WHITE") && game.whiteUsername() != null){
            throw new DataAccessException("White username already exists");
        }
        if (playerColor.equals("BLACK") && game.blackUsername() != null){
            throw new DataAccessException("Black username already exists");
        }
        try {
            if (playerColor.equals("WHITE")){
                gameDao.updateGame(new GameData(gameId, auth.username(), game.blackUsername(), game.gameName(), game.game()));
            } else if (playerColor.equals("BLACK")) {
                gameDao.updateGame(new GameData(gameId, game.whiteUsername(), auth.username(), game.gameName(), game.game()));
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed to update game");
        }

    }

}
