/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

/**
 *
 * @author user
 */
public abstract class ReachStartScreen extends Scene {

    /**
     * {@link javafx.scene.Scene#Scene(javafx.scene.Parent) contructor}     * @param root 
     * @param root
     */
    public ReachStartScreen(Parent root) {
        super(root);
    }

    /**
     * {@link javafx.scene.Scene#Scene(javafx.scene.Parent, double, double) contructor}
     * @param root
     * @param width
     * @param height 
     */
    public ReachStartScreen(Parent root, double width, double height) {
        super(root, width, height);
    }

    /**
     * {@link javafx.scene.Scene#Scene(javafx.scene.Parent, javafx.scene.paint.Paint) constructor}
     * @param root
     * @param fill 
     */
    public ReachStartScreen(Parent root, Paint fill) {
        super(root, fill);
    }

    /**
     * {@link javafx.scene.Scene#Scene(javafx.scene.Parent, double, double, javafx.scene.paint.Paint) contructor}
     * @param root
     * @param width
     * @param height
     * @param fill 
     */
    public ReachStartScreen(Parent root, double width, double height, Paint fill) {
        super(root, width, height, fill);
    }

    /**
     * {@link javafx.scene.Scene#Scene(javafx.scene.Parent, double, double, boolean) contructor}
     * @param root
     * @param width
     * @param height
     * @param depthBuffer 
     */
    public ReachStartScreen(Parent root, double width, double height, boolean depthBuffer) {
        super(root, width, height, depthBuffer);
    }

    /**
     * {@link javafx.scene.Scene#Scene(javafx.scene.Parent, double, double, boolean, javafx.scene.SceneAntialiasing) contructor}
     * @param root
     * @param width
     * @param height
     * @param depthBuffer
     * @param antiAliasing 
     */
    public ReachStartScreen(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);
    }
    
    /**
     * Contains the behavior needed when the scene gets its focus back
     */
    public abstract void giveFocusBack();
}
