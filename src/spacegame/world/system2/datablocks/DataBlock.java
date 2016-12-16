/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.system2.datablocks;

import java.util.Properties;
import spacegame.world.system2.BubbleSystem;

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

    public abstract Properties toProperties();
    
    public abstract void treatProperty(String prop, BubbleSystem system);
}
