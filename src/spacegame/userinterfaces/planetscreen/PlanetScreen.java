/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.planetscreen;

import javafx.scene.layout.StackPane;
import spacegame.SpaceGame;
import spacegame.userinterfaces.ReachStartScreen;
import spacegame.world.systems.CelestialBody;

/**
 *
 * @author user
 */
public class PlanetScreen extends ReachStartScreen {

    private final StackPane root;
    private final SpaceGame parent;

    /**
     * initialize the screen scene for the planet interface view
     * @param aThis 
     */
    public PlanetScreen(SpaceGame aThis) {
        super(new StackPane());
        root = (StackPane) this.getRoot();
        parent = aThis;

    }

    @Override
    public void giveFocusBack() {
        /* does nothing yet*/
    }

    public void loadPlanet(CelestialBody t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
