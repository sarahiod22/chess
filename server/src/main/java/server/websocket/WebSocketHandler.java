
package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.SQLAuthDao;
import dataaccess.SQLGameDao;
import dataaccess.SQLUserDao;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final SQLUserDao userDao = new SQLUserDao();
    private final SQLGameDao gameDao = new SQLGameDao();
    private final SQLAuthDao authDao = new SQLAuthDao();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(new Gson().fromJson(message, Connect.class), session);
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);
            case LEAVE -> leave(new Gson().fromJson(message, Leave.class), session);
            case RESIGN -> resign(new Gson().fromJson(message, Resign.class),session);
        }
    }


    private void connect(Connect connect, Session session) throws IOException, ResponseException {
        try {
            AuthData authData = authDao.getAuth(connect.getAuthString());
            GameData gameData = gameDao.getGame(connect.gameId);
            connections.add(connect.getAuthString(), session, connect.gameId);
            //Server sends a LOAD_GAME message back to the root client
            LoadGame loadGame = new LoadGame(gameData);
            connections.connections.get(connect.getAuthString()).send(loadGame.toString());
            //Server sends a Notification message to all other clients in that game informing them the root client connected
            if (connect.observer){
                Notification notification = new Notification(authData.username() + " joined the game as observer");
                connections.broadcast(connect.getAuthString(), notification, connect.gameId);
            }else {
                Notification notification = new Notification(authData.username() + " joined the game as " + connect.teamColor.toString());
                connections.broadcast(connect.getAuthString(), notification, connect.gameId);
            }
        }
        catch (ResponseException | IOException e) {
            session.getRemote().sendString(new Gson().toJson(new Error("Failed to join the game")));
        }
    }

    private void makeMove(MakeMove makeMove, Session session) throws IOException {
        try {
            AuthData authData = authDao.getAuth(makeMove.getAuthString());
            GameData gameData = gameDao.getGame(makeMove.gameId);
            gameData.game().makeMove(makeMove.move, Objects.equals(gameData.whiteUsername(), authData.username()) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK);
            gameDao.updateGame(new GameData(makeMove.gameId, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game()));
            //load game message to all clients in the game
            LoadGame loadGame = new LoadGame(gameData);
            connections.broadcast(" ", loadGame, makeMove.gameId);
            //Server sends a Notification message to all other clients in that game about the move
            Notification notification = new Notification(authData.username() + " made the following move: " + makeMove.move.toString());
            connections.broadcast(makeMove.getAuthString(), notification, makeMove.gameId);
            //If the move results in check or checkmate the server sends a Notification message to all clients.
            if (gameDao.getGame(makeMove.gameId).game().isInCheckmate(ChessGame.TeamColor.WHITE) ||  gameDao.getGame(makeMove.gameId).game().isInCheck(ChessGame.TeamColor.WHITE) ){
                Notification notification2 = new Notification("Move resulted in check/checkmate for " + gameData.whiteUsername());
                connections.broadcast(" ", notification2, makeMove.gameId);
            }
            if (gameDao.getGame(makeMove.gameId).game().isInCheckmate(ChessGame.TeamColor.BLACK) || gameDao.getGame(makeMove.gameId).game().isInCheck(ChessGame.TeamColor.BLACK)){
                Notification notification2 = new Notification("Move resulted in check/checkmate for " + gameData.blackUsername());
                connections.broadcast(" ", notification2, makeMove.gameId);
            }

        }catch (ResponseException | InvalidMoveException | IOException e){
            session.getRemote().sendString(new Gson().toJson(new Error("Unable to make the move: " + e.getMessage())));
        }
    }

    private void leave(Leave leave, Session session) throws IOException {
        try {
            AuthData authData = authDao.getAuth(leave.getAuthString());
            GameData gameData = gameDao.getGame(leave.gameId);
            if (authData.username().equals(gameData.whiteUsername())){
                gameDao.updateGame(new GameData(leave.gameId, null, gameData.blackUsername(), gameData.gameName(), gameData.game()));
            }
            if (authData.username().equals(gameData.blackUsername())){
                gameDao.updateGame(new GameData(leave.gameId, gameData.whiteUsername(), null, gameData.gameName(), gameData.game()));
            }
            connections.remove(leave.getAuthString());
            //Server sends a Notification message to all other clients  in that game
            Notification notification = new Notification(authData.username() + " has left the game.");
            connections.broadcast(leave.getAuthString(), notification, leave.gameId);
        } catch (ResponseException | IOException e){
            session.getRemote().sendString(new Gson().toJson(new Error("Unable to leave")));
        }
    }

    private void resign(Resign resign, Session session) throws IOException {
        try {
            AuthData authData = authDao.getAuth(resign.getAuthString());
            GameData gameData = gameDao.getGame(resign.gameId);
            if (authData.username().equals(gameData.whiteUsername())){
                gameDao.updateGame(new GameData(resign.gameId, null, gameData.blackUsername(), gameData.gameName(), gameData.game()));
            }
            if (authData.username().equals(gameData.blackUsername())){
                gameDao.updateGame(new GameData(resign.gameId, gameData.whiteUsername(), null, gameData.gameName(), gameData.game()));
            }
            //Server marks the game as over
            gameData.game().setEndGame();
            //Server sends a Notification message to all clients in that game
            Notification notification = new Notification(authData.username() + " has resigned.");
            connections.broadcast("", notification, resign.gameId);
            connections.remove(resign.getAuthString());
        } catch (ResponseException | IOException e){
            session.getRemote().sendString(new Gson().toJson(new Error("Unable to resign")));
        }
    }


}
