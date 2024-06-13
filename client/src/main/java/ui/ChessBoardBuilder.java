//package ui;
//import chess.ChessBoard;
//import chess.ChessGame;
//import chess.ChessPiece;
//import java.io.PrintStream;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.Collections;
//
//import static ui.EscapeSequences.*;
//
//public class ChessBoardBuilder {
//    private static ChessBoard gameBoard;
//
//    public ChessBoardBuilder(ChessBoard board) {
//        gameBoard = board;
//    }
//
//    public static void drawBoard(PrintStream out, boolean reversed) {
//        ChessPiece[][] board = gameBoard.getBoard();
//
//        String[] topBottomBorder = reversed
//                ? new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "} :
//                new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
//        String[] leftRightBorder = new String[]{" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
//
//        // Top border
//        letterBorder(out, topBottomBorder);
//
//        // Main board
//        if (reversed) {
//            for (int i = 0; i < 8; i++) {
//                fillBoard(out, leftRightBorder[i], board[i], i % 2 == 0, reversed);
//            }
//        } else {
//            for (int i = 7; i >= 0; i--) {
//                fillBoard(out, leftRightBorder[i], board[i], i % 2 != 0, reversed);
//            }
//        }
//
//        // Bottom border
//        letterBorder(out, topBottomBorder);
//    }
//
//    public static void fillBoard(PrintStream out, String rowLabel, ChessPiece[] row, boolean isEvenRow, boolean reversed) {
//        numberBorder(out, rowLabel);
//
//        String currentColor = isEvenRow ? SET_BG_COLOR_BEIGE : SET_BG_COLOR_BROWN;
//        String alternateColor = isEvenRow ? SET_BG_COLOR_BROWN : SET_BG_COLOR_BEIGE;
//
//        if (reversed) {
//            Collections.reverse(Arrays.asList(row));
//        }
//
//        for (ChessPiece piece : row) {
//            out.print(currentColor);
//            printPiece(out, piece);
//            // Alternate color
//            String temp = currentColor;
//            currentColor = alternateColor;
//            alternateColor = temp;
//        }
//
//        numberBorder(out, rowLabel);
//        out.println(SET_BG_COLOR_DARK_GREY);
//    }
//
//    public static void letterBorder(PrintStream out, String[] labels) {
//        out.print(SET_TEXT_COLOR_LIGHT_GREY + SET_BG_COLOR_BLACK + EMPTY);
//        for (String label : labels) {
//            out.print(label + "\u202F");
//        }
//        out.println(EMPTY + SET_BG_COLOR_DARK_GREY);
//    }
//
//    public static void numberBorder(PrintStream out, String label) {
//        out.print(SET_TEXT_COLOR_LIGHT_GREY + SET_BG_COLOR_BLACK + label);
//    }
//
//    public static void printPiece(PrintStream out, ChessPiece piece) {
//        if (piece != null) {
//            if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
//                out.print(SET_TEXT_COLOR_WHITE);
//                switch (piece.getPieceType()) {
//                    case KING -> out.print(WHITE_KING);
//                    case QUEEN -> out.print(WHITE_QUEEN);
//                    case BISHOP -> out.print(WHITE_BISHOP);
//                    case ROOK -> out.print(WHITE_ROOK);
//                    case KNIGHT -> out.print(WHITE_KNIGHT);
//                    case PAWN -> out.print(WHITE_PAWN);
//                }
//            } else {
//                out.print(SET_TEXT_COLOR_BLACK);
//                switch (piece.getPieceType()) {
//                    case KING -> out.print(BLACK_KING);
//                    case QUEEN -> out.print(BLACK_QUEEN);
//                    case BISHOP -> out.print(BLACK_BISHOP);
//                    case ROOK -> out.print(BLACK_ROOK);
//                    case KNIGHT -> out.print(BLACK_KNIGHT);
//                    case PAWN -> out.print(BLACK_PAWN);
//                }
//            }
//        } else {
//            out.print(EMPTY);
//        }
//    }
//
//    public void printBoard(String playerColor){
//        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//
//        out.print(ERASE_SCREEN);
//        if (playerColor.equals("BLACK")) {
//            drawBoard(out, true);
//        }else {
//            drawBoard(out, false);
//        }
//        out.print(RESET_BG_COLOR + RESET_TEXT_COLOR);
//    }
//}

package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoardBuilder {
    private static ChessBoard gameBoard;
    private static ChessGame chessGame;

    public ChessBoardBuilder(ChessBoard board, ChessGame game) {
        gameBoard = board;
        chessGame = game;
    }

    public static void drawBoard(PrintStream out, boolean reversed) {
        drawBoard(out, reversed, null, Collections.emptySet());
    }

    public static void drawBoard(PrintStream out, boolean reversed, ChessPosition selectedPiece, Collection<ChessMove> validMoves) {
        ChessPiece[][] board = gameBoard.getBoard();

        String[] topBottomBorder = reversed
                ? new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "} :
                new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        String[] leftRightBorder = new String[]{" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};

        // Top border
        letterBorder(out, topBottomBorder);

        // Main board
        if (reversed) {
            for (int i = 0; i < 8; i++) {
                fillBoard(out, leftRightBorder[i], board[i], i % 2 == 0, reversed, selectedPiece, validMoves);
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                fillBoard(out, leftRightBorder[i], board[i], i % 2 != 0, reversed, selectedPiece, validMoves);
            }
        }

        // Bottom border
        letterBorder(out, topBottomBorder);
    }

    public static void fillBoard(PrintStream out, String rowLabel, ChessPiece[] row, boolean isEvenRow, boolean reversed, ChessPosition selectedPiece, Collection<ChessMove> validMoves) {
        numberBorder(out, rowLabel);

        String currentColor = isEvenRow ? SET_BG_COLOR_BEIGE : SET_BG_COLOR_BROWN;
        String alternateColor = isEvenRow ? SET_BG_COLOR_BROWN : SET_BG_COLOR_BEIGE;

        if (reversed) {
            Collections.reverse(Arrays.asList(row));
        }

        for (int col = 0; col < row.length; col++) {
            ChessPiece piece = row[col];
            ChessPosition position = new ChessPosition(reversed ? 8 - col : col + 1, Integer.parseInt(rowLabel.trim()));

            if (position.equals(selectedPiece) || validMoves.stream().anyMatch(move -> move.getEndPosition().equals(position))) {
                out.print(SET_BG_COLOR_GREEN); // Highlight the square
            } else {
                out.print(currentColor);
            }
            printPiece(out, piece);
            // Alternate color
            String temp = currentColor;
            currentColor = alternateColor;
            alternateColor = temp;
        }

        numberBorder(out, rowLabel);
        out.println(SET_BG_COLOR_DARK_GREY);
    }

    public static void letterBorder(PrintStream out, String[] labels) {
        out.print(SET_TEXT_COLOR_LIGHT_GREY + SET_BG_COLOR_BLACK + EMPTY);
        for (String label : labels) {
            out.print(label + "\u202F");
        }
        out.println(EMPTY + SET_BG_COLOR_DARK_GREY);
    }

    public static void numberBorder(PrintStream out, String label) {
        out.print(SET_TEXT_COLOR_LIGHT_GREY + SET_BG_COLOR_BLACK + label);
    }

    public static void printPiece(PrintStream out, ChessPiece piece) {
        if (piece != null) {
            if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                out.print(SET_TEXT_COLOR_WHITE);
                switch (piece.getPieceType()) {
                    case KING -> out.print(WHITE_KING);
                    case QUEEN -> out.print(WHITE_QUEEN);
                    case BISHOP -> out.print(WHITE_BISHOP);
                    case ROOK -> out.print(WHITE_ROOK);
                    case KNIGHT -> out.print(WHITE_KNIGHT);
                    case PAWN -> out.print(WHITE_PAWN);
                }
            } else {
                out.print(SET_TEXT_COLOR_BLACK);
                switch (piece.getPieceType()) {
                    case KING -> out.print(BLACK_KING);
                    case QUEEN -> out.print(BLACK_QUEEN);
                    case BISHOP -> out.print(BLACK_BISHOP);
                    case ROOK -> out.print(BLACK_ROOK);
                    case KNIGHT -> out.print(BLACK_KNIGHT);
                    case PAWN -> out.print(BLACK_PAWN);
                }
            }
        } else {
            out.print(EMPTY);
        }
    }

    public void printBoard(String playerColor) {
        printBoard(playerColor, null);
    }

    public void printBoard(String playerColor, ChessPosition selectedPiece) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        boolean reversed = playerColor.equals("BLACK");
        Collection<ChessMove> validMoves = selectedPiece != null ? chessGame.validMoves(selectedPiece) : Collections.emptySet();

        drawBoard(out, reversed, selectedPiece, validMoves);

        out.print(RESET_BG_COLOR + RESET_TEXT_COLOR);
    }

    public void highlightLegalMoves(ChessPosition position) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        Collection<ChessMove> validMoves = chessGame.validMoves(position);
        drawBoard(out, false, position, validMoves);

        out.print(RESET_BG_COLOR + RESET_TEXT_COLOR);
    }
}
