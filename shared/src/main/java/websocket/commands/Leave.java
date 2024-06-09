package websocket.commands;

public class Leave extends UserGameCommand{
    public final Integer gameId;

    public Leave(String authToken, Integer gameId) {
        super(authToken);
        this.gameId = gameId;
        this.commandType = CommandType.LEAVE;
    }
}
