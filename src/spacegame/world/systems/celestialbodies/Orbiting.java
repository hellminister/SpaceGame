/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.celestialbodies;

/**
 *
 * @author user
 */
public class Orbiting {
    
    private double orbitDistance;
    private double yearLength;
    private CelestialBody orbitingBody;

    public Orbiting(double orbitDistance, double yearLength, CelestialBody orbitingBody) {
        this.orbitDistance = orbitDistance;
        this.yearLength = yearLength;
        this.orbitingBody = orbitingBody;
    }

    public double getOrbitDistance() {
        return orbitDistance;
    }

    public double getYearLength() {
        return yearLength;
    }

    public CelestialBody getOrbitingBody() {
        return orbitingBody;
    }
    
}
