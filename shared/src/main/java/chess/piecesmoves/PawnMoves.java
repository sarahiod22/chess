package chess.piecesmoves;

import chess.*;
import com.sun.source.tree.BreakTree;

import java.util.HashSet;

public class PawnMoves {
    public static HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validPositions = new HashSet<>();
        int direction = (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startPosition = (direction == 1) ? 2 : 7;
        int promoPosition = (direction == 1) ? 8 : 1;
        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        //move 1 step forward
        if ((board.getPiece(new ChessPosition(y+direction, x)) == null) && (checkBounds(new ChessPosition(y + direction, x)))) {
            if(!checkPromotion(myPosition, new ChessPosition(y + direction, x), promoPosition, validPositions)) {
                validPositions.add(new ChessMove(myPosition, new ChessPosition(y + direction, x), null));
            }
        }
        //move 2 step forward
        if ((y == startPosition) && (board.getPiece(new ChessPosition(y+direction, x)) == null) && (board.getPiece(new ChessPosition(y+(2*direction), x)) == null) && (checkBounds(new ChessPosition(y + (2*direction), x)))){
            validPositions.add(new ChessMove(myPosition, new ChessPosition(y+(2*direction), x), null));
        }
        //capture in diagonal
        if (board.getPiece(new ChessPosition(y+direction, x+1)) != null && (board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(y+direction, x+1)).getTeamColor()) && checkBounds(new ChessPosition(y+direction, x+1))){
            if(!checkPromotion(myPosition, new ChessPosition(y+direction, x+1),promoPosition,validPositions)){
                validPositions.add(new ChessMove(myPosition, new ChessPosition(y + direction, x + 1), null));
            }
        }
        //capture in diagonal
        if (board.getPiece(new ChessPosition(y+direction, x-1)) != null && (board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(y+direction, x-1)).getTeamColor()) && (checkBounds(new ChessPosition(y+direction, x-1)))){
            if(!checkPromotion(myPosition, new ChessPosition(y+direction, x-1),promoPosition,validPositions)){
                validPositions.add( new ChessMove(myPosition, new ChessPosition(y+direction, x-1), null));
            }
        }

        return validPositions;
    }

    private static boolean checkBounds(ChessPosition position){
        return !(position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8);
    }

    private static boolean checkPromotion(ChessPosition myPosition, ChessPosition position, int promoPosition, HashSet<ChessMove> validPositions) {
        if (position.getRow() == promoPosition) {
            validPositions.add(new ChessMove(myPosition, position, ChessPiece.PieceType.QUEEN));
            validPositions.add(new ChessMove(myPosition, position, ChessPiece.PieceType.ROOK));
            validPositions.add(new ChessMove(myPosition, position, ChessPiece.PieceType.KNIGHT));
            validPositions.add(new ChessMove(myPosition, position, ChessPiece.PieceType.BISHOP));
            return true;
        }
        return false;
    }

}
