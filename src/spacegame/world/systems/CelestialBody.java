/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import javafx.scene.Node;

/**
 *
 * @author user
 */
public abstract class CelestialBody {
    
    protected final String id;
    protected Node sprite;

    /**
     * Sets the id of this Celestial Body
     * @param id 
     */
    public CelestialBody(String id) {
        this.id = id;
    }
    
    public final Node getSprite(){
        if (sprite == null){
            createSprite();
        }
        return sprite;
    }

    protected abstract void createSprite();
    
}
