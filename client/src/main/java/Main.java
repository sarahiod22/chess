import chess.*;
import dataaccess.exceptions.ResponseException;
import ui.PreloginUI;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        PreloginUI.preloginMenu();
    }
}