/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javafx.scene.Node;
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
    private EnumSet<Building> building;
        
    public Planet(Properties body, String id) {
        super(id);
        building = EnumSet.noneOf(Building.class);
        for (Map.Entry<Object, Object> entry : body.entrySet()) {
            String key = (String) entry.getKey();
            
            if ("size".equals(key)) {
                size = Integer.valueOf((String) entry.getValue());
            } else if ("habitability".equals(key)) {
                habitability = Habitability.valueOf((String) entry.getValue());
            } else if (key.startsWith("building")) {
                building.add(Building.valueOf((String) entry.getValue()));
            } else {
                LOG.info(key + " Not Treated");
            }
        }
    }
    
    @Override
    protected void createSprite() {
        if (sprite == null) {
            Circle planet = new Circle(size);
            planet.setFill(Color.DARKGREEN);

            planet.setOnMouseClicked(e -> {
                LOG.info(id + " clicked!");
            });
            sprite = planet;
        }
    }
    
}
