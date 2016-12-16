/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.system2;

import spacegame.world.system2.datablocks.InfoType;
import spacegame.world.system2.datablocks.DataBlock;
import java.util.EnumMap;

/**
 *
 * @author user
 */
public class CelestialBody {
    
    private EnumMap<InfoType, DataBlock> composingData;
    private BaseBodyType type;
    
    public CelestialBody(BaseBodyType bbType){
        type = bbType;
        composingData = new EnumMap<>(InfoType.class);
    }
    
    
}
