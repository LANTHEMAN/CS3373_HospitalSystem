package edu.wpi.cs3733d18.teamF.gfx.impl;

import com.sun.javafx.font.Glyph;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.scene.layout.Pane;

public class StartNodeDrawer extends NodeDrawable {
    public StartNodeDrawer(Node node) {
        super(node);
    }

    public StartNodeDrawer() {
        super();
    }

    @Override
    public void selectNode() {
        //not used
    }

    @Override
    public void unselectNode() {
        //not used
    }

    @Override
    public void draw(Pane pane) {
        FontAwesomeIconView startIcon = new FontAwesomeIconView(FontAwesomeIcon.HOME);
        startIcon.setScaleX(1);
        startIcon.setScaleY(1);
        startIcon.setTranslateX(node.getPosition().getX());
        startIcon.setTranslateY(node.getPosition().getY());
        startIcon.setVisible(true);
        pane.getChildren().add(startIcon);
    }
}
