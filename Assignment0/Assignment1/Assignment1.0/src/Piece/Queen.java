package Piece;
import Logic.Player;

import javax.swing.*;

public class Queen extends Piece {

    private boolean alive;
    private Player player;
    private String name;
    private boolean first_move;

    public Queen(Player player) {
        this.player = player;
        this.alive = true;
        name = "Q";
    }

    @Override
    public String getName(){
        return name;
    }

    public String printName(){
        return "Queen";
    }

    public void setFirst_move(boolean first_move){
        this.first_move = first_move;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean check_valid_move(Piece[][] spots, int srcX, int srcY, int destX, int destY) {

        return new Rook(player).check_valid_move(spots, srcX, srcY, destX, destY)
                || new Bishop(player).check_valid_move(spots, srcX, srcY, destX, destY);
    }

    public void setIcon() {
        if (getPlayer().IsWhite()) {
            icon = new ImageIcon(getClass().getResource("/images/queen_w.png"));
        } else {
            icon = new ImageIcon(getClass().getResource("/images/queen_b.png"));
        }
    }
}