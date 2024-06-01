package server;

import dataaccess.*;
import service.GameService;
import service.SystemService;
import service.UserService;
import spark.*;

public class Server {

    private final UserHandler userHandler;
    private final GameHandler gameHandler;
    private final SystemHandler systemHandler;

    public Server(){
        SQLUserDao userDao = new SQLUserDao();
        SQLGameDao gameDao = new SQLGameDao();
        SQLAuthDao authDao = new SQLAuthDao();
        this.userHandler = new UserHandler(new UserService(userDao, authDao));
        this.gameHandler = new GameHandler(new GameService(gameDao,authDao));
        this.systemHandler = new SystemHandler(new SystemService(userDao, authDao, gameDao));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",(req, res) -> (userHandler.register(req, res)));
        Spark.post("/session", (req, res) -> (userHandler.login(req, res)));
        Spark.delete("/session", (req, res) -> (userHandler.logout(req, res)));
        Spark.get("/game", (req, res) -> (gameHandler.listGames(req, res)));
        Spark.post("/game", (req, res) -> (gameHandler.createGame(req, res)));
        Spark.put("/game", (req, res) -> (gameHandler.joinGame(req, res)));
        Spark.delete("/db", (req, res) -> (systemHandler.clear(req, res)));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
