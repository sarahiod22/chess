package ui;

import com.google.gson.Gson;
import model.*;
import java.net.URI;
import java.net.http.*;
import java.util.Map;

public class ServerFacade {
    private String serverUrl;


    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthData register(UserData user)  throws Exception{
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/user")).POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user))).header("Content-Type", "application/json").build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200){
            return new Gson().fromJson(httpResponse.body(), AuthData.class);
        } else {
            throw new Exception("Error: " + httpResponse.statusCode() + " " + httpResponse.body());
        }
    }

    public AuthData login(UserData user) throws Exception{
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/session")).POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user))).header("Content-Type", "application/json").build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200){
            return new Gson().fromJson(httpResponse.body(), AuthData.class);
        } else {
            throw new Exception("Error: " + httpResponse.statusCode() + " " + httpResponse.body());
        }
    }

    public void logout(String authToken) throws Exception{
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/session")).DELETE().header("Authorization", authToken).build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() != 200){
            throw new Exception("Error: " + httpResponse.statusCode() + " " + httpResponse.body());
        }
    }

    public ListGameData listGames(String authToken) throws Exception{
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/game")).GET().header("Authorization", authToken).build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200){
            return new Gson().fromJson(httpResponse.body(), ListGameData.class);
        } else {
            throw new Exception("Error: " + httpResponse.statusCode() + " " + httpResponse.body());
        }

    }

    public GameData createGame(String authToken, GameData game) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/game")).POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(game))).header("Content-Type", "application/json").header("Authorization", authToken).build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200){
            return new Gson().fromJson(httpResponse.body(), GameData.class);
        } else {
            throw new Exception("Error: " + httpResponse.statusCode() + " " + httpResponse.body());
        }
    }

    public void joinGame(String authToken, int gameId, String playerColor) throws Exception{
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/game")).PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(Map.of("playerColor", playerColor, "gameID", gameId)))).header("Content-Type", "application/json").header("Authorization", authToken).build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() != 200){
            throw new Exception("Error: " + httpResponse.statusCode() + " " + httpResponse.body());
        }
    }

    public void clear() throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/db")).DELETE().build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() != 200){
            throw new Exception("Error: " + httpResponse.statusCode() + " " + httpResponse.body());
        }
    }





}
