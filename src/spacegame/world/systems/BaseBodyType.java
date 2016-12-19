/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import spacegame.world.systems.datablocks.InfoType;

import static spacegame.world.systems.datablocks.InfoType.*;

/**
 *
 * @author user
 */
public enum BaseBodyType {
    STAR(BASIC, SPRITE, ENERGY_EMMITING),
    PLANETOID(BASIC, LANDABLE, SPRITE),
    GASOUS_PLANETOID(BASIC, SPRITE),
    WARP_GATE(BASIC, SPRITE, GATE),
    ASTEROID(BASIC, SPRITE, MINABLE, FIELD),
    SPACE_STATION(BASIC, SPRITE, OWNED, LANDABLE, ARTIFICIAL),
    ;
    
    private final EnumSet<InfoType> requiredInfoBlock;
    
    BaseBodyType(InfoType... types){
        requiredInfoBlock = EnumSet.copyOf(Arrays.asList(types));
    }
    
    public Set<InfoType> getRequiresInfoBlock(){
        return Collections.unmodifiableSet(requiredInfoBlock);
    }
}
