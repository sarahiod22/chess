package websocket.commands;

public class Resign extends UserGameCommand {
    public final Integer gameId;

    public Resign(String authToken, Integer gameId) {
        super(authToken);
        this.gameId = gameId;
        this.commandType = CommandType.RESIGN;
    }
}
