/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * This class represents a whole system with its stars and planets
 *
 * @author user
 */
public class BubbleSystem {

    private static final Logger LOG = Logger.getLogger(BubbleSystem.class.getName());

    private final String systemName;

    private Star star;

    private final Map<CelestialBody, Integer> planets;

    private StackPane systemPane;

    /**
     * Creates the star system from the given properties
     * @param t
     * @param currentSystemState 
     */
    public BubbleSystem(Map.Entry<String, Properties> t, Map<String, Properties> currentSystemState) {
        systemName = t.getKey();
        planets = new HashMap<>();
        LOG.info(systemName);
        Properties props = t.getValue();
        LOG.info(props.stringPropertyNames().toString());
        for (String s : props.stringPropertyNames()){
            LOG.log(Level.INFO, "{0} {1}", new Object[]{s, props.getProperty(s)});
            if (s.startsWith("contains")){
                String key = systemName + "." + props.getProperty(s);
                Properties body = currentSystemState.get(key);
                instanciateObject(body, key);
            }
        }
    }

    public String getName() {
        return systemName;
    }

    public Node getNode() {
        if (systemPane == null) {
            systemPane = new StackPane();
            systemPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            systemPane.setPickOnBounds(false);
            Node suns = star.getSprite();
            systemPane.getChildren().add(suns);
            StackPane.setAlignment(suns, Pos.CENTER);
            planets.forEach((t, u) -> {
                Node body = t.getSprite();
                body.setTranslateX(u);
                systemPane.getChildren().add(body);
            });
        }
        return systemPane;
    }

    private void instanciateObject(Properties body, String id) {
        String type = body.getProperty("class");
        LOG.info(type);
        switch (type) {
            case "Star" :
                star = new Star(body, id);
                break;
            case "Planet" :
                int orbit = Integer.parseInt(body.getProperty("orbit"));
                planets.put(new Planet(body, id), orbit);
                break;
            default:
                LOG.warning(type);
        }
    }

}
