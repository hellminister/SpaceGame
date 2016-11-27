/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.startscreen;

import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import spacegame.world.GameState;
import spacegame.world.player.Gender;
import spacegame.world.player.PlayerInfo;
import spacegame.world.player.Species;

/**
 *
 * @author user
 */
public class MiddlePane {

    private VBox middlePane;

    private ListView<String> infoBox2;
    private Button newPlayer;
    private Button saveButton;

    private Button acLoadGame;
    private Button selectPlayer;

    private TextInputDialog userAnswer;
    private PlayerList playerList;
    private final StartScreen startScreen;

    public MiddlePane(StartScreen sS) {
        startScreen = sS;

        userAnswer = new TextInputDialog();
        userAnswer.setHeaderText(null);
        userAnswer.initStyle(StageStyle.UNDECORATED);
        
                playerList = new PlayerList();
                
                createMiddlePane();

    }

    private void createMiddlePane() {
        middlePane = new VBox(12);
        middlePane.setAlignment(Pos.CENTER);

        infoBox2 = new ListView<>();
        infoBox2.setEditable(false);
        infoBox2.setPrefSize(200, 288);
        infoBox2.setMaxSize(200, 288);
        infoBox2.setMinSize(200, 288);
        infoBox2.setStyle("-fx-background-color: grey");
        infoBox2.setEditable(false);

        middlePane.getChildren().add(infoBox2);

        HBox middlePaneButtons = new HBox(10);
        middlePaneButtons.setAlignment(Pos.CENTER);

        StackPane selectButtons = new StackPane();

        selectPlayer = new Button();
        selectPlayer.setText("Select");
        setMiddleButtonSizings(selectPlayer);
        selectPlayer.setOnAction(event -> {
            String selected = infoBox2.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                playerList.selectPlayer(selected);
                startScreen.setPlayerInfo(playerList.load());
                
            }
        });

        acLoadGame = new Button();
        acLoadGame.setText("Load");
        setMiddleButtonSizings(acLoadGame);
        acLoadGame.setOnAction(event -> {
            String selected = infoBox2.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                startScreen.setPlayerInfo(playerList.loadSavedGame(selected));
            }
        });

        selectButtons.getChildren().addAll(selectPlayer, acLoadGame);

        Button close = new Button();
        close.setText("Close");
        setMiddleButtonSizings(close);
        close.setOnAction(event -> {
            middlePane.setVisible(false);
        });

        middlePaneButtons.getChildren().addAll(selectButtons, close);
        middlePane.getChildren().add(middlePaneButtons);

        StackPane switchButton = new StackPane();

        newPlayer = new Button();
        newPlayer.setText("New Player");
        newPlayer.setOnAction(event -> {
            createPlayer();
            startScreen.setPlayerInfo(playerList.load());
        });

        setNewPlayerButtonSizings(newPlayer);

        switchButton.getChildren().add(newPlayer);

        saveButton = new Button();
        saveButton.setText("Save Game");
        saveButton.setOnAction(event -> {
            String saveName = getnewSaveGameName();
            playerList.saveGame(saveName, startScreen.getPlayerInfo());
        });

        setNewPlayerButtonSizings(saveButton);

        switchButton.getChildren().add(saveButton);

        middlePane.getChildren().add(switchButton);

        middlePane.setVisible(false);
    }

    private void createPlayer() {
        String first_name = getAnswer("What is your first name?");
        String name = getAnswer("What is you last name");
        Gender gender = Gender.valueOf(getAnswer("What is your gender? (Male Female Neuter)"));
        Species species = Species.valueOf(getAnswer("What is your species? (Human)"));
        
        PlayerInfo newPlayer = new PlayerInfo(name, first_name, species, gender);
        GameState newGame = new GameState(newPlayer);
        
        playerList.createPlayer(newGame);
    }
    
    /**
     * will need to refactor this for integrity checks.....
     * @param question
     * @return 
     */
    private String getAnswer(String question){
        userAnswer.setContentText(question);
        userAnswer.getEditor().clear();
        Optional<String> result = userAnswer.showAndWait();
        return result.get();
    }

    private void setMiddleButtonSizings(Button btn) {
        btn.setMaxSize(95, 30);
        btn.setMinSize(50, 15);
        btn.setPrefSize(95, 30);

    }

    private void setNewPlayerButtonSizings(Button btn) {
        btn.setMaxSize(200, 30);
        btn.setMinSize(50, 15);
        btn.setPrefSize(200, 30);
    }

    private String getnewSaveGameName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Node getRootPane() {
        return middlePane;
    }

    public GameState load() {
        return playerList.load();
    }

    public void hide() {
        middlePane.setVisible(false);
    }

    void showForChoosePlayer() {
        middlePane.setVisible(true);
            saveButton.setVisible(false);
            newPlayer.setVisible(true);
            acLoadGame.setVisible(false);
            selectPlayer.setVisible(true);
            infoBox2.setItems(playerList.getPlayerList());
    }

    void showForLoadGame(boolean gameStarted) {
            middlePane.setVisible(true);
            newPlayer.setVisible(false);
            if (gameStarted) {
                saveButton.setVisible(true);
            } else {
                saveButton.setVisible(false);
            }
            selectPlayer.setVisible(false);
            acLoadGame.setVisible(true);
            infoBox2.setItems(playerList.getSaveList());
    }
}
