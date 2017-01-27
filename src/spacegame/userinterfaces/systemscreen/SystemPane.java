/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

/**
 *
 * This Pane contains the stars and celectial bodies of a bubble system
 * 
 * @author user
 */
class SystemPane extends StackPane{
    
    private EnumMap<UIAction, Set<SystemScreenSprite>> sprites;

    SystemPane(BubbleSystem system, SystemScreen attachedTo) {
        sprites = new EnumMap<>(UIAction.class);

        for (CelestialBody bodies : system.getAllBodies()){
            SystemScreenSprite sprite = bodies.getSystemScreenSprite();
            Node celBod = sprite.getNode();
            celBod.setOnMouseClicked(event -> {
                attachedTo.selectedCelestialBody(bodies);
            });
            for (UIAction act : sprite.getAcceptableActions()){
                sprites.computeIfAbsent(act, uiAction -> new HashSet<>()).add(sprite);
            }
            getChildren().add(sprite.getNode());
        }

    }

}
