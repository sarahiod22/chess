package server;

import com.google.gson.Gson;
import dataaccess.*;
import server.websocket.WebSocketHandler;
import service.GameService;
import service.SystemService;
import service.UserService;
import spark.*;

import java.util.Map;

import static spark.Spark.webSocket;

public class Server {

    private final UserHandler userHandler;
    private final GameHandler gameHandler;
    private final SystemHandler systemHandler;
    private final WebSocketHandler webSocketHandler;

    public Server(){
        SQLUserDao userDao = new SQLUserDao();
        SQLGameDao gameDao = new SQLGameDao();
        SQLAuthDao authDao = new SQLAuthDao();
        this.userHandler = new UserHandler(new UserService(userDao, authDao));
        this.gameHandler = new GameHandler(new GameService(gameDao,authDao));
        this.systemHandler = new SystemHandler(new SystemService(userDao, authDao, gameDao));
        this.webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        try {
            Spark.port(desiredPort);
            Spark.staticFiles.location("web");
            webSocket("/ws", webSocketHandler);

            // Register your endpoints and handle exceptions here.
            Spark.post("/user", (req, res) -> (userHandler.register(req, res)));
            Spark.post("/session", (req, res) -> (userHandler.login(req, res)));
            Spark.delete("/session", (req, res) -> (userHandler.logout(req, res)));
            Spark.get("/game", (req, res) -> (gameHandler.listGames(req, res)));
            Spark.post("/game", (req, res) -> (gameHandler.createGame(req, res)));
            Spark.put("/game", (req, res) -> (gameHandler.joinGame(req, res)));
            Spark.delete("/db", (req, res) -> (systemHandler.clear(req, res)));

            Spark.exception(Exception.class, this::errorHandler);
            Spark.notFound((req, res) -> {
                var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
                return errorHandler(new Exception(msg), req, res);
            });

        }catch (Exception e){
            System.out.println("Unable to start server: " + e.getMessage());
            System.exit(1);
        }

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

}
