/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Logger;


/**
 *
 * @author user
 */
public class GameState implements Serializable{

    private static final Logger LOG = Logger.getLogger(GameState.class.getName());
    private static final long serialVersionUID = 1L;
    
    /**
     * Contains the players info
     * @serial
     */
    private PlayerInfo playerInfo;

    public GameState(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }
    
    //temporary, will have to be delete
    public GameState(){
        
    }
    
    public String getInfo() {
        return playerInfo.getInfo();
    }
    
    private void writeObject(ObjectOutputStream s) throws IOException{
        s.defaultWriteObject();
        
        s.writeObject(playerInfo);
        
    }
    
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException{
        s.defaultReadObject();
        
        playerInfo = (PlayerInfo) s.readObject();
    }

    public String getFullName() {
        return playerInfo.getFullName();
    }

}
