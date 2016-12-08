/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world;

import spacegame.world.player.PlayerInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class GameState implements Serializable {

    private static final Logger LOG = Logger.getLogger(GameState.class.getName());
    private static final long serialVersionUID = 1L;

    /**
     * Contains the players info
     *
     * @serial
     */
    private PlayerInfo playerInfo;

    private Map<String, Properties> systemsModifications;

    public GameState(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        systemsModifications = new HashMap<>();
    }

    public String getInfo() {
        return playerInfo.getInfo();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        s.writeObject(playerInfo);
        s.writeInt(systemsModifications.size());
        for (Entry<String, Properties> e : systemsModifications.entrySet()) {
            s.writeUTF(e.getKey());
            s.writeObject(e.getValue());
        }

    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        playerInfo = (spacegame.world.player.PlayerInfo) s.readObject();
        int nb = s.readInt();
        systemsModifications = new HashMap<>();
        String key;
        Properties value;
        for (int i = 0; i < nb; i++) {
            key = s.readUTF();
            value = (Properties) s.readObject();
            systemsModifications.put(key, value);
        }
    }

    public String getFullName() {
        return playerInfo.getFullName();
    }

    public PlayerInfo getPlayerState() {
        return playerInfo;
    }

    public Map<String, Properties> getSystemsModifications() {
        return systemsModifications;
    }

}
