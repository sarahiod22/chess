package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

public class PawnMoves {
    public static HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validPositions = new HashSet<>();
        HashSet<ChessPosition> possiblePositions = new HashSet<>();
        int direction;
        int startPosition;
        int promoPosition;
        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            direction = 1;
            startPosition = 2;
            promoPosition = 8;
        } else {
            direction = -1;
            startPosition = 7;
            promoPosition = 1;
        }

        possiblePositions.add(new ChessPosition(y+direction, x));
        if (y == startPosition){
            possiblePositions.add(new ChessPosition(y+(2*direction), x));
        }
        if (board.getPiece(new ChessPosition(y+direction, x+1)) != null && (board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(y+direction, x+1)).getTeamColor())){
            possiblePositions.add(new ChessPosition(y+direction, x+1));
        }
        if (board.getPiece(new ChessPosition(y+direction, x-1)) != null && (board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(y+direction, x-1)).getTeamColor())){
            possiblePositions.add(new ChessPosition(y+(2*direction), x-1));
        }

        for(ChessPosition position : possiblePositions) {
            if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8)
                continue;

            if (board.getPiece(position) == null || board.getPiece(myPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, position, null));
            }
        }

        return validPositions;
    }
}
