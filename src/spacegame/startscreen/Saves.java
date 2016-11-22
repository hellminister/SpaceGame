/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.startscreen;

import com.sun.javafx.collections.ObservableListWrapper;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
        currentFilePath = PATH_URL + saveFolder + "/";
        reloadSaveList();
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
    
    private void reloadSaveList(){
        saveList = new ObservableListWrapper<>(new LinkedList<>());
        
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
    

    ObservableList<String> getSavesList() {
        return saveList;
    }

    GameState loadSavedGame(String fileName) {
        String filePath = currentFilePath + fileName;
        try (
                InputStream file = new FileInputStream(filePath);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream (buffer);) {
            GameState gs = (GameState) input.readObject();
            return gs;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // this should crash the game...
    }

    private static final Logger LOG = Logger.getLogger(Saves.class.getName());

    GameState load() {
        return loadSavedGame(RECENT_SAVE);
    }

    void save(String saveName, GameState playerInfo) {
        String fileName = currentFilePath + saveName;
        try (
                OutputStream file = new FileOutputStream(fileName);
                OutputStream buffer = new BufferedOutputStream(file);
                ObjectOutput output = new ObjectOutputStream(buffer);) {
            output.writeObject(playerInfo);
            reloadSaveList();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void save(GameState playerInfo) {
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
