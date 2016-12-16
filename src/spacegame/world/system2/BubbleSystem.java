/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.system2;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class BubbleSystem {
    
    private Map<String, CelestialBody> celestialBodies;
    
    public BubbleSystem(){
        celestialBodies = new HashMap<>();
    }
}
