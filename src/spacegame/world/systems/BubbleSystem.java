/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class BubbleSystem {

    private static final Logger LOG = Logger.getLogger(BubbleSystem.class.getName());

    private final String systemName;
    private Map<String, CelestialBody> celestialBodies;
    
    public BubbleSystem(Map.Entry<String, Properties> t, Map<String, Properties> currentSystemState){
        systemName = t.getKey();
        celestialBodies = new HashMap<>();

        Properties props = t.getValue();
        for (String s : props.stringPropertyNames()){
            if ("contains".equals(s)){
                String containedIds = props.getProperty((s),"");
                for (String id : containedIds.split(",")) {
                    String key = systemName + "." + id.trim();
                    Properties body = currentSystemState.get(key);
                    instanciateObject(body, key);
                }
            }
        }
        celestialBodies.forEach((s, celestialBody) -> celestialBody.checkIntegrity(this));
    }

    public String getName() {
        return systemName;
    }

    public CelestialBody getCelestialBodyForName(String id) {
        return celestialBodies.get(id);
    }

    private void instanciateObject(Properties body, String id) {
        String type = body.getProperty("type");
        LOG.info(type);
        CelestialBody cBody = new CelestialBody(id, body);
        celestialBodies.put(id, cBody);
    }

    public Collection<CelestialBody> getAllBodies() {
        return Collections.unmodifiableCollection(celestialBodies.values());
    }
}
