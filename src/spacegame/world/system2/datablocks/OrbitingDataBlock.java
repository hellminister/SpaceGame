/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.system2.datablocks;

import java.util.Properties;
import spacegame.world.system2.BubbleSystem;
import spacegame.world.system2.CelestialBody;

/**
 *
 * @author user
 */
public class OrbitingDataBlock extends DataBlock{

    
    private CelestialBody gravityCenter;
    private double orbitDistance;
    private double angularSpeed;
    
    OrbitingDataBlock() {
        super(InfoType.ORBITING);
    }

    @Override
    public Properties toProperties() {
        return new Properties();
    }

    @Override
    public void treatProperty(String prop, BubbleSystem system) {
        
    }
    
}
