package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

import static chess.piecesmoves.CommonMoves.checkDiagonal;

public class BishopMoves {


    public static HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validPositions = new HashSet<>();

        validPositions.addAll(checkDiagonal(board, myPosition, 1, 1));
        validPositions.addAll(checkDiagonal(board, myPosition, 1, -1));
        validPositions.addAll(checkDiagonal(board, myPosition, -1, 1));
        validPositions.addAll(checkDiagonal(board, myPosition, -1, -1));

        return validPositions;
    }
}