package server;

import service.GameService;
import service.SystemService;
import service.UserService;
import spark.*;

public class Server {

    private UserService userService;
    private GameService gameService;
    private SystemService systemService;

    public Server(){

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
