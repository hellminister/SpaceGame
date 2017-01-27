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
 * This class contains the world unmodified by the player
 * This is the initial state of the world for a new game
 * The modified states will be in GameState
 * and will combine with this object to make the current world state
 *
 * @author user
 */
class ReadOnlyGameData {

    private static final String STAR_SYSTEMS_FOLDER = "src/resources/data/systems/";
    private static final Logger LOG = Logger.getLogger(ReadOnlyGameData.class.getName());
    private static final int ID_POSITION = 0;
    private static final int KEY_POSITION = 1;
    private static final int VALUE_POSITION = 2;


    private final Map<String, Properties> systems;

    /**
     * Loads all the initial game states of the game
     */
    ReadOnlyGameData() {
        systems = Collections.unmodifiableMap(loadSystems());
    }
    
    public Map<String, Properties> getSystemsInitialStates(){
        return systems;
    }

    private static Map<String, Properties> loadSystems() {
        final Map<String, Properties> objects = new HashMap<>();
        try {
            List<Path> files = Files.list(Paths.get(STAR_SYSTEMS_FOLDER))
                    .filter(t -> t.toString().endsWith(".txt"))
                    .collect(Collectors.toList());

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
            Files.lines(path).filter(t ->  !t.isEmpty())
                    .forEach(t -> {
                        String[] parts = t.split(":");
                        String id = systemName + (parts[ID_POSITION].isEmpty() ? "" : "." + parts[ID_POSITION]);
                        Properties prop2 = objects.computeIfAbsent(id, k -> new Properties());
                        prop2.setProperty(parts[KEY_POSITION], parts[VALUE_POSITION]);
                        LOG.info(t);
                        LOG.log(Level.INFO, "id1 {0} id {1} key {2} value {3}", new Object[]{parts[ID_POSITION], id, parts[KEY_POSITION], parts[VALUE_POSITION]});
                    });
        } else {
            LOG.log(Level.SEVERE, "{0} already exists.", systemName);
        }
    }

}
