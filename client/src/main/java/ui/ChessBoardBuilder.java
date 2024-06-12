package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static ui.EscapeSequences.*;

public class ChessBoardBuilder {
    private static ChessBoard gameBoard;

    public ChessBoardBuilder(ChessBoard board) {
        gameBoard = board;
    }

    public static void drawBoard(PrintStream out, boolean reversed) {
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
                fillBoard(out, leftRightBorder[i], board[i], i % 2 == 0, reversed);
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                fillBoard(out, leftRightBorder[i], board[i], i % 2 != 0, reversed);
            }
        }

        // Bottom border
        letterBorder(out, topBottomBorder);
    }

    public static void fillBoard(PrintStream out, String rowLabel, ChessPiece[] row, boolean isEvenRow, boolean reversed) {
        numberBorder(out, rowLabel);

        String currentColor = isEvenRow ? SET_BG_COLOR_BEIGE : SET_BG_COLOR_BROWN;
        String alternateColor = isEvenRow ? SET_BG_COLOR_BROWN : SET_BG_COLOR_BEIGE;

        if (reversed) {
            Collections.reverse(Arrays.asList(row));
        }

        for (ChessPiece piece : row) {
            out.print(currentColor);
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

    public static void drawDivider(PrintStream out) {
        out.println(SET_BG_COLOR_DARK_GREY + EMPTY.repeat(1));
    }

    public void printBoard(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawBoard(out, true); // White pieces at the top
        drawDivider(out);
        drawBoard(out, false);  // Black pieces at the top

        out.print(SET_BG_COLOR_BLACK);
    }
}
