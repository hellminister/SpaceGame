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
public class SpriteDataBlock extends DataBlock{

    SpriteDataBlock() {
        super(InfoType.SPRITE);
    }

    @Override
    public Properties toProperties() {
        return new Properties();
    }

    @Override
    public void treatProperty(String prop, BubbleSystem system) {
    }
    
}
