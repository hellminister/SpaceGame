/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import java.awt.Paint;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import spacegame.userinterfaces.systemscreen.SystemScreen;

/**
 * This class represents a whole system with its stars and planets
 * @author user
 */
public class BubbleSystem {
    
    private static final Logger LOG = Logger.getLogger(BubbleSystem.class.getName());
    
    private final String systemName;
    
    private Star star;
    
    private List<CelestialBody> planets;
    
    private StackPane systemPane = null;

    public BubbleSystem(String systemName) {
        this.systemName = systemName;
        planets = new LinkedList<>();
        star = new Star();
    }

    public String getName() {
        return systemName;
    }

    public void draw(SystemScreen aThis) {
        if (systemPane == null){
            systemPane = new StackPane();
            systemPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            systemPane.setPickOnBounds(false);
            star.draw(systemPane);
        }
        aThis.addSystem(systemPane);
    }
    
    
}
