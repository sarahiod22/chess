package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

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
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/session")).DELETE().header("Authorization", "Bearer " + authToken).build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() != 200){
            throw new Exception("Error: " + httpResponse.statusCode() + " " + httpResponse.body());
        }
    }

    public Collection<GameData> listGames(String authToken) {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/user")).POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user))).header("Content-Type", "application/json").build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());


    }

    public GameData createGame(String authToken, GameData game) {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/user")).POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user))).header("Content-Type", "application/json").build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public void joinGame(String authToken, int gameId, String playerColor) {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/user")).POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user))).header("Content-Type", "application/json").build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public void clear() {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(serverUrl + "/user")).POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user))).header("Content-Type", "application/json").build();

        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }





}
