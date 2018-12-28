package Piece;

import javax.swing.*;
import Logic.Player;

public class Pawn extends Piece {

    private boolean alive;
    private Player player;
    private boolean first_move;
    private String name;

    public Pawn(Player player){
        this.player = player;
        this.alive = true;
        this.first_move = true;
        name = "P";
    }

 
    
    @Override
    public String getName(){
        return name;
    }

    public String printName(){
        return "Pawn";
    }

    public Player getPlayer(){
        return this.player;
    }

    public void setFirst_move(boolean first_move){
        this.first_move = first_move;
    }

    public boolean check_valid_move(Piece[][] spots, int srcX, int srcY, int destX, int destY){

        //no retreats
        if(player.IsWhite()){
            if(srcX > destX){
                return false;
            }
        }else{
            if(srcX < destX){
                return false;
            }
        }

        //move straight forward
        if(destY == srcY){
            
            int stepX = Math.abs(destX - srcX);
            
            if( stepX > 2){
                return false;
            }

            //check if there is piece ahead
            else if( stepX == 1) {
                if (spots[destX][destY] !=null)
                    return false;
            
            }
            
            //maybe first move
            else if(stepX == 2){
                if(first_move == false){
                    return false;
                }

                if(spots[destX][destY] != null  )
                        return false;

                if(player.IsWhite()){
                    if(spots[destX-1][destY] != null)
                        return false;
                }else{
                    if(spots[destX+1][destY] != null)
                        return false;
                }
            }
        }
        else{
            //taking piece
            if(Math.abs(destY - srcY) != 1 || Math.abs(destX - srcX) != 1){
                return false;
            }
            
            //if no piece
            if(spots[destX][destY] == null){
                return false;
            }
        }
        
        return true;

    }

    public void setIcon() {
        if (getPlayer().IsWhite()) {
            icon = new ImageIcon(getClass().getResource("/images/pawn_w.png"));
        } else {
            icon = new ImageIcon(getClass().getResource("/images/pawn_b.png"));
        }
    }
}
