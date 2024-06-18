package ui;

import chess.*;
import dataaccess.GameDao;
import dataaccess.SQLGameDao;
import model.AuthData;
import model.GameData;
import model.UserData;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import websocket.commands.Connect;
import websocket.commands.Leave;
import websocket.commands.MakeMove;
import websocket.commands.Resign;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

;
public class Client implements NotificationHandler {
    private ClientState state = ClientState.PRE_LOGIN;
    private ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
    private WebSocketFacade webSocket = new WebSocketFacade("ws://localhost:8080/connect", this);
    private GameDao gameDao = new SQLGameDao();
    private Scanner scanner = new Scanner(System.in);

    private AuthData authData = null;
    private int currentGameId = -1;
    private String currentPlayerColor = "";
    private ChessBoard currentBoard = null;
    private ChessGame currentGame = null;

    public void displayPreloginCommands() {
        System.out.println("1. \"register\"");
        System.out.println("2. \"login\"");
        System.out.println("3. \"quit\"");
        System.out.println("4. \"help\"");
    }

    public void displayPostloginCommands() {
        System.out.println("1. \"create game\"");
        System.out.println("2. \"list games\"");
        System.out.println("3. \"join game\"");
        System.out.println("4. \"observe game\"");
        System.out.println("5. \"logout\"");
        System.out.println("6. \"quit\"");
        System.out.println("7. \"help\"");
    }

    public void displayIngameCommands() {
        System.out.println("1. \"redraw\"");
        System.out.println("2. \"leave\"");
        System.out.println("3. \"make move\"");
        System.out.println("4. \"resign\"");
        System.out.println("5. \"highlight legal moves\"");
        System.out.println("6. \"help\"");
    }

    public void displayObservingCommands(){
        System.out.println("1. \"redraw\"");
        System.out.println("2. \"leave\"");
        System.out.println("3. \"help\"");
    }

    public void run() throws Exception {
        System.out.println("Welcome to 240 Chess!");
        System.out.println();

        do {
            System.out.println("Available commands: ");

            if(state == ClientState.PRE_LOGIN) {
                displayPreloginCommands();

                switch(scanner.nextLine()) {
                    case "1":
                    case "register":
                        register();
                        break;
                    case "2":
                    case "login":
                        login();
                        break;
                    case "3":
                    case "quit":
                        quit();
                        break;
                    case "4":
                    case "help":
                        helpPrelogin();
                        break;
                    default:
                        System.out.println("Invalid command, please enter: register, login, quit, help");
                        break;
                }
            }
            else if(state == ClientState.POST_LOGIN) {
                displayPostloginCommands();
                switch(scanner.nextLine()) {
                    case "1":
                    case "create game":
                        createGame();
                        break;
                    case "2":
                    case "list games":
                        listGames();
                        break;
                    case "3":
                    case "join game":
                        joinGame(false);
                        break;
                    case "4":
                    case "observe game":
                        joinGame(true);
                        break;
                    case "5":
                    case "logout":
                        logout();
                        break;
                    case "6":
                    case "quit":
                        quit();
                        break;
                    case "7":
                    case "help":
                        helpPostlogin();
                        break;
                    default:
                        System.out.println("Invalid command, please enter: create game, list games, join game, observe game, logout, quit, help");
                        break;
                }
            }
            if(state == ClientState.IN_GAME){
                displayIngameCommands();
                switch(scanner.nextLine()) {
                    case "1":
                    case "redraw":
                        redraw();
                        break;
                    case "2":
                    case "leave":
                        leave();
                        break;
                    case "3":
                    case "make move":
                        makeMove();
                        break;
                    case "4":
                    case "resign":
                        resign();
                        break;
                    case "5":
                    case "highlight legal moves":
                        highlightMoves();
                        break;
                    case "6":
                    case "help":
                        helpIngame();
                        break;
                    default:
                        System.out.println("Invalid command, please enter: redraw, leave, make move, resign, highlight legal moves, help");
                        break;
                }
            }
            else if (state == ClientState.OBSERVING){
                displayObservingCommands();
                switch(scanner.nextLine()) {
                    case "1":
                    case "redraw":
                        redraw();
                        break;
                    case "2":
                    case "leave":
                        state = ClientState.POST_LOGIN;
                        break;
                    case "3":
                    case "help":
                        helpObserving();
                        break;
                    default:
                        System.out.println("Invalid command, please enter: redraw, leave, help");
                        break;
                }
            }
        }
        while(true);
    }

    private void register() throws Exception {
        try {
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            System.out.println("Enter your email: ");
            String email = scanner.nextLine();

            UserData userData = new UserData(username, password, email);
            authData = serverFacade.register(userData);
            System.out.println("You have registered your account and logged in successfully.");

            state = ClientState.POST_LOGIN;
        }
        catch (Exception e) {
            //throw e;
            System.out.println("Unable to register with the information provided");
            System.out.println(" ");
        }
    }

    private void login() throws Exception {
        try {
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            UserData userData = new UserData(username, password, null);
            authData = serverFacade.login(userData);
            System.out.println("You have logged in successfully.");

            state = ClientState.POST_LOGIN;
        }
        catch (Exception e) {
            //throw e;
            System.out.println("Unable to login with the information provided");
            System.out.println(" ");
        }
    }

    private void quit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private void createGame() throws Exception {
        try {
            System.out.println("Enter the name of the game: ");
            String gameName = scanner.nextLine();

            GameData gameData = new GameData(0, null, null, gameName, null);
            serverFacade.createGame(authData.authToken(), gameData);
            System.out.println("Game created successfully!");
        }
        catch (Exception e) {
            //throw e;
            System.out.println("Unable to create game with the information provided");
            System.out.println(" ");
        }
    }

    private void listGames() throws Exception {
        try {
            var games = serverFacade.listGames(authData.authToken());
            int gameIDIndex = 1;
            if (games != null && !(games.games().isEmpty())) {
                System.out.println("List of current games:");
                for (GameData game : games.games()) {
                    System.out.println("Game ID: " + gameIDIndex);
                    System.out.println("Game Name: " + game.gameName());
                    System.out.println("White Player: " + game.whiteUsername());
                    System.out.println("Black Player: " + game.blackUsername());
                    System.out.println(" ");
                    gameIDIndex++;
                }
            } else {
                System.out.println("No games available.");
            }
        }
        catch (Exception e) {
            //throw e;
            System.out.println("Unable to list games");
            System.out.println(" ");
        }
    }

    private void joinGame(boolean observer) throws Exception {
        try {
            var games = serverFacade.listGames(authData.authToken());
            Map<Integer, Integer> gamesIDs = new HashMap<>();
            int gameIDIndex = 1;
            if (games != null && !(games.games().isEmpty())) {
                for (GameData game : games.games()) {
                    gamesIDs.put(gameIDIndex, game.gameID());
                    gameIDIndex++;
                }
            }

            System.out.println("Enter the Game ID:");
            int gameId = scanner.nextInt();
            scanner.nextLine();
            currentGameId = gamesIDs.get(gameId);

            if (!observer) {
                System.out.println("Enter player color (WHITE/BLACK):");
                String playerColor = scanner.nextLine().toUpperCase();
                serverFacade.joinGame(authData.authToken(), currentGameId, playerColor);
                currentPlayerColor = playerColor;
                state = ClientState.IN_GAME;
            }
            else {
                serverFacade.joinGame(authData.authToken(), currentGameId, "observer");
                currentPlayerColor = "";
                state = ClientState.OBSERVING;
            }

            webSocket.sendCommand(new Connect(authData.authToken(), currentGameId, currentPlayerColor.equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK, observer));

            System.out.println("Joined game successfully.");

        }
        catch (Exception e) {
            //throw e;
            System.out.println("Unable to join game with the information provided");
            System.out.println(" ");
        }
    }

    private void logout() throws Exception {
        serverFacade.logout(authData.authToken());
        state = ClientState.PRE_LOGIN;
    }

    private void redraw() {
        if(currentBoard != null) {
            ChessBoardBuilder boardBuilder = new ChessBoardBuilder(currentBoard, currentGame);
            boardBuilder.printBoard(currentPlayerColor, null);
        }
    }

    private void makeMove() throws Exception {
        try {
            System.out.print("Enter move to execute (e.g., a1-a5): ");
            String moveInput = scanner.nextLine();
            String[] movePositions = moveInput.split("-");
            ChessPosition start = new ChessPosition(-1,-1);
            ChessPosition end = new ChessPosition(-1,-1);
            start = start.getPositionFromString(movePositions[0].trim().toLowerCase(),currentPlayerColor.toLowerCase(Locale.ROOT).equals("black"));
            end = end.getPositionFromString(movePositions[1].trim().toLowerCase(), currentPlayerColor.toLowerCase(Locale.ROOT).equals("black"));
            if (start != null && end != null) {
                ChessMove move = new ChessMove(start, end, null);
                try {
                    webSocket.sendCommand(new MakeMove(authData.authToken(), currentGameId, move));
                    System.out.println("Move executed succesfully");
                    //displayIngameCommands();
                } catch (Exception e) {
                    System.out.println("Error making move");
                }
            }
        }
        catch (Exception e) {
            //throw e;
            System.out.println("Unable to make a move with the information provided");
            System.out.println(" ");
        }
    }

    private void resign() throws Exception {
        try {
            System.out.print("Are you sure you want to resign? [y/n]: ");
            String confirmation = scanner.nextLine();
            if (confirmation.equals("y")) {
                webSocket.sendCommand(new Resign(authData.authToken(), currentGameId));
                System.out.println("Game is over!");
            }
        }
        catch (Exception e) {
            //throw e;
            System.out.println("Unable to resign the game");
            System.out.println(" ");
        }
    }

    private void leave() throws Exception {
        try {
            webSocket.sendCommand(new Leave(authData.authToken(), currentGameId));
            state = ClientState.POST_LOGIN;
        }
        catch (Exception e) {
            //throw e;
            System.out.println("Unable to leave the game");
            System.out.println(" ");
        }
    }

    private void highlightMoves() throws Exception {
        try {
            if(currentBoard != null && currentGame!= null) {
                System.out.println("Enter the position of the piece you want to move: (e.g., a1) ");
                String positionInput = scanner.nextLine();
                ChessPosition piecePosition = new ChessPosition(-1,-1);
                piecePosition = piecePosition.getPositionFromString(positionInput, currentPlayerColor.toLowerCase(Locale.ROOT).equals("black"));
                ChessBoardBuilder boardBuilder = new ChessBoardBuilder(currentBoard, currentGame);
                boardBuilder.printBoard(currentPlayerColor, piecePosition);
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    private void helpPrelogin() {
        System.out.println("register - to create a new account");
        System.out.println("login - to play chess");
        System.out.println("quit -  exit the program");
        System.out.println("help - repeat commands");
    }

    private void helpPostlogin() {
        System.out.println("create game - create a new game");
        System.out.println("list games - list all existing games");
        System.out.println("join game - join an existing game");
        System.out.println("observe game - join a game as an observer");
        System.out.println("logout - go back to previous menu");
        System.out.println("quit - exit the program");
        System.out.println("help - repeat commands");
    }

    private void helpIngame() {
        System.out.println("redraw - redraw the chess board");
        System.out.println("leave - leave the current game");
        System.out.println("make move - move one of your pieces");
        System.out.println("resign - leave and end the game");
        System.out.println("highlight legal moves - redraw board with valid moves for a piece");
        System.out.println("help - repeat commands");
    }

    private void helpObserving() {
        System.out.println("redraw - redraw the chess board");
        System.out.println("leave - leave the current game");
        System.out.println("help - repeat commands");
    }

    @Override
    public void notify(Notification notification) {

    }

    @Override
    public void warn(Error error) {

    }

    @Override
    public void loadGame(LoadGame loadGame) {
        this.currentGame = loadGame.game.game();
        this.currentBoard = loadGame.game.game().getBoard();
        redraw();
    }
}
