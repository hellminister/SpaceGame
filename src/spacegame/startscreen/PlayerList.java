/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.startscreen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
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
public class PlayerList {
    
    private static final Logger LOG = Logger.getLogger(PlayerList.class.getName());
    private static final String PATH_URL = "src/resources/saves/";
    private static final String FILE_NAME = "playerList.txt";
    
  //  private final String pathName;
    
    private TreeMap<String, String> playerListMap;
    
    private String currentPlayer;
    
    private Saves currentPlayerSaves;
    
    private int nextNewPlayerNumber;
    
    private ObservableList<String> playerList;
    
    public PlayerList(){
        
        // Using a tree map to order the player's name alphabetically
        playerListMap = new TreeMap<>();
        nextNewPlayerNumber = 1;
        
        String file_url = PATH_URL + FILE_NAME;
        
        ClassLoader classLoader = getClass().getClassLoader();
        URL path = classLoader.getResource(file_url);
//        pathName = path.getFile();
        
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file_url)))){
            String line = reader.readLine();
            while (line != null){
                String[] parts = line.split(" \\\"|\\\" ?");
                
                switch (parts.length) {
                    case 1:
                        nextNewPlayerNumber = Integer.valueOf(parts[0]);
                        break;
                    case 3:
                        currentPlayer = parts[1];
                        currentPlayerSaves = new Saves(parts[0]);
                    case 2:
                        playerListMap.put(parts[1], parts[0]);
                        break;
                    default:
                        LOG.log(Level.WARNING, "Bad line in player list file : {0}", line);
                        break;
                }
                
                line = reader.readLine();
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        playerList = FXCollections.observableArrayList(playerListMap.navigableKeySet());
    }
    
    public void selectPlayer(String playerName){
        String saveFolder = playerListMap.get(playerName);
        currentPlayerSaves = new Saves(saveFolder);
    }
    
    public void createPlayer(String name){
        String num = Integer.toString(nextNewPlayerNumber);
        nextNewPlayerNumber++;
        int nbZero = 3-num.length();
        String saveFolder = "";
        for (int i = 0; i < nbZero; i++){
            saveFolder+="0";
        }
        saveFolder+=num;
        
        currentPlayer = name;
        currentPlayerSaves = new Saves(saveFolder, true);
        playerListMap.put(name, saveFolder);
        
        saveData();
        
        playerList = FXCollections.observableArrayList(playerListMap.navigableKeySet());
     }
    
    public boolean nameExists(String name){
        return playerListMap.containsKey(name);
    }

    private void saveData() {
        String file_url = PATH_URL + FILE_NAME;
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_url)))){
            String treating;
            bw.write("");
            for (Entry<String, String> me : playerListMap.entrySet()){
                treating = me.getKey();
                String toSave = me.getValue() + " \"" + treating + "\"" + (treating.equals(currentPlayer) ? " r" : "") + "\n";
                bw.append(toSave);
            }
            bw.append(Integer.toString(nextNewPlayerNumber) + "\n");
            bw.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayerList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlayerList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<String> getPlayerList(){
        return playerList;
    }
    
    public ObservableList<String> getSaveList() {
        return currentPlayerSaves.getSavesList();
    }
    
    public GameState loadSavedGame(String fileName){
        return currentPlayerSaves.loadSavedGame(fileName);
    }
    

    public void saveGame(String saveName, GameState playerInfo) {
        currentPlayerSaves.save(saveName, playerInfo);
    }

    public GameState load() {
        return currentPlayerSaves.load();
    }
}
