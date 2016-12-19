/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.datablocks;

import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

import java.util.Properties;
import java.util.Set;

/**
 *
 * @author user
 */
public abstract class DataBlock {
    
    protected InfoType dataBlockType;
    
    protected DataBlock(InfoType type){
        dataBlockType = type;
    }

    public final InfoType getDataBlockType(){
        return dataBlockType;
    }

    abstract Set<String> treatsInfo();

    public abstract Properties toProperties();
    
    public abstract void treatProperty(String prop, String value);

    public abstract boolean checkIntegrity(BubbleSystem system, CelestialBody me);
}
