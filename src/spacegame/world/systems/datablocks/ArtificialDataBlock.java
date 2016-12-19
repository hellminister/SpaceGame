/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.datablocks;

import java.util.Properties;
import java.util.Set;

import spacegame.util.Utilities;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

/**
 *
 * @author user
 */
public class ArtificialDataBlock extends DataBlock{

    private static final Set<String> treatedProperties = Utilities.newUnmodifiableSet();
    public static Set<String> treatsProperty(){
        return treatedProperties;
    }

    ArtificialDataBlock() {
        super(InfoType.ARTIFICIAL);
    }



    @Override
    Set<String> treatsInfo() {
        return treatsProperty();
    }

    @Override
    public Properties toProperties() {
        return new Properties();
    }

    @Override
    public void treatProperty(String prop, String system) {
        
    }

    @Override
    public boolean checkIntegrity(BubbleSystem system, CelestialBody me) {
        return true;
    }


}
