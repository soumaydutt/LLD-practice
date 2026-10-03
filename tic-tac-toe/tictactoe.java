import java.util.*;

//BLOCK 1:ENUMS & PIECE HIERARCHY
enum PieceType{
    O,
    X
}
enum GameStatus{
    IN_PROGRESS,
    WIN,
    DRAW
}

abstract class PlayingPiece{
    private final PieceType pieceType;
    public PlayingPiece(PieceType pieceType){
        this.pieceType=pieceType;
    }
    public PieceType getPieceType(){
        return pieceType;
    }
}
class PlayingPieceX extends PlayingPiece{
    public PlayingPieceX(){
        super (PieceType.X);
    }
}
class PlayingPieceO extends PlayingPiece{
    public PlayingPieceO(){
        super(PieceType.O);
    }
}
//BLOCK 2:BOARD
class Board{
    private final int size;
    private final PlayingPiece[][] grid;
    public Board (int size){
        this.size=size;
        this.grid=new PlayingPiece[size][size];
    }
    public int getSize(){
        return size;
    }
    public PlayingPiece[][] getgrid(){
        return grid;
    }
    public boolean addPiece(int row,int col,PlayingPiece piece){
        if(row<0||row>=size||col<0||col>=size){
            return false;
        }
        if(grid[row][col]!=null){
            return false;
        }
        grid[row][col]=piece;
        return true;
    }
    public List<int[]> getFreeCells(){
        List<int[]> freeCells= new ArrayList<>();
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if (grid[i][j]==null){
                    freeCells.add(new int[]{i,j});
                }
            }
        }
        return freeCells;
    }
    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != null) {
                    System.out.print(" " + grid[i][j].getPieceType() + " ");
                } else {
                    System.out.print("   ");
                }
                if (j < size - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < size - 1) {
                System.out.println("---+---+---");
        }
       }
      }
     }




public class tictactoe{
    public static void main(String[] args) {
        Board board = new Board(3);
        PlayingPiece pieceX = new PlayingPieceX();
        PlayingPiece pieceO = new PlayingPieceO();

        System.out.println("Initial empty board:");
        board.printBoard();
        System.out.println("Free cells remaining: " + board.getFreeCells().size());

        // Test 1: Valid placements
        boolean placed1 = board.addPiece(0, 0, pieceX);
        boolean placed2 = board.addPiece(1, 1, pieceO);
        System.out.println("\nPlaced X at (0,0): " + placed1);
        System.out.println("Placed O at (1,1): " + placed2);

        // Test 2: Placing on an already occupied spot (Expected: false)
        boolean placedOccupied = board.addPiece(0, 0, pieceO);
        System.out.println("Placed O at occupied (0,0) [Expected false]: " + placedOccupied);

        // Test 3: Placing out of bounds (Expected: false)
        boolean placedOutOfBounds = board.addPiece(3, 0, pieceX);
        System.out.println("Placed X at (3,0) out-of-bounds [Expected false]: " + placedOutOfBounds);

        System.out.println("\nBoard after moves:");
        board.printBoard();
        System.out.println("Free cells remaining: " + board.getFreeCells().size());

        System.out.println("\nBlock 2 complete: Board logic verified!");
    }
}





/*
block 1 test:
public static void main (String[] args){
        PlayingPiece pieceX=new PlayingPieceX();
        PlayingPiece pieceO= new PlayingPieceO();
        System.out.println("Piece 1 type:"+pieceX.getPieceType());
        System.out.println("Piece 2 type:"+pieceO.getPieceType());
    }
    */