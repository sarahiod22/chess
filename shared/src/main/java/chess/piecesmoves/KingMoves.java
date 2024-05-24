package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

public class KingMoves {
    public static HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        ChessPosition[] possiblePositions = new ChessPosition[] {
                new ChessPosition(y, x+1),
                new ChessPosition(y, x-1),
                new ChessPosition(y+1, x),
                new ChessPosition(y-1, x),
                new ChessPosition(y+1, x+1),
                new ChessPosition(y+1, x-1),
                new ChessPosition(y-1, x+1),
                new ChessPosition(y-1, x-1),
        };

        return CommonMoves.checkPositions(possiblePositions,board,myPosition);

    }
}
