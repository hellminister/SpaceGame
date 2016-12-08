/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class contains the world unmodified by the player This is the initial
 * state of the world for a new game The modified states will be in GameState
 * and will combine with this object to make the current world state
 *
 * @author user
 */
public class ReadOnlyGameData {

    private static final String STAR_SYSTEMS_FOLDER = "src/resources/data/systems/";
    private static final Logger LOG = Logger.getLogger(ReadOnlyGameData.class.getName());

    private final Map<String, Properties> systems;

    /**
     * Loads all the initial game states of the game
     */
    public ReadOnlyGameData() {
        systems = Collections.unmodifiableMap(loadSystems());
    }
    
    public Map<String, Properties> getSystemsInitialStates(){
        return systems;
    }

    private static Map<String, Properties> loadSystems() {
        final Map<String, Properties> objects = new HashMap<>();
        try {
            List<Path> files = Files.list(Paths.get(STAR_SYSTEMS_FOLDER)).filter(t -> {
                return t.toString().endsWith(".txt");
            }).collect(Collectors.toList());

            for (Path path : files) {
                String systemName = path.getFileName().toString();
                systemName = systemName.substring(0, systemName.lastIndexOf(".txt"));
                LOG.log(Level.INFO, "System name {0}", systemName);

                loadSystem(objects, systemName, path);
            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return objects;
    }

    private static void loadSystem(final Map<String, Properties> objects, String systemName, Path path) throws IOException {
        Properties prop = objects.get(systemName);
        if (prop == null) {
            prop = new Properties();
            prop.setProperty("class", "System");
            objects.put(systemName, prop);
            Files.lines(path).filter(t -> {
                LOG.log(Level.INFO, "{0} {1}", new Object[]{t, t.isEmpty()});
                return !t.isEmpty();
            }).forEach(t -> {
                    String[] parts = t.split(":");
                    String id = systemName + (parts[0].isEmpty() ? "" : "." + parts[0]);
                    Properties prop2 = objects.get(id);
                    if (prop2 == null) {
                        prop2 = new Properties();
                        objects.put(id, prop2);
                    }
                    prop2.setProperty(parts[1], parts[2]);
                    LOG.info(t);
                    LOG.log(Level.INFO, "id1 {0} id {1} key {2} value {3}", new Object[]{parts[0], id, parts[1], parts[2]});
            });
        } else {
            LOG.log(Level.SEVERE, "{0}already exists.", systemName);
        }
    }

}
