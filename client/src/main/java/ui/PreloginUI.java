//package ui;
//
//import model.*;
//import java.util.Scanner;
//
//public class PreloginUI {
//
//    private static ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
//    private static boolean loggedIn = false;
//    private static AuthData authData = null;
//    private static Scanner scanner = new Scanner(System.in);
//
//    public static void preloginMenu() throws Exception {
//        System.out.println("Welcome to 240 Chess! Available commands:");
//        System.out.println();
//        System.out.println("1. \"register\"");
//        System.out.println("2. \"login\"");
//        System.out.println("3. \"quit\"");
//        System.out.println("4. \"help\"");
//
//        String command = scanner.nextLine();
//
//        switch (command) {
//            case "register":
//                register();
//                break;
//            case "login":
//                login();
//                break;
//            case "quit":
//                System.out.println("Goodbye!");
//                System.exit(0);
//            case "help":
//                System.out.println("register - to create a new account");
//                System.out.println("login - to play chess");
//                System.out.println("quit -  exit the program");
//                System.out.println("help - repeat commands");
//                preloginMenu();
//                break;
//            default:
//                System.out.println("Invalid command, please enter: register, login, quit, help");
//                preloginMenu();
//        }
//    }
//
//        private static void register() throws Exception {
//            try {
//                System.out.println("Enter your username: ");
//                String username = scanner.nextLine();
//                System.out.println("Enter your password: ");
//                String password = scanner.nextLine();
//                System.out.println("Enter your email: ");
//                String email = scanner.nextLine();
//
//                UserData userData = new UserData(username, password, email);
//                authData = serverFacade.register(userData);
//                loggedIn = true;
//                System.out.println("You have registered your account and logged in successfully.");
//
//                PostloginUI.postloginMenu(authData, serverFacade);
//
//            }catch (Exception e) {
//                System.out.println("Registration error: Please provide valid user information");
//                System.out.println(" ");
//                loggedIn = false;
//                preloginMenu();
//            }
//        }
//
//        private static void login() throws Exception {
//            try {
//                System.out.println("Enter your username: ");
//                String username = scanner.nextLine();
//                System.out.println("Enter your password: ");
//                String password = scanner.nextLine();
//
//                UserData userData = new UserData(username, password, null);
//                authData = serverFacade.login(userData);
//                loggedIn = true;
//                System.out.println("You have logged in successfully.");
//
//                PostloginUI.postloginMenu(authData, serverFacade);
//
//            }catch (Exception e) {
//                System.out.println("Login error: Unable to login with the information provided");
//                System.out.println(" ");
//                loggedIn = false;
//                preloginMenu();
//            }
//        }
//}
