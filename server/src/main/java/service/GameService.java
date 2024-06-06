package service;

import dataaccess.AuthDao;
import dataaccess.exceptions.DataAccessException;
import dataaccess.GameDao;
import dataaccess.exceptions.ResponseException;
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
    public Collection<GameData> listGames(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty() || authDao.getAuth(authToken) == null ) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        try{
            return gameDao.listGames();
        } catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public Integer createGame(String authToken, GameData game) throws ResponseException {
        if (authToken == null || authToken.isEmpty() || authDao.getAuth(authToken) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (game == null || game.gameName() == null || game.gameName().isEmpty()){
            throw new ResponseException(400, "Error: bad request");
        }
        try {
            return gameDao.createGame(game);
        } catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public void joinGame(String authToken, String playerColor, int gameId) throws ResponseException {
        AuthData auth = authDao.getAuth(authToken);
        GameData game = gameDao.getGame(gameId);

        if (authToken == null || authToken.isEmpty() || auth == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (playerColor == null || playerColor.isEmpty() || gameId < -1 || game == null){
            throw new ResponseException(400, "Error: bad request");
        }
        if (playerColor.equals("WHITE") && game.whiteUsername() != null){
            throw new ResponseException(403, "Error: already taken");
        }
        if (playerColor.equals("BLACK") && game.blackUsername() != null){
            throw new ResponseException(403, "Error: already taken");
        }
        try {
            if (playerColor.equals("WHITE")){
                gameDao.updateGame(new GameData(gameId, auth.username(), game.blackUsername(), game.gameName(), game.game()));
            } else if (playerColor.equals("BLACK")) {
                gameDao.updateGame(new GameData(gameId, game.whiteUsername(), auth.username(), game.gameName(), game.game()));
            } else if (playerColor.equals("observer")) {
                return;
            }
        } catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }

    }

}
