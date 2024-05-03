package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

import static chess.piecesmoves.CommonMoves.checkMovements;

public class RookMoves {
    public static HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validPositions = new HashSet<>();

        validPositions.addAll(checkMovements(board, myPosition, 1, 0));
        validPositions.addAll(checkMovements(board, myPosition, -1, 0));
        validPositions.addAll(checkMovements(board, myPosition, 0, 1));
        validPositions.addAll(checkMovements(board, myPosition, 0, -1));

        return validPositions;
    }
}
