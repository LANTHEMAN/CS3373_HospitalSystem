package edu.wpi.cs3733d18.teamF.controller.page;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public abstract class PageElement {
    private Pane root;

    protected void initElement(AnchorPane host, Pane root) {
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        host.getChildren().setAll(root);
        this.root = host;
    }

    public void hideElement() {
        root.setMouseTransparent(true);
        root.setPickOnBounds(false);
        root.setVisible(false);
        hideElement(root);
    }

    public void showElement() {
        root.setMouseTransparent(false);
        root.setPickOnBounds(true);
        root.setVisible(true);
        showElement(root);
    }

    private void hideElement(Pane pane) {
        for (Node component : pane.getChildren()) {
            if (component instanceof Pane) {
                //if the component is a container, scan its children
                hideElement((Pane) component);
                component.setMouseTransparent(true);
                component.setPickOnBounds(false);
                component.setVisible(false);

            } else {
                component.setVisible(false);
                component.setMouseTransparent(true);
                component.setPickOnBounds(false);
            }

        }
    }
    private void showElement(Pane pane) {
        for (Node component : pane.getChildren()) {
            if (component instanceof Pane) {
                //if the component is a container, scan its children
                showElement((Pane) component);
                component.setMouseTransparent(false);
                component.setPickOnBounds(true);
                component.setVisible(true);
            } else {
                component.setVisible(true);
                component.setMouseTransparent(false);
                component.setPickOnBounds(true);
            }

        }
    }
}
