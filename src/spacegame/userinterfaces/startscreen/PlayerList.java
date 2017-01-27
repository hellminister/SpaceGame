/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.startscreen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import spacegame.world.GameState;

/**
 *
 * @author user
 */
class PlayerList {

    private static final Logger LOG = Logger.getLogger(PlayerList.class.getName());
    private static final String PATH_URL = "src/resources/saves/";
    private static final String FILE_NAME = "playerList.txt";
    
    private static final int SAVE_FOLDER_NAME_POSITION = 0;
    private static final int PLAYER_NAME_POSITION = 1;
    private static final int CURRENT_PLAYER_MARKER_POSITION = 2;

    private TreeMap<String, String> playerListMap;

    private String currentPlayer;

    private Saves currentPlayerSaves;

    private int nextNewPlayerNumber;

    private final ObservableList<String> listOfPlayers;

    /**
     * Loads the list of players for the game
     */
    PlayerList() {

        // Using a tree map to order the player's name alphabetically
        playerListMap = new TreeMap<>();
        nextNewPlayerNumber = 1;

        String fileUrl = PATH_URL + FILE_NAME;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileUrl)))) {
            String line = reader.readLine();
            nextNewPlayerNumber = Integer.valueOf(line);
            line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(" \\\"|\\\" ?");

                playerListMap.put(parts[PLAYER_NAME_POSITION], parts[SAVE_FOLDER_NAME_POSITION]);
                if (parts.length > CURRENT_PLAYER_MARKER_POSITION){
                    currentPlayer = parts[PLAYER_NAME_POSITION];
                    currentPlayerSaves = new Saves(parts[SAVE_FOLDER_NAME_POSITION]);
                }

                line = reader.readLine();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        listOfPlayers = FXCollections.observableArrayList(playerListMap.navigableKeySet());
    }

    /**
     * Load the selected player
     * @param playerName 
     */
    public void selectPlayer(String playerName) {
        String saveFolder = playerListMap.get(playerName);
        currentPlayerSaves = new Saves(saveFolder);
        currentPlayer = playerName;
        saveData();
    }

    /**
     * Creates a new player with the given data
     * @param newPlayer 
     */
    public void createPlayer(GameState newPlayer) {
        String saveFolder = String.format("%03d", nextNewPlayerNumber); 
        
        nextNewPlayerNumber++;

        currentPlayer = newPlayer.getFullName();
        currentPlayerSaves = Saves.createSaveFolder(saveFolder);
        playerListMap.put(currentPlayer, saveFolder);

        saveData();
        currentPlayerSaves.save(newPlayer);

        listOfPlayers.setAll(playerListMap.navigableKeySet());
    }

    /**
     * return wether the given name already exists
     * @param name
     * @return 
     */
    public boolean nameExists(String name) {
        return playerListMap.containsKey(name);
    }

    private void saveData() {
        String fileUrl = PATH_URL + FILE_NAME;
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileUrl)))) {
            String treating;
            bw.write(Integer.toString(nextNewPlayerNumber) + "\n");
            for (Entry<String, String> me : playerListMap.entrySet()) {
                treating = me.getKey();
                String toSave = me.getValue() + " \"" + treating + "\"" + (treating.equals(currentPlayer) ? " r" : "") + "\n";
                bw.append(toSave);
            }
            bw.flush();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public ObservableList<String> getPlayerList() {
        return listOfPlayers;
    }

    public ObservableList<String> getSaveList() {
        return currentPlayerSaves.getSavesList();
    }

    /**
     * Loads the specified saved game
     * @param fileName
     * @return 
     */
    public GameState loadSavedGame(String fileName) {
        return currentPlayerSaves.loadSavedGame(fileName);
    }

    /**
     * saves the game to the specified file name
     * @param saveName
     * @param playerInfo 
     */
    public void saveGame(String saveName, GameState playerInfo) {
        currentPlayerSaves.save(saveName, playerInfo);
    }

    /**
     * loads the most recent game of the player
     * @return 
     */
    public GameState load() {
        if (currentPlayerSaves != null) {
            return currentPlayerSaves.load();
        } else {
            return null;
        }
    }
}
