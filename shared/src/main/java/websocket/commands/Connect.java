package websocket.commands;

public class Connect extends UserGameCommand{
    public final Integer gameId;
    public Connect(String authToken, Integer gameId) {
        super(authToken);
        this.gameId = gameId;
        this.commandType = CommandType.CONNECT;
    }
}
