package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.GameData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static dataaccess.DatabaseManager.configureDatabase;

public class SQLGameDao implements GameDao{

    private static final String[] createTableStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
                `gameId` int UNSIGNED NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(255) NULL,
                `blackUsername` varchar(255) NULL,
                `gameName` varchar(255) NOT NULL,
                `game` BLOB NULL,
                PRIMARY KEY (`gameId`)
            )"""
    };

    public SQLGameDao() {
        try {
            configureDatabase(createTableStatements);
        } catch (DataAccessException e){
            throw new RuntimeException("Unable to create game's table", e);
        }
    }

    @Override
    public int createGame(GameData newGame) throws ResponseException {
        try {
            String insertStatement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            return DatabaseManager.executeUpdate(insertStatement, newGame.whiteUsername(), newGame.blackUsername(), newGame.gameName(), newGame.game());
        }catch (DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameId) throws ResponseException {
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement( "SELECT * FROM gameData WHERE gameId = ?")){
            stmt.setInt(1, gameId);
            var rs = stmt.executeQuery();
            if(rs.next()){
                ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(rs.getInt("gameId"), rs.getString("whiteUsername"),rs.getString("blackUsername"), rs.getString("gameName"), chessGame);
            } else {
                return null;
            }
        }catch (SQLException | DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() throws ResponseException {
        Collection<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement( "SELECT * FROM gameData")){
            var rs = stmt.executeQuery();
            while (rs.next()) {
                ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                games.add(new GameData(rs.getInt("gameId"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), chessGame));
            }
            return games;
        } catch (SQLException | DataAccessException e){
        throw new ResponseException(500, "Error: " + e.getMessage());
    }
    }

    @Override
    public void updateGame(GameData game) throws ResponseException {
        try {
            DatabaseManager.executeUpdate("UPDATE gameData SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameId = ?", game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), game.gameID());
        } catch (DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws ResponseException {
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement("DELETE FROM gameData")) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
}
