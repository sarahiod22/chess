package ui;
import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class ChessBoardBuilder {
    private static ChessBoard gameBoard;
    private static ChessGame chessGame;

    public ChessBoardBuilder(ChessBoard board, ChessGame game) {
        gameBoard = board;
        chessGame = game;
    }

    public static void drawBoard(PrintStream out, boolean reversed, ChessPosition piecePosition) {
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
                fillBoard(out, leftRightBorder[i], board[i], i % 2 == 0, reversed, piecePosition);
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                fillBoard(out, leftRightBorder[i], board[i], i % 2 != 0, reversed, piecePosition);
            }
        }

        // Bottom border
        letterBorder(out, topBottomBorder);
    }


//    public static void fillBoard(PrintStream out, String rowLabel, ChessPiece[] row, boolean isEvenRow, boolean reversed, ChessPosition piecePosition) {
//        Collection<ChessPosition> endPositions = new HashSet<>();
//        if (piecePosition != null) {
//            Collection<ChessMove> validMoves = chessGame.validMoves(piecePosition);
//            for (ChessMove move : validMoves) {
//                endPositions.add(move.getEndPosition());
//            }
//        }
//
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

    public static void fillBoard(PrintStream out, String rowLabel, ChessPiece[] row, boolean isEvenRow, boolean reversed, ChessPosition piecePosition) {
        Collection<ChessPosition> endPositions = new HashSet<>();
        if (piecePosition != null) {
            Collection<ChessMove> validMoves = chessGame.validMoves(piecePosition);
            for (ChessMove move : validMoves) {
                endPositions.add(move.getEndPosition());
            }
        }

        numberBorder(out, rowLabel);

        String currentColor = isEvenRow ? SET_BG_COLOR_BEIGE : SET_BG_COLOR_BROWN;
        String alternateColor = isEvenRow ? SET_BG_COLOR_BROWN : SET_BG_COLOR_BEIGE;

        if (reversed) {
            Collections.reverse(Arrays.asList(row));
        }

        for (int i = 0; i < row.length; i++) {
            ChessPiece piece = row[i];
            ChessPosition currentPosition = reversed ? new ChessPosition(7 - rowLabel.charAt(1) + '1', 7 - i) : new ChessPosition(rowLabel.charAt(1) - '1', i);

            if (currentPosition.equals(piecePosition) || endPositions.contains(currentPosition)) {
                out.print(SET_TEXT_COLOR_YELLOW);
            } else {
                out.print(currentColor);
            }

            printPiece(out, piece);
            // Reset to the current background color
            out.print(currentColor);

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

    public void printBoard(String playerColor, ChessPosition piecePosition){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        if (playerColor.equals("BLACK")) {
            drawBoard(out, true, piecePosition);
        }else {
            drawBoard(out, false, piecePosition);
        }
        out.print(RESET_BG_COLOR + RESET_TEXT_COLOR);
    }

}