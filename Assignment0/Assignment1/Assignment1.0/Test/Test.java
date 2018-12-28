/*import java.io.IOException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException {

        //initializing players
        Logic.Player white = new Logic.Player("White");
        Logic.Player black = new Logic.Player("Black");

        Logic.Board board = new Logic.Board(white, black, true);
        Scanner scanner = new Scanner(System.in);

        int[] positions = new int[4];
        Piece.Piece[][] spots = board.getspots();

        System.out.println("Hello! Welcome to the chess board. A new game has been started.\n");

        board.printBoard();

        boolean whiteWin = false,
                blackWin = false,
                draw = false;
        String player1 = "White, ";
        String player2 = "Black, ";
        String name = player1;
        Logic.Player player = white;

        while (!whiteWin && !blackWin && !draw) {
            System.out.print(name);
            MoveCommand(scanner, positions, spots, player, board);

            board.printBoard();

            whiteWin = board.getKingPos(black) == -1 || inCheckmate(board, black) ;
            blackWin = board.getKingPos(white) == -1 || inCheckmate(board, white) ;
            draw = inStalemate(board, white) || inStalemate(board, black);

            if(player.IsWhite()){
                name = player2;
                player = black;
            }else{
                name = player1;
                player = white;
            }
        }

        if(blackWin){
            System.out.print("Congratulations! Player2 won!");
        }else if(whiteWin){
            System.out.print("Congratulations! Player1 won!");
        }else{
            System.out.print("Logic.Game over! Draw!");
        }


    }


    private static void MoveCommand(Scanner scanner, int[] positions, Piece.Piece[][] spots, Logic.Player player, Logic.Board board) {
        System.out.println("What piece would you like to move? Please enter the num of row.(1-8)");
        getPosition(scanner, positions, 0, "X");

        System.out.println("Please enter  num of col. (1-8)");
        getPosition(scanner, positions, 1, "Y");

        System.out.println("What is the row of the destination.(1-8)");
        getPosition(scanner, positions, 2, "X");

        System.out.println("Please enter the col of the destination. (1-8)");
        getPosition(scanner, positions, 3, "Y");


        tryMove(scanner, positions, spots, player, board);

        if(positions[0] == positions[2] && positions[1] == positions[3]){
            System.out.println("Pls don't move to current position. Try again.");
            MoveCommand(scanner,  positions, spots, player, board);
        }

    }

    private static int[] putPosition(int a, int b, int c, int d, int[] pos){
        pos[0] = a;
        pos[1] = b;
        pos[2] = c;
        pos[3] = d;
        return pos;
    }

    private static void tryMove(Scanner scanner,int[] positions, Piece.Piece[][] spots, Logic.Player player, Logic.Board board) {
        if(spots[positions[0]][positions[1]] == null){
            System.out.println("Move unsuccessfully.\n");
            MoveCommand(scanner,  positions, spots, player, board);
            return;
        }

        if(spots[positions[0]][positions[1]].check_valid_move(spots, positions[0], positions[1], positions[2], positions[3])){
            //if valid
            try{
                board.move(positions, player);
                System.out.println("Move Successfully.\n");
            }
            catch (IOException e) {
                System.out.println("exception.\n");
            }

        }else{
            System.out.println("Move unsuccessfully.\n");
            MoveCommand(scanner,  positions, spots, player, board);
        }
    }

    private static void getPosition(Scanner scanner, int[] positions, int i, String axis) {
        positions[i] = scanner.nextInt() -1;
        while(positions[i]>7 || positions[i]<0){
            System.out.println("Invalid "+axis+".Please re-enter.");
            positions[i] = scanner.nextInt();
        }
    }

    private static boolean inCheckmate(Logic.Board board, Logic.Player player){
        if (!board.inCheck(player)) {
            return false;
        }

        try {
            return !resolvedCheck(player, board, board.getplayerlist(player), board.getCheckingPiece(player));
        } catch (IOException e){
            //do nothing
        }

        return false;
    }

    private static boolean resolvedCheck(Logic.Player player, Logic.Board board, int[] pieceList, int[] checkingPiece) throws IOException {

        Logic.Board copy_board = new Logic.Board(board);
       // Piece.Piece[][] spots = copy_board.getspots();
        int[] positions = new int[4];

        for (int aPieceList : pieceList) {

                for(int k = 0 ; k < 8; k++){
                    for(int l = 0; l < 8; l++){
                    putPosition(aPieceList / 10,aPieceList % 10,k,l, positions);
                    try{
                        copy_board.move(positions, player);
                        if(!board.inCheck(player)){
                            return true;
                        }
                    }
                    catch(IOException e){
                        //do nothing
                        }
                        copy_board = board;
                    }
                }

        }
            return false;

        //try eat threatening piece to resolve checkmate



    }

    private static boolean inStalemate(Logic.Board board, Logic.Player player){
        if (board.inCheck(player)) {
            return false;
        }

        //try legal move
        Piece.Piece[][] spots = board.getspots();
        int myPieceX, myPieceY;
        int[] playerList = board.getplayerlist(player);

        for (int aPlayerList : playerList) {
            if (aPlayerList >= 0) {
                myPieceX = aPlayerList / 10;
                myPieceY = aPlayerList % 10;
                //Check this piece against every other piece...

                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        if (spots[myPieceX][myPieceY].check_valid_move(spots,
                                myPieceX, myPieceY, j, k)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    private static class TryInsert {
        private boolean myResult;
        private Logic.Player player;
        private Logic.Board board;
        private Logic.Board copy_board;
        private Piece.Piece[][] spots;
        private int[] positions;
        private int kingY;
        private int checkingY;

        public TryInsert(Logic.Player player, Logic.Board board, Logic.Board copy_board, Piece.Piece[][] spots, int[] positions, int kingY, int checkingY) {
            this.player = player;
            this.board = board;
            this.copy_board = copy_board;
            this.spots = spots;
            this.positions = positions;
            this.kingY = kingY;
            this.checkingY = checkingY;
        }

        boolean is() {
            return myResult;
        }

        public Logic.Board getCopy_board() {
            return copy_board;
        }

        public TryInsert invoke() {
            for (int k = Math.min(kingY, checkingY); k < Math.max(kingY, checkingY); k++) {
                positions[3] = k;  //destY

                if (spots[positions[0]][positions[1]].check_valid_move(spots,
                        positions[0], positions[1], positions[0], k)) {
                    //try every k between
                    try {
                        copy_board.move(positions, player);
                        if (!copy_board.inCheck(player)) {
                            myResult = true;
                            return this;
                        } else {
                            copy_board = board;
                        }
                    } catch (IOException e) {
                        copy_board = board;
                    }

                }
            }
            myResult = false;
            return this;
        }
    }
}
    /*for (int k = Math.min(kingX, checkingX); k < Math.max(kingX, checkingX); k++) {
                            positions[2] = k;  //destX

                            if (spots[positions[0]][positions[1]].check_valid_move(spots,
                                    positions[0], positions[1], k, positions[1])) {
                                //try every k between
                                try {
                                    copy_board.move(positions, player);
                                    if (!copy_board.inCheck(player)) {
                                        return true;
                                    } else {
                                        copy_board = board;
                                    }
                                } catch (IOException e) {
                                    copy_board = board;
                                }

                            }
                        }*/
 /* for (int aCheckingPiece1 : checkingPiece) {
            for (int aPieceList : pieceList) {

                if(aPieceList >= 0 && aCheckingPiece1 >= 0){
                    positions[0] = aPieceList / 10; //my x
                    positions[1] = aPieceList % 10; //my y

                    positions[2] = aCheckingPiece1 / 10; // threatening piece x
                    positions[3] = aCheckingPiece1 % 10; // threatening piece y

                    //System.out.println(positions[0]+" "+positions[1]+ " " +positions[2]+" "+positions[3]);
                    if (spots[positions[0]][positions[1]].check_valid_move(spots,
                            positions[0], positions[1], positions[2], positions[3]))
                        try {
                            copy_board.move(positions, player);
                            if (!copy_board.inCheck(player)) {
                                return true;
                            } else {
                                copy_board = board;
                            }
                        } catch (IOException e) {
                            copy_board = board;
                        }
                }
            }
        }


        //try move king to avoid
        copy_board = new Logic.Board(board);

        int kingX = board.getKingPos(player)/10;
        int kingY = board.getKingPos(player)%10;

        positions[0] = kingX;
        positions[1] = kingY;

        for(int i = Math.max(kingX-1, 0); i <= Math.min(kingX+1, 7); i++){
            for(int j = Math.max(kingY-1, 0); j <= Math.min(kingY+1, 7); j++){
                positions[2] = i;
                positions[3] = j;

                //System.out.println(positions[0]+" "+positions[1]+ " " +positions[2]+" "+positions[3]);
                //System.out.println(spots[2][4]);

                if(spots[kingX][kingY].check_valid_move(spots,
                        kingX, kingY, i, j))
                    try {
                        copy_board.move(positions, player);
                        System.out.println(spots[2][4]);
                        if(!copy_board.inCheck(player)){
                            return true;
                        }else{
                            copy_board = board;
                            System.out.println(spots[2][4]);
                        }
                    }catch (IOException e){
                        copy_board = board;
                        System.out.println(spots[2][4]);
                    }
            }
        }

        //try block in middle
        copy_board = new Logic.Board(board);
        int checkingY, checkingX;

        for (int aCheckingPiece : checkingPiece) {
            for (int j = 0; j < pieceList.length; j++) {

                positions[0] = pieceList[j] / 10; //my x
                positions[1] = pieceList[j] % 10; //my y

                checkingX = aCheckingPiece / 10; // threatening piece x
                checkingY = aCheckingPiece % 10; // threatening piece y

                positions[2] = checkingX;
                positions[3] = checkingY;


                System.out.println(positions[0]+" "+positions[1]+ " " +positions[2]+" "+positions[3]);
                if(pieceList[j]>=0 && aCheckingPiece >= 0){
                    String name = spots[positions[2]][positions[3]].getName();
                    if (!name.equals("P") && !name.equals("N")) {
                        //same rank or file
                        if (positions[0] == positions[2]) {
                            //find y
                            //from king to threatening piece
                            TryInsert tryInsert = new TryInsert(player, board, copy_board, spots, positions, kingY, checkingY).invoke();
                            if (tryInsert.is()) return true;
                            copy_board = tryInsert.getCopy_board();

                        } else if (positions[1] == positions[3]) {
                            //find x
                            //from king to threatening piece
                            TryInsert tryInsert = new TryInsert(player, board, copy_board, spots, positions, kingX, checkingX).invoke();
                            if (tryInsert.is()) return true;
                            copy_board = tryInsert.getCopy_board();

                        } else {
                            //find diagonal

                            //from king to threatening piece
                            int l = Math.min(kingY, checkingY);
                            for (int k = Math.min(kingX, checkingX); k < Math.max(kingX, checkingX); k++) {
                                positions[2] = k;  //destX
                                positions[3] = l;  //destY

                                if (spots[positions[0]][positions[1]].check_valid_move(spots,
                                        positions[0], positions[1], k, l)) {
                                    //try every k between
                                    try {
                                        copy_board.move(positions, player);
                                        if (!copy_board.inCheck(player)) {
                                            return true;
                                        } else {
                                            copy_board = board;
                                        }
                                    } catch (IOException e) {
                                        copy_board = board;
                                    }

                                }
                                j += 1;
                            }
                        }
                    }
                }
            }
        }
        */
