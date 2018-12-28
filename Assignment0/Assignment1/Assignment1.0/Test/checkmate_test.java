

import Logic.Game;
import Logic.Player;
import Logic.Board;

import Piece.Piece;
import Piece.Hopper;
import Piece.King;
import Piece.Bishop;
import Piece.Cannon;
import Piece.Knight;
import Piece.Pawn;
import Piece.Queen;
import Piece.Rook;

import GUI.chessBoard_gui;



import java.io.IOException;
import java.util.Scanner;

public class checkmate_test {

    public static void main(String[] args) throws IOException {

        //initializing players
        Player white = new Player("White");
        Player black = new Player("Black");

        Game game = new Game();

        Board board = new Board(white, black, true);
        Scanner scanner = new Scanner(System.in);

        int[] positions = new int[4];
        Piece[][] spots = board.getspots();

        /************************************ test logic *************************************/



        System.out.println("\nLogic tests: ");
        //not case
        positions[0] = 1;
        positions[1] = 7;
        positions[2] = 3;
        positions[3] = 7;

        board.move(positions, white);

        if(game.inCheckmate(board, white) ||! game.inCheckmate(board, black))
            System.out.println("not-checkmate pass");


        //in checkmate
        positions[0] = 6;
        positions[1] = 7;
        positions[2] = 5;
        positions[3] = 7;

        board.move(positions, black);

        if(game.inCheckmate(board, white))
            System.out.println("checkmate pass");

        //throw exception
        positions[0] = 3;
        positions[1] = 7;
        positions[2] = 4;
        positions[3] = 7;

        try{
            board.move(positions, white);
        }
            catch(IOException e){
                System.out.println("bad-move-exception pass");
        }

        //in stalemate

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                spots[i][j] = null;
            }
        }
        spots[2][6] = new Queen(white);
        spots[1][5] = new King(white);
        spots[0][7] = new King(black);

        board.setspots(spots);

        if(game.inStalemate(board, black))
            System.out.println("stalemate pass");

        /************************************ test IOExceptions *************************************/
        board = new Board(white, black, true);

        System.out.println("\nIOException tests: ");
        //Invalid move
        positions[0] = 1;
        positions[1] = 1;
        positions[2] = 2;
        positions[3] = 0;
        try{
            board.move(positions, white);
        }
        catch(IOException e){
            System.out.println("Invalid move: "+e.getMessage());
        }

        //wrong player
        positions[0] = 6;
        positions[1] = 1;
        positions[2] = 2;
        positions[3] = 0;
        try{
            board.move(positions, white);
        }
        catch(IOException e1){
            System.out.println("wrong player: "+e1.getMessage());
        }

        //cant eat self
        positions[0] = 0;
        positions[1] = 4;
        positions[2] = 1;
        positions[3] = 4;
        try{
            board.move(positions, white);
        }
        catch(IOException e2){
            System.out.println("cant eat self: "+e2.getMessage());
        }




        /************************************ test fairy pieces *************************************/

        board = new Board(white, black, false);

        System.out.println("\nFairy pieces tests: ");
        //test hopper
        positions[0] = 7;
        positions[1] = 0;
        positions[2] = 5;
        positions[3] = 0;

        board.move(positions, black);

        spots = board.getspots();
        if(spots[5][0].getName().equals("H")){
            System.out.println("Piece.Hopper pass");
        }else  System.out.println("Piece.Hopper not pass");

        //test Piece.Piece.Cannon
        positions[0] = 1;
        positions[1] = 7;
        positions[2] = 3;
        positions[3] = 7;

        board.move(positions, white);

        positions[0] = 7;
        positions[1] = 7;
        positions[2] = 3;
        positions[3] = 7;

        board.move(positions, black);

        spots = board.getspots();
        if(spots[3][7].getName().equals("C")){
            System.out.println("Piece.Piece.Cannon pass");
        }else  System.out.println("Piece.Piece.Cannon not pass");

        /************************************ test Undo *************************************/
        board.undo(black);
        board.undo(white);
        board.undo(black);

        /************************************ test GUI *************************************/

        chessBoard_gui GUI = new chessBoard_gui();



        System.out.println("\nSwitch player test: \n"+GUI.player.printName());
        GUI.switchplayer();
        System.out.println(GUI.player.printName());


    }

}
