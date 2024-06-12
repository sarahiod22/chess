
package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.GameData;
import dataaccess.exceptions.ResponseException;
import java.io.*;
import java.net.*;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public GameData[] listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, GameData[].class, authToken);
    }

    public GameData createGame(String authToken, GameData game) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, game, GameData.class, authToken);
    }

    public void joinGame(String authToken, Integer gameId, String playerType) throws ResponseException {
        var path = "/game";
        Map<String, Object> requestBody = Map.of("gameID", gameId, "playerColor", playerType);
        this.makeRequest("PUT", path, requestBody, null, authToken);
    }

    public void clearData() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = new URI(serverUrl + path).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
            if (request != null) {
                http.setDoOutput(true);
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        http.addRequestProperty("Content-Type", "application/json");
        String reqData = new Gson().toJson(request);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        if (responseClass != null) {
            try (InputStream respBody = (http.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) ? http.getInputStream() : http.getErrorStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                return new Gson().fromJson(reader, responseClass);
            }
        }
        return null;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (status/100 != 2) {
            throw new ResponseException(status, "Error: " + status);
        }
    }
}