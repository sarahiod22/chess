package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

public class KnightMoves {
    public static HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        ChessPosition[] possiblePositions = new ChessPosition[] {
                new ChessPosition(y+1, x+2),
                new ChessPosition(y+2, x+1),
                new ChessPosition(y+2, x-1),
                new ChessPosition(y+1, x-2),
                new ChessPosition(y-1, x+2),
                new ChessPosition(y-2, x+1),
                new ChessPosition(y-2, x-1),
                new ChessPosition(y-1, x-2),
        };

        return CommonMoves.checkPositions(possiblePositions,board,myPosition);
    }
}
