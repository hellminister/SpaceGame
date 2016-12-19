/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.datablocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author user
 */
public enum InfoType {
    ARTIFICIAL(ArtificialDataBlock::new, ArtificialDataBlock::treatsProperty),
    BASIC(BasicDataBlock::new, BasicDataBlock::treatsProperty),
    ENERGY_EMMITING(EnergyEmmitingDataBlock::new, EnergyEmmitingDataBlock::treatsProperty),
    FIELD(FieldDataBlock::new, FieldDataBlock::treatsProperty),
    GATE(GateDataBlock::new, GateDataBlock::treatsProperty),
    LANDABLE(LandableDataBlock::new, LandableDataBlock::treatsProperty),
    MINABLE(MinableDataBlock::new, MinableDataBlock::treatsProperty),
    OWNED(OwnedDataBlock::new, OwnedDataBlock::treatsProperty),
    SPRITE(SpriteDataBlock::new, SpriteDataBlock::treatsProperty);

    private static Map<String, InfoType> treatedProperties;

    static {
        treatedProperties = new HashMap<>();
        Arrays.stream(values())
                .forEach(infoType -> infoType.treatedProperty.getTreatingProperty().stream()
                        .forEach(s -> treatedProperties.put(s, infoType)));
    }

    private final DataBlockCreator creator;
    private final TreatingProperty treatedProperty;

    InfoType(DataBlockCreator creator, TreatingProperty treatingProperty) {
        this.creator = creator;
        treatedProperty = treatingProperty;
    }

    /**
     * Creates the corresponding data block for this type
     * @return 
     */
    public DataBlock createBlock() {
        return creator.create();
    }

    public static InfoType treats(String property) {
        return treatedProperties.get(property);
    }

    @FunctionalInterface
    private interface DataBlockCreator {
        DataBlock create();
    }

    @FunctionalInterface
    private interface TreatingProperty {
        Set<String> getTreatingProperty();
    }
}
