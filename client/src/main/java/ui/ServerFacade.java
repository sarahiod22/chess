package ui;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class ServerFacade {
    private String serverUrl;


    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthData register(UserData user)  throws Exception{
        return null;
    }

    public AuthData login(UserData user) {
        return null;
    }

    public void logout(String authToken) {

    }

    public Collection<GameData> listGames(String authToken) {
        return null;
    }

    public GameData createGame(String authToken, GameData game) {
        return null;
    }

    public void joinGame(String authToken, int gameId, String playerColor) {

    }





}
