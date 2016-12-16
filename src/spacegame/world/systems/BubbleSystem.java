/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import spacegame.world.systems.celestialbodies.Planet;
import spacegame.world.systems.stars.Star;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import spacegame.SpaceGame;
import spacegame.world.systems.celestialbodies.Orbiting;

/**
 * This class represents a whole system with its stars and planets
 *
 * @author user
 */
public class BubbleSystem {

    private static final Logger LOG = Logger.getLogger(BubbleSystem.class.getName());

    private final String systemName;

    private Star star;

    private final List<Orbiting> planets;

    private StackPane systemPane;

    /**
     * Creates the star system from the given properties
     * @param t
     * @param currentSystemState 
     */
    public BubbleSystem(Map.Entry<String, Properties> t, Map<String, Properties> currentSystemState) {
        systemName = t.getKey();
        planets = new LinkedList<>();
        LOG.info(systemName);
        Properties props = t.getValue();
        LOG.info(props.stringPropertyNames().toString());
        for (String s : props.stringPropertyNames()){
            LOG.log(Level.INFO, "{0} {1}", new Object[]{s, props.getProperty(s)});
            if (s.startsWith("contains")){
                String key = systemName + "." + props.getProperty(s);
                Properties body = currentSystemState.get(key);
                instanciateObject(body, key, currentSystemState);
            }
        }
    }

    public String getName() {
        return systemName;
    }

    public Node getNode(SpaceGame mainTheater) {
//        if (systemPane == null) {
//            systemPane = new StackPane();
//            systemPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
//            systemPane.setPickOnBounds(false);
//            Node suns = star.getSprite();
//            systemPane.getChildren().add(suns);
//            StackPane.setAlignment(suns, Pos.CENTER);
//            planets.forEach((t, u) -> {
//                Node body = t.getSprite();
//                body.setTranslateX(u);
//                body.setOnMouseClicked(event -> {
//                    mainTheater.changeSceneToPlanetScreen(t);
//                });
//                systemPane.getChildren().add(body);
//            });
//        }
        return systemPane;
    }

    private void instanciateObject(Properties body, String id, Map<String, Properties> currentSystemState) {
        String type = body.getProperty("class");
        LOG.info(type);
        switch (type) {
            case "Star" :
                star = new Star(body, id);
                break;
            case "Planet" :
                double orbit = Double.parseDouble(body.getProperty("orbit", "300"));
                double yearLength = Double.parseDouble(body.getProperty("yearLength", "1.0"));
                planets.add(new Orbiting(orbit, yearLength,new Planet(body, id, currentSystemState)));
                break;
            default:
                LOG.warning(type);
        }
    }

}
