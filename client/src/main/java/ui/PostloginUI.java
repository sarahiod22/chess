package ui;

import chess.ChessGame;
import dataaccess.GameDao;
import dataaccess.SQLGameDao;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PostloginUI {
    private static Scanner scanner = new Scanner(System.in);
    private static GameDao gameDao = new SQLGameDao();

    public static void postloginMenu(AuthData authData, ServerFacade serverFacade) throws Exception {
        System.out.println("Available commands: ");
        System.out.println("1. \"create game\"");
        System.out.println("2. \"list games\"");
        System.out.println("3. \"join game\"");
        System.out.println("4. \"observe game\"");
        System.out.println("5. \"logout\"");
        System.out.println("6. \"quit\"");
        System.out.println("7. \"help\"");

        String command = scanner.nextLine();

        switch (command) {
                case "create game":
                    createGame(authData, serverFacade);
                    break;
                case "list games":
                    listGames(authData, serverFacade);
                    postloginMenu(authData, serverFacade);
                    break;
                case "join game":
                    joinGame(authData, serverFacade, false);
                    //postloginMenu(authData, serverFacade);
                    break;
                case "observe game":
                    joinGame(authData, serverFacade, true);
                    //postloginMenu(authData, serverFacade);
                    break;
                case "logout":
                    logout(authData, serverFacade);
                    PreloginUI.preloginMenu();
                    break;
                case "quit":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                case "help":
                    System.out.println("create game - create a new game");
                    System.out.println("list games - list all existing games");
                    System.out.println("join game - join an existing game");
                    System.out.println("observe game - join a game as an observer");
                    System.out.println("logout - go back to previous menu");
                    System.out.println("quit - exit the program");
                    System.out.println("help - repeat commands");
                    postloginMenu(authData, serverFacade);
                    break;
                default:
                    System.out.println("Invalid command, please enter: create game, list games, join game, observe game, logout, quit, help");
                    postloginMenu(authData, serverFacade);
        }
    }

    private static void createGame(AuthData authData, ServerFacade serverFacade) throws Exception {
        //try {
            System.out.println("Enter the name of the game: ");
            String gameName = scanner.nextLine();

            GameData gameData = new GameData(0, null, null, gameName, null);
            serverFacade.createGame(authData.authToken(), gameData);
            System.out.println("Game created successfully!");
            postloginMenu(authData, serverFacade);
        //}catch (Exception e) {
        //    System.out.println("Error creating the game: Please provide valid game information");
        //    System.out.println(" ");
        //    postloginMenu(authData, serverFacade);
        //}
    }

    private static void listGames(AuthData authData, ServerFacade serverFacade) throws Exception {
        //try {
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
        //} catch (Exception e) {
        //    System.out.println("Error listing games");
        //    System.out.println(" ");
        //    postloginMenu(authData, serverFacade);
        //}
    }

    private static void joinGame(AuthData authData, ServerFacade serverFacade, boolean observer) throws Exception {
        //try {
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
            int actualGameId = gamesIDs.get(gameId);

            if (!observer) {
                System.out.println("Enter player color (WHITE/BLACK):");
                String playerColor = scanner.nextLine().toUpperCase();
                serverFacade.joinGame(authData.authToken(), actualGameId, playerColor);
            }

            serverFacade.joinGame(authData.authToken(), actualGameId, "observer");
            System.out.println("Joined game successfully.");
            ChessBoardBuilder chessBoard = new ChessBoardBuilder(gameDao.getGame(actualGameId).game().getBoard());
            chessBoard.printBoard("");
        //}catch (Exception e) {
        //    System.out.println("Error joining game: Please provide valid game information");
        //    System.out.println(" ");
        //    postloginMenu(authData, serverFacade);
        //}
    }

    private static void logout(AuthData authData, ServerFacade serverFacade) throws Exception {
        try {
            serverFacade.logout(authData.authToken());
        }catch (Exception e) {
            System.out.println("Error logging out");
        }
    }
}