package Piece;
import Logic.Player;

import javax.swing.*;

public class Cannon extends Piece {

    //piece that only move like rook but taking piece only leaping over other piece
    private boolean alive;
    private Player player;
    private boolean first_move;
    private String name;

    public Cannon(Player player){
        this.player = player;
        this.alive = true;
        this.first_move = true;
        name = "C";
    }



    @Override
    public String getName(){
        return name;
    }

    public String printName(){
        return "Cannon";
    }

    public void setFirst_move(boolean first_move){
        this.first_move = first_move;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean check_valid_move(Piece[][] spots, int srcX, int srcY, int destX, int destY) {


        //check straight
        if(srcX != destX && srcY != destY){
            return false;
        }

        if(spots[destX][destY] != null){
            //taking piece
            int step;//for loop
            if (srcY != destY) {
                //check horizontally if there is piece

                if (srcY < destY)
                    step = 1;
                else
                    step = -1;

                for (int i = srcY + step; i != destY; i += step) {
                    if (spots[destX][i] != null)
                        return true;
                }
            }

            if (srcX != destX) {
                //check vertically if there is piece

                if (srcX < destX)
                    step = 1;
                else
                    step = -1;

                for (int i = srcX + step; i != destX; i += step) {
                    if (spots[destY][i] != null)
                        return true;
                }

            }
            return false;
        }else {

            int step;//for loop
            if (srcY != destY) {
                //check horizontally if there is piece

                if (srcY < destY)
                    step = 1;
                else
                    step = -1;

                for (int i = srcY + step; i != destY; i += step) {
                    if (spots[destX][i] != null)
                        return false;
                }
            }

            if (srcX != destX) {
                //check vertically if there is piece

                if (srcX < destX)
                    step = 1;
                else
                    step = -1;

                for (int i = srcX + step; i != destX; i += step) {
                    if (spots[destY][i] != null)
                        return false;
                }

            }

            return true;
        }
    }

    public void setIcon() {
        if (getPlayer().IsWhite()) {
            icon = new ImageIcon(getClass().getResource("/images/cannon_w.png"));
        } else {
            icon = new ImageIcon(getClass().getResource("/images/cannon_b.png"));
        }
    }
}
