/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import spacegame.userinterfaces.systemscreen.SystemScreenSprite;
import spacegame.world.systems.datablocks.BasicDataBlock;
import spacegame.world.systems.datablocks.InfoType;
import spacegame.world.systems.datablocks.DataBlock;

import java.util.EnumMap;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class CelestialBody {

    private static final Logger LOG = Logger.getLogger(CelestialBody.class.getName());

    private final EnumMap<InfoType, DataBlock> composingData;


    private final BaseBodyType type;

    private boolean isWellFormed = true;


    public CelestialBody(String id, Properties body) {
        // if the field isnt correct (ie null or not corresponding to an actual type)
        // i want the program to crash
        body.put("id", id);
        type = BaseBodyType.valueOf(body.getProperty("type"));
        composingData = new EnumMap<>(InfoType.class);
        for (InfoType infoType: type.getRequiresInfoBlock()) {
            composingData.put(infoType, infoType.createBlock());
        }

        for (String property : body.stringPropertyNames()){
            String value = body.getProperty(property);
            InfoType goesWith = InfoType.treats(property);

            if (goesWith != null) {
                DataBlock block = composingData.computeIfAbsent(goesWith, InfoType::createBlock);
                block.treatProperty(property, value);
            } else {
                LOG.warning("property : " + property + " is not treated by any of the existing info blocks");
            }
        }

    }

    void checkIntegrity(BubbleSystem system) {
        // even if the first one isnt correct, still need to check them all so the report can be complete
        for (DataBlock data : composingData.values()){
            isWellFormed &= data.checkIntegrity(system, this);
        }
    }

    public String getId() {
        return ((BasicDataBlock)composingData.get(InfoType.BASIC)).getName();
    }

    public BaseBodyType getType() {
        return type;
    }

    public boolean isWellFormed() {
        return isWellFormed;
    }


    public SystemScreenSprite getSystemScreenSprite() {
        return (SystemScreenSprite)composingData.get(InfoType.SPRITE);
    }
}
