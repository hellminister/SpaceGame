/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.startscreen;

import com.sun.javafx.collections.ObservableListWrapper;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import spacegame.world.GameState;

/**
 *
 * @author user
 */
class Saves {

    private static final String PATH_URL = "src/resources/saves/";
    private static final String RECENT_SAVE = "recent.sav";
    private static final String PREVIOUS_SAVE_1 = "previous1.sav";
    private static final String PREVIOUS_SAVE_2 = "previous2.sav";
    private static final String PREVIOUS_SAVE_3 = "previous3.sav";

    private final String currentFilePath;

    private ObservableList<String> saveList;

    Saves(String saveFolder) {
        saveList = new ObservableListWrapper<>(new LinkedList<>());
currentFilePath = PATH_URL + saveFolder + "/";
            try {
                List<String> fileList = Files.list(Paths.get(currentFilePath)).map((t) -> {
                    return t.toFile().getName();
                }).filter((t) -> {
                    return t.endsWith(".sav"); //To change body of generated lambdas, choose Tools | Templates.
                }).collect(Collectors.toList());
                LOG.finest(fileList::toString);
                saveList = new ObservableListWrapper<>(fileList);
            } catch (IOException ex) {
                Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    Saves(String saveFolder, boolean b) {
        currentFilePath = PATH_URL + saveFolder + "/";
        if (b) {
            try {
                Files.createDirectory(Paths.get(currentFilePath));
            } catch (IOException ex) {
                Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            
        }
    }


    ObservableList<String> getSavesList() {
        return saveList;
    }

    // have this return a GameState
    GameState loadSavedGame(String fileName) {
        return new GameState();
    }

    private static final Logger LOG = Logger.getLogger(Saves.class.getName());

    GameState load() {
        return loadSavedGame(currentFilePath + RECENT_SAVE);
    }

    void save(String saveName, GameState playerInfo) {
        
    }
    
    void save(GameState playerInfo){
        doBackUps(PREVIOUS_SAVE_2, PREVIOUS_SAVE_3);
        doBackUps(PREVIOUS_SAVE_1, PREVIOUS_SAVE_2);
        doBackUps(RECENT_SAVE, PREVIOUS_SAVE_1);
        save(RECENT_SAVE, playerInfo);
    }

    private void doBackUps(String from, String to) {
        try {
            Path old = Paths.get(currentFilePath + from);
            Files.move(old, old.resolveSibling(to), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidPathException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.WARNING, null, ex);
        }
    }

}
