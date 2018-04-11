package edu.wpi.cs3733d18.teamF.gfx.impl;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.scene.layout.Pane;

public class EndNodeDrawer extends NodeDrawable {

    public EndNodeDrawer(Node node) {
        super(node);
    }

    public EndNodeDrawer() {
        super();
    }

    @Override
    public void selectNode() {

    }

    @Override
    public void unselectNode() {

    }

    @Override
    public void draw(Pane pane) {
        FontAwesomeIconView endIcon = new FontAwesomeIconView(FontAwesomeIcon.MAP_PIN);
        endIcon.setScaleX(1);
        endIcon.setScaleY(1);
    }
}
