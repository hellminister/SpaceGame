/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.stars;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * this is probably abstract
 *
 * @author user
 */
public class Star {

    private static final Logger LOG = Logger.getLogger(Star.class.getName());

    private final String id;
    private int size;
    private Intensity intensity;
    private EffectType effectType;

    private Node sprite;

    public Star(Properties body, String id) {
        this.id = id;
        LOG.log(Level.INFO, "Making {0}", id);
        for (Map.Entry<Object, Object> entry : body.entrySet()) {
            String key = (String) entry.getKey();
            LOG.info(key);
            if ("size".equals(key)) {
                size = Integer.valueOf((String) entry.getValue());
            } else if ("intensity".equals(key)) {
                intensity = Intensity.valueOf((String) entry.getValue());
            } else if ("type".equals(key)) {
                effectType = EffectType.valueOf((String) entry.getValue());
            } else {
                LOG.log(Level.INFO, "{0} Not Treated", key);
            }
        }
    }

    public Node getSprite() {
        if (sprite == null) {
            Circle suns = new Circle(size);
            suns.setFill(Color.YELLOW);

            suns.setOnMouseClicked(e -> {
                LOG.log(Level.INFO, "{0} clicked!", id);
            });
            sprite = suns;
        }

        return sprite;

    }

    public EffectType getEffectType() {
        return effectType;
    }

}
