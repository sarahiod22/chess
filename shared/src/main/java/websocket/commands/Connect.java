package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand{
    public final Integer gameId;
    public final ChessGame.TeamColor teamColor;
    public final boolean observer;

    public Connect(String authToken, Integer gameId, ChessGame.TeamColor teamColor, boolean observer) {
        super(authToken);
        this.gameId = gameId;
        this.commandType = CommandType.CONNECT;
        this.teamColor = teamColor;
        this.observer = observer;
    }
}
