package Piece;

import javax.swing.*;
import Logic.Player;
public class King extends Piece {

    private boolean alive;
    private Player player;
    private String name;
    private boolean first_move;

    public King(Player player){
        this.player = player;
        this.alive = true;
        name = "K";
    }

    @Override
    public String getName(){
        return name;
    }

    public String printName(){
        return "King";
    }

    public void setFirst_move(boolean first_move){
        this.first_move = first_move;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean check_valid_move(Piece[][] spots, int srcX, int srcY, int destX, int destY){
        if(Math.abs(destX - srcX) > 1 || Math.abs(destY - srcY) > 1){
            return false;
        }

        return true;
    }

    public void setIcon() {
        if (getPlayer().IsWhite()) {
            icon = new ImageIcon(getClass().getResource("/images/king_w.png"));
        } else {
            icon = new ImageIcon(getClass().getResource("/images/king_b.png"));
        }
    }
}
