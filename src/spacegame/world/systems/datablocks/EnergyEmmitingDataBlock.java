/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.datablocks;

import spacegame.util.Utilities;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author user
 */
public class EnergyEmmitingDataBlock extends DataBlock {

    private static final Logger LOG = Logger.getLogger(BasicDataBlock.class.getName());
    private static final int ENERGY_TYPE_POS = 0;
    private static final int ENERGY_INTENSITY_POS = 1;
    private static final int ENERGY_DEGRADATION_POS = 2;

    private static final Set<String> treatedProperties = Utilities.newUnmodifiableSet("energy_emission");

    private Map<EnergyType, Strength> energies;


    EnergyEmmitingDataBlock() {
        super(InfoType.ENERGY_EMMITING);
        energies = new EnumMap<>(EnergyType.class);
    }

    public static Set<String> treatsProperty() {
        return treatedProperties;
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
        switch (prop) {
            case "energy_emission":
                String[] byType = value.split("#");
                LOG.info("energy_emission " + value + " splitted " + Arrays.toString(byType));
                for (String triplet : byType) {
                    String[] values = triplet.split(",");
                    LOG.info("a triplet " + triplet + " splitted " + Arrays.toString(values));
                    energies.put(EnergyType.valueOf(values[ENERGY_TYPE_POS]),
                            new Strength(Double.valueOf(values[ENERGY_INTENSITY_POS]), Double.valueOf(values[ENERGY_DEGRADATION_POS])));
                }
                break;
            default:
                LOG.warning("No treatment for property : " + prop + " with value : " + value);
        }
    }

    public double getEnergyStrength(EnergyType type, double distance) {
        Strength energyStrength = energies.get(type);
        return energyStrength == null ? 0.0 : energyStrength.getStrength(distance);
    }


    @Override
    public boolean checkIntegrity(BubbleSystem system, CelestialBody me) {
        return true;
    }

    private static class Strength {
        private double intensity;
        private double degradingRate;

        private Strength(double intens, double deg) {
            intensity = intens;
            degradingRate = deg;
        }

        double getStrength(double distance) {
            return Math.max((intensity - (degradingRate * Math.abs(distance))), 0.0);
        }

    }
}
