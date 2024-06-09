package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    public final Integer gameId;
    public final ChessMove move;

    public MakeMove(String authToken, Integer gameId, ChessMove move) {
        super(authToken);
        this.gameId = gameId;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }
}
