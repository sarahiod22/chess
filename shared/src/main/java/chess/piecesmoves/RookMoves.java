package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

import static chess.piecesmoves.CommonMoves.checkDiagonal;

public class RookMoves {
    public static HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validPositions = new HashSet<>();

        validPositions.addAll(checkDiagonal(board, myPosition, 1, 0));
        validPositions.addAll(checkDiagonal(board, myPosition, -1, 0));
        validPositions.addAll(checkDiagonal(board, myPosition, 0, 1));
        validPositions.addAll(checkDiagonal(board, myPosition, 0, -1));

        return validPositions;
    }
}
