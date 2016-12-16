/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.celestialbodies;

import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author user
 */
public class Planet extends CelestialBody {
    
    private static final Logger LOG = Logger.getLogger(Planet.class.getName());
    
    private int size;
    private Habitability habitability;
    private EnumSet<Building> buildings;
    
    /**
     * Creates the planet from the given properties
     * @param body
     * @param id 
     */
    public Planet(Properties body, String id, Map<String, Properties> currentSystemState) {
        super(id);
        buildings = EnumSet.noneOf(Building.class);
        for (Map.Entry<Object, Object> entry : body.entrySet()) {
            String key = (String) entry.getKey();
            
            if ("size".equals(key)) {
                size = Integer.valueOf((String) entry.getValue());
            } else if ("habitability".equals(key)) {
                habitability = Habitability.valueOf((String) entry.getValue());
            } else if (key.startsWith("building")) {
                buildings.add(Building.valueOf((String) entry.getValue()));
            } else {
                LOG.log(Level.INFO, "{0} Not Treated", key);
            }
        }
    }
    
    @Override
    protected void createSprite() {
        if (sprite == null) {
            Circle planet = new Circle(size);
            planet.setFill(Color.DARKGREEN);

            planet.setOnMouseClicked(e -> {
                LOG.log(Level.INFO, "{0} clicked!", id);
            });
            sprite = planet;
        }
    }
    
    public boolean hasBuilding(Building building){
        return buildings.contains(building);
    }
    
}
