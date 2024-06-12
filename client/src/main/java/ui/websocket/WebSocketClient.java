package ui.websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import ui.ServerFacade;
import websocket.commands.Connect;
import websocket.commands.Leave;
import websocket.commands.MakeMove;
import websocket.commands.Resign;
import java.util.Collection;
import java.util.List;

public class WebSocketClient {
    private static ServerFacade serverFacade;
    private static WebSocketFacade webSocketFacade;
    private AuthData userToken;

    public WebSocketClient(String URL, NotificationHandler notificationHandler) throws ResponseException {
        serverFacade = new ServerFacade(URL);
        webSocketFacade = new WebSocketFacade(URL, notificationHandler);
    }

    public boolean login(String username, String password) throws Exception {
        UserData userData = new UserData(username, password, null);
        this.userToken = serverFacade.login(userData);

        return this.userToken != null && this.userToken.authToken() != null;
    }

    public boolean register(String username, String password, String email) throws Exception {
        UserData userData = new UserData(username, password, email);
        this.userToken = serverFacade.register(userData);
        return this.userToken != null && this.userToken.authToken() != null;
    }

    public void logout() throws Exception {
        serverFacade.logout(this.userToken.authToken());
        this.userToken = null;
    }

    public Collection<GameData> listGames() throws Exception {
        return (serverFacade.listGames(this.userToken.authToken()).games());
    }

    public GameData createGame(String gameName) throws Exception {
        GameData newGame = new GameData(0,null,null, gameName, new ChessGame());
        //GameData createdGame = serverFacade.createGame(userToken.authToken(), newGame);
        GameData game = serverFacade.createGame(userToken.authToken(), newGame);
        return game;
    }

    public void joinGame(int gameId, ChessGame.TeamColor playerColor) throws Exception {
        serverFacade.joinGame(this.userToken.authToken(), gameId, playerColor.toString());
        webSocketFacade.sendCommand(new Connect(this.userToken.authToken(), gameId, playerColor, false));
    }

    public void joinObserver(int gameId) throws Exception {
        webSocketFacade.sendCommand(new Connect(this.userToken.authToken(), gameId, null, true));
    }

    public void leaveGame(int gameId) throws Exception {
        webSocketFacade.sendCommand(new Leave(this.userToken.authToken(), gameId));
    }

    public void resignGame(Integer gameId) throws Exception {
        webSocketFacade.sendCommand(new Resign(this.userToken.authToken(), gameId));
    }

    public void makeMove(Integer inGameID, ChessMove move) throws Exception {
        webSocketFacade.sendCommand(new MakeMove(this.userToken.authToken(), inGameID, move));
    }
}
