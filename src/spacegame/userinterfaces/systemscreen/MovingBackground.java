/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author user
 */
public class MovingBackground extends Group {
    
    
    private static final Logger LOG = Logger.getLogger(MovingBackground.class.getName());

    private static final String BACK_IMAGE_TILE_FILE_PATH = "/resources/images/tiles/1.jpg";
    private static final int GRIDSIZE = 5;
    private static final int CENTER_SQUARE = (GRIDSIZE - 1) / 2;

    private final Image backgroundTile;
    private final ImageView[][] aSquare;

    private int leftMostIndex;
    private int rightMostIndex;
    private int bottomMostIndex;
    private int topMostIndex;
    
    private int horizontalCenter;
    private int verticalCenter;

    /**
     * Initialize a moving background with the default values
     */
    public MovingBackground() {
        leftMostIndex = 0;
        rightMostIndex = GRIDSIZE - 1;
        bottomMostIndex = GRIDSIZE - 1;
        topMostIndex = 0;
        horizontalCenter = CENTER_SQUARE;
        verticalCenter = CENTER_SQUARE;
        
        backgroundTile = new Image(BACK_IMAGE_TILE_FILE_PATH);
        aSquare = new ImageView[GRIDSIZE][GRIDSIZE];
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                aSquare[i][j] = new ImageView(backgroundTile);
                aSquare[i][j].setTranslateX((i - CENTER_SQUARE) * backgroundTile.getWidth());
                aSquare[i][j].setTranslateY((j - CENTER_SQUARE) * backgroundTile.getHeight());
            }
        }
    }

    /**
     * Add the created moving background to the given Pane
     *
     * @param fullSystemArea
     */
    public void addToPane(Pane fullSystemArea) {
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                fullSystemArea.getChildren().add(aSquare[i][j]);
            }
        }
    }

    /**
     * Moves the background squares so that the ship is mostly always centered in the squares
     * @param posXProperty
     * @param posYProperty 
     */
    public void moveBackgroundIfNecessary(ReadOnlyDoubleProperty posXProperty, ReadOnlyDoubleProperty posYProperty) {
        double posX = posXProperty.get();
        double posY = posYProperty.get();
        double centerX = aSquare[horizontalCenter][verticalCenter].getTranslateX();
        double centerY = aSquare[horizontalCenter][verticalCenter].getTranslateY();
        
        if (posX < centerX){
            moveLeft();
        } else if (posX > (centerX + backgroundTile.getWidth())){
            moveRight();
        }
        
        if (posY < centerY){
            moveUp();
        } else if (posY > (centerY + backgroundTile.getHeight())){
            moveDown();
        }

    }

    private void moveRight() {
        for (int j = 0; j < GRIDSIZE; j++) {
            aSquare[leftMostIndex][j].setTranslateX((aSquare[leftMostIndex][j].getTranslateX()) + (backgroundTile.getWidth() * GRIDSIZE));
        }
        rightMostIndex = leftMostIndex;
        leftMostIndex = (leftMostIndex + 1) % GRIDSIZE;
        horizontalCenter = (horizontalCenter + 1) % GRIDSIZE;
    }

    private void moveLeft() {
        for (int j = 0; j < GRIDSIZE; j++) {
            aSquare[rightMostIndex][j].setTranslateX((aSquare[rightMostIndex][j].getTranslateX()) - (backgroundTile.getWidth() * GRIDSIZE));
        }
        leftMostIndex = rightMostIndex;
        rightMostIndex -= 1;
        if (rightMostIndex < 0) {
            rightMostIndex = GRIDSIZE - 1;
            
        }
        horizontalCenter -= 1;
        if (horizontalCenter < 0) {
            horizontalCenter = GRIDSIZE - 1;
            
        }
    }

    private void moveUp() {
        for (int i = 0; i < GRIDSIZE; i++) {
            aSquare[i][bottomMostIndex].setTranslateY((aSquare[i][bottomMostIndex].getTranslateY()) - (backgroundTile.getHeight() * GRIDSIZE));
        }
        topMostIndex = bottomMostIndex;
        bottomMostIndex -= 1;
        if (bottomMostIndex < 0) {
            bottomMostIndex = GRIDSIZE - 1;
        }
        
        verticalCenter -= 1;
        if (verticalCenter < 0) {
            verticalCenter = GRIDSIZE - 1;
        }
        
    }

    private void moveDown() {
        for (int i = 0; i < GRIDSIZE; i++) {
            aSquare[i][topMostIndex].setTranslateY((aSquare[i][topMostIndex].getTranslateY()) + (backgroundTile.getHeight() * GRIDSIZE));
        }
        bottomMostIndex = topMostIndex;
        topMostIndex = (topMostIndex + 1) % GRIDSIZE;
        verticalCenter = (verticalCenter + 1) % GRIDSIZE;
    }

}
