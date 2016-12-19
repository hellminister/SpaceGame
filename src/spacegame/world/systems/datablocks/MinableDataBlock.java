/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.datablocks;

import spacegame.util.Utilities;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

import java.util.Properties;
import java.util.Set;

/**
 *
 * @author user
 */
public class MinableDataBlock extends DataBlock{

    private static final Set<String> treatedProperties = Utilities.newUnmodifiableSet();
    public static Set<String> treatsProperty(){
        return treatedProperties;
    }

    MinableDataBlock() {
        super(InfoType.MINABLE);
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
    public void treatProperty(String prop, String value) {
    }

    @Override
    public boolean checkIntegrity(BubbleSystem system, CelestialBody me) {
        return true;
    }

}
