package edu.wpi.cs3733d18.teamF.gfx.impl.node;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d18.teamF.Main;
import edu.wpi.cs3733d18.teamF.graph.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class StartNodeDrawer extends NodeDrawable {
    private boolean isSelected = false;

    public StartNodeDrawer(Node node) {
        super(node);
    }

    @Override
    public void selectNode() {
        isSelected = true;
    }

    @Override
    public void unselectNode() {
        isSelected = false;
    }

    @Override
    public void draw(Pane pane) {
        boolean is2D = MapSingleton.getInstance().getMap().is2D();
        double imageWidth = 5000;
        double imageHeight = is2D ? 3400 : 2772;
        double posX = is2D ? node.getPosition().getX() : node.getWireframePosition().getX();
        double posY = is2D ? node.getPosition().getY() : node.getWireframePosition().getY();
        if(!node.getFloor().equals(MapSingleton.getInstance().getMap().getFloor()) && is2D){
            return;
        }

        ImageView start  = new ImageView(Main.class.getResource("start-icon.png").toExternalForm());
        start.setTranslateX((posX * pane.getMaxWidth() / imageWidth) - 85);
        start.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 131);

        start.setScaleX(0.03);
        start.setScaleY(0.03);
        start.setVisible(true);
        pane.getChildren().add(start);

        if(!node.getNodeType().equals("HALL")) {
            JFXTextField startText = new JFXTextField();

            startText.setPrefWidth(-1);
            startText.setPrefHeight(-1);

            startText.setTranslateX((posX * pane.getMaxWidth() / imageWidth));
            startText.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 10);

            startText.setText(node.getShortName());
            startText.setPrefColumnCount(node.getShortName().length());
            startText.setAlignment(Pos.CENTER);
            startText.setStyle("-fx-background-color: green; " +
                    "-fx-border-color: black; " +
                    "-fx-border-width: .2; " +
                    "-fx-border-radius: 2; " +
                    "-fx-background-radius: 2;" +
                    "-fx-font: 2 Ariel;" +
                    "-fx-text-fill: white;");
            pane.getChildren().add(startText);
            startText.setVisible(true);
        }
    }
}
