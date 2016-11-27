/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import spacegame.world.systems.BubbleSystem;

/**
 * This class contains the world unmodified by the player
 * This is the initial state of the world for a new game
 * The modified states will be in GameState and will combine with this object 
 *   to make the current world state
 * @author user
 */
public class GameWorld {
    
    private static final Logger LOG = Logger.getLogger(GameWorld.class.getName());
    
    private GameState currentState;
    
    private Map<String, BubbleSystem> systemList;
    
    public GameWorld(){
        systemList = new HashMap<>();
        
        BubbleSystem sys = new BubbleSystem("test");
        
        systemList.put(sys.getName(), sys);
        
        currentState = null;
    }

    public void setPlayerState(GameState gameData) {
        currentState = gameData;
    }

    public GameState getPlayerState() {
        return currentState;
    }
    
    public BubbleSystem getSystem(){
        return systemList.get(currentState.getPlayerState().getCurrentSystem());
    }
}
