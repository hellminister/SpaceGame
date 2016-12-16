/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.system2.datablocks;

/**
 *
 * @author user
 */
public enum InfoType {
    ARTIFICIAL(ArtificialDataBlock::new),
    BASIC(BasicDataBlock::new),
    ENERGY_EMMITING(EnergyEmmitingDataBlock::new),
    FIELD(FieldDataBlock::new),
    GATE(GateDataBlock::new),
    LANDABLE(LandableDataBlock::new),
    MINABLE(MinableDataBlock::new),
    ORBITING(OrbitingDataBlock::new),
    OWNED(OwnedDataBlock::new),
    SPRITE(SpriteDataBlock::new);

    private final DataBlockCreator creator;

    InfoType(DataBlockCreator creator) {
        this.creator = creator;
    }

    /**
     * Creates the corresponding data block for this type
     * @return 
     */
    public DataBlock createBlock() {
        return creator.create();
    }

    @FunctionalInterface
    private static interface DataBlockCreator {

        DataBlock create();
    }
}
