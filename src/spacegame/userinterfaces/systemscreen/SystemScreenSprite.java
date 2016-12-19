/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;


import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;

import java.util.Set;

/**
 *
 * @author user
 */
public interface SystemScreenSprite {
    
    ReadOnlyDoubleProperty posXProperty();
    ReadOnlyDoubleProperty posYProperty();

    boolean needsRealTimeUpdate();

    Set<UIAction> getAcceptableActions();

    Node getNode();
}
