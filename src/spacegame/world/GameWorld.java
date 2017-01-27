/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import spacegame.world.player.StarDate;
import spacegame.world.systems.BubbleSystem;

/**
 * Made this a singleton to reduce coupling of classes
 * And anyway this was going to be a final field in the main game class
 * as there is supposed to be only 1 such instance
 * @author user
 */
public final class GameWorld {

    private static final Logger LOG = Logger.getLogger(GameWorld.class.getName());
    private static final GameWorld theGameWorld;
    static {
        theGameWorld = new GameWorld();
    }

    private GameState currentState;

    private Map<String, BubbleSystem> systemList;

    private final ReadOnlyGameData initialGameState;

    /**
     * Initialize the gameWorld
     */
    private GameWorld() {
        initialGameState = new ReadOnlyGameData();

        currentState = null;
    }

    public static GameWorld accessGameWorld(){
        return theGameWorld;
    }

    public void setPlayerState(GameState gameData) {
        currentState = gameData;
        populateSystemList();
    }

    public GameState getPlayerState() {
        return currentState;
    }

    public BubbleSystem getSystem() {
        return systemList.get(currentState.getPlayerState().getCurrentSystem());
    }

    private void populateSystemList() {
        LOG.info("populating Systems");
        Map<String, Properties> currentSystemState = combine(initialGameState.getSystemsInitialStates(), currentState.getSystemsModifications());
        systemList = currentSystemState.entrySet().stream()
                .filter(t -> "System".equals(t.getValue().getProperty("class")))
                .map(t -> new BubbleSystem(t, currentSystemState))
                .collect(Collectors.toConcurrentMap(BubbleSystem::getName,
                                                    t -> t));
    }

    private static Map<String, Properties> combine(final Map<String, Properties> systemsInitialStates, final Map<String, Properties> systemsModifications) {
        Map<String, Properties> combined = new HashMap<>();
        systemsInitialStates.forEach((t, u) -> {
            Properties p = new Properties();
            u.forEach((t2, u2) -> p.setProperty((String) t2, (String) u2));
            combined.put(t, p);
        });

        systemsModifications.forEach((componentId, componentModifications) -> {
            combined.merge(componentId, componentModifications, GameWorld::remappingFunction);
        });

        return combined;
    }

    private static Properties remappingFunction(Properties componentOldProperties, Properties componentToMerge) {
        Properties props;
        if (componentOldProperties == null) {
            props = new Properties(componentToMerge);
        } else if (componentToMerge.containsKey("deleted")) {
            props = null;
        } else {
            props = new Properties(componentOldProperties);
            componentToMerge.forEach((propertyKey, propertyValue) -> {
                if ("".equals(propertyValue)) {
                    props.remove(propertyKey);
                } else {
                    props.setProperty((String) propertyKey, (String) propertyValue);
                }
            });
        }
        return props;
    }

    public StarDate getCurrentStarDate(){
        return getPlayerState().getPlayerState().getStarDate();
    }

}
