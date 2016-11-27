/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * this is probably abstract
 * @author user
 */
public class Star {

    void draw(StackPane systemPane) {
        Circle sun = new Circle(50);
        sun.setFill(Color.YELLOW);
        systemPane.getChildren().add(sun);
        
        StackPane.setAlignment(sun, Pos.CENTER);
        
        sun.setOnMouseClicked(e -> {
            System.out.println("spacegame.world.systems.Star.draw()");
        });
        
    }
    
}
