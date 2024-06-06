package ui;

import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.util.Scanner;

public class PreloginUI {

    private ServerFacade serverFacade;
    private boolean loggedIn;
    private AuthData authData;
    private Scanner scanner;

    public PreloginUI(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
        this.loggedIn = false;
        this.scanner = new Scanner(System.in);
        this.authData = null;
    }

    public void preloginMenu() throws Exception {
        System.out.println("Welcome to 240 Chess! Available commands:");
        System.out.println();
        System.out.println("1. register");
        System.out.println("2. login");
        System.out.println("3. quit");
        System.out.println("4. help");

        String command = scanner.nextLine();

        switch (command) {
            case "help":
                System.out.println("register - to create a new account");
                System.out.println("login - to play chess");
                System.out.println("quit -  playing chess");
                System.out.println("help - repeat commands");
                preloginMenu();
                break;
            case "register":
                register();
                break;
            case "login":
                login();
                break;
            case "quit":
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid command, please enter: register, login, quit, help");
                preloginMenu();
        }
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
                loggedIn = true;
                System.out.println("You have registered your account and logged in successfully.");

                //PostloginUI.postloginMenu();

            }catch (Exception e) {
                System.out.println("Registration error: " + e.getMessage());
                loggedIn = false;
                preloginMenu();
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
                loggedIn = true;
                System.out.println("You have logged in successfully.");

                //PostloginUI.postloginMenu();

            }catch (Exception e) {
                System.out.println("Login error: " + e.getMessage());
                loggedIn = false;
                preloginMenu();
            }
        }
}
