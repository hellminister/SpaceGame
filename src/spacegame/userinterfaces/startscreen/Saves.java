/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.startscreen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import spacegame.world.GameState;

/**
 *
 * @author user
 */
public class Saves {

    private static final Logger LOG = Logger.getLogger(Saves.class.getName());
    private static final String PATH_URL = "src/resources/saves/";
    private static final String RECENT_SAVE = "recent.sav";
    private static final String PREVIOUS_SAVE_1 = "previous1.sav";
    private static final String PREVIOUS_SAVE_2 = "previous2.sav";
    private static final String PREVIOUS_SAVE_3 = "previous3.sav";

    private final String currentFilePath;

    private ObservableList<String> saveList;

    /**
     * Opens the requested saves folder
     * And extracts the save files list
     * @param saveFolder Folders name
     */
    public Saves(String saveFolder) {
        currentFilePath = PATH_URL + saveFolder + "/";
        reloadSaveList();
    }

    /**
     * Creates a new save folder for a new player And returns the associated Saves object
     * @param saveFolder new Folder's name
     * @return The associated Saves object
     */
    public static Saves createSaveFolder(String saveFolder) {
        String currentFilePath = PATH_URL + saveFolder + "/";
        try {
            Files.createDirectory(Paths.get(currentFilePath));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return new Saves(saveFolder);

    }

    private void reloadSaveList() {

        try {
            List<String> fileList = Files.list(Paths.get(currentFilePath)).map(t -> {
                    return t.toFile().getName();
            }).filter(t -> {
                    return t.endsWith(".sav");
            }).collect(Collectors.toList());
            LOG.finest(fileList::toString);
            saveList = FXCollections.observableList(fileList);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public ObservableList<String> getSavesList() {
        return saveList;
    }

    /**
     * Loads the requested file
     * @param fileName the file to load
     * @return the loaded GameState
     */
    public GameState loadSavedGame(String fileName) {
        String filePath = currentFilePath + fileName;
        try (
                InputStream file = new FileInputStream(filePath);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput input = new ObjectInputStream(buffer);) {
            return (spacegame.world.GameState) input.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // this should crash the game...
    }

    /**
     * Loads the recent save file
     * @return the associated GameState
     */
    public GameState load() {
        return loadSavedGame(RECENT_SAVE);
    }

    /**
     * Saves the GameState to the specified file
     * @param saveName The name of the save file
     * @param playerInfo The state of the game
     */
    public void save(String saveName, GameState playerInfo) {
        String fileName = currentFilePath + saveName;
        try (
                OutputStream file = new FileOutputStream(fileName);
                OutputStream buffer = new BufferedOutputStream(file);
                ObjectOutput output = new ObjectOutputStream(buffer);) {
            output.writeObject(playerInfo);
            reloadSaveList();

        } catch (IOException ex) {
            Logger.getLogger(Saves.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * saves the GameState to the recent file and keeps up to 3 backups in time
     * @param playerInfo the GameState to save
     */
    public void save(GameState playerInfo) {
        doBackUps(PREVIOUS_SAVE_2, PREVIOUS_SAVE_3);
        doBackUps(PREVIOUS_SAVE_1, PREVIOUS_SAVE_2);
        doBackUps(RECENT_SAVE, PREVIOUS_SAVE_1);
        save(RECENT_SAVE, playerInfo);
    }

    private void doBackUps(String from, String to) {
        try {
            Path old = Paths.get(currentFilePath + from);
            Files.move(old, old.resolveSibling(to), StandardCopyOption.REPLACE_EXISTING);
        } catch (NoSuchFileException ex) {
            LOG.log(Level.INFO, "No file to Backup", ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (InvalidPathException ex) {
            LOG.log(Level.WARNING, null, ex);
        }
    }

}
