package edu.wpi.cs3733d18.teamF.gfx.impl;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.Map;
import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.PathDrawable;
import edu.wpi.cs3733d18.teamF.graph.Edge;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public class DynamicPathDrawer extends PathDrawable {

    Timeline timeline = null;
    private ArrayList<Arrow> arrows = new ArrayList<>();

    public DynamicPathDrawer() {
        super();
    }

    private Pair<Pair<Node, Node>, Double> getPathPos(double progress) {
        Node src;
        Node dst = path.getNodes().get(0);
        for (Edge edge : path.getEdges()) {
            src = dst;
            dst = (edge.getNode1() == src) ? edge.getNode2() : edge.getNode1();
            if (progress < edge.getDistance()) {
                return new Pair<>(new Pair<>(src, dst), progress);
            }
            progress -= edge.getDistance();
        }
        // if drawing off of the path
        return null;
    }

    @Override
    public void draw(Pane pane) {
        arrows.clear();

        if (path.getEdges().size() <= 1) {
            return;
        }

        double len = path.getUnweightedLength();
        double divDist = 40;
        int divs = (int) ceil(len / divDist);
        divDist = len / divs;



        for (int i = 0; i < divs; i++) {
            arrows.add(new Arrow(i * divDist));
        }

        int timestep = 300;
        for (Arrow arrow : arrows) {
            arrow.view.setVisible(false);
            pane.getChildren().add(arrow.view);
            arrow.prevX = null; //pane.getMaxWidth() / 2;
            arrow.prevY = null; //pane.getMaxHeight() / 2;
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(timestep), event -> {
            for (Arrow arrow : arrows) {
                //arrow.progress += .8;
                arrow.progress += timestep / 10;
                Pair<Pair<Node, Node>, Double> pathPos = getPathPos(arrow.progress);
                if (pathPos == null) {
                    arrow.progress -= path.getUnweightedLength();
                    arrow.view.setVisible(false);
                    arrow.prevX = null;
                    arrow.prevY = null;
                    continue;
                }
                if (!MapSingleton.getInstance().getMap().getFloor().equals(pathPos.getKey().getValue().getFloor())
                        || !MapSingleton.getInstance().getMap().getFloor().equals(pathPos.getKey().getKey().getFloor())) {
                    arrow.view.setVisible(false);
                    arrow.prevX = null;
                    arrow.prevY = null;
                    continue;
                }

                arrow.view.setFill(Color.color(Math.random(), Math.random(), Math.random()));

                arrow.view.setVisible(true);

                Node src = pathPos.getKey().getKey();
                Node dst = pathPos.getKey().getValue();
                double distAlongPath = pathPos.getValue();

                boolean is2D = MapSingleton.getInstance().getMap().is2D();
                double imageWidth = 5000;
                double imageHeight = is2D ? 3400 : 2772;
                double posX1 = is2D ? src.getPosition().getX() : src.getWireframePosition().getX();
                double posY1 = is2D ? src.getPosition().getY() : src.getWireframePosition().getY();
                double posX2 = is2D ? dst.getPosition().getX() : dst.getWireframePosition().getX();
                double posY2 = is2D ? dst.getPosition().getY() : dst.getWireframePosition().getY();

                double angle = Math.toDegrees(Math.atan2(posY2 - posY1, posX2 - posX1)) + 90;

                if (arrow.prevX == null && arrow.prevY == null) {
                    arrow.prevX = (posX1 + ((posX2 - posX1) * (distAlongPath / src.displacementTo(dst))) - 35) * pane.getMaxWidth() / imageWidth;
                    arrow.prevY = (posY1 + ((posY2 - posY1) * (distAlongPath / src.displacementTo(dst))) + 28) * pane.getMaxHeight() / imageHeight;
                    arrow.view.setTranslateX(arrow.prevX);
                    arrow.view.setTranslateY(arrow.prevY);
                    arrow.view.setRotate(angle);
                } else {
                    arrow.prevX = arrow.view.getTranslateX();
                    arrow.prevY = arrow.view.getTranslateY();
                }

                arrow.prevAngle = arrow.view.getRotate();

                TranslateTransition translateTransition =
                        new TranslateTransition(Duration.millis(timestep + 10), arrow.view);
                translateTransition.setFromX(arrow.prevX);
                translateTransition.setToX((posX1 + ((posX2 - posX1) * (distAlongPath / src.displacementTo(dst))) - 35) * pane.getMaxWidth() / imageWidth);
                translateTransition.setFromY(arrow.prevY);
                translateTransition.setToY((posY1 + ((posY2 - posY1) * (distAlongPath / src.displacementTo(dst))) + 28) * pane.getMaxHeight() / imageHeight);
                translateTransition.setCycleCount(1);
                translateTransition.play();

                if (arrow.prevAngle - angle > 180) {
                    angle += 360;
                } else if(arrow.prevAngle - angle < -180){
                    angle -= 360;
                }

                RotateTransition rotateTransition =
                        new RotateTransition(Duration.millis(timestep), arrow.view);
                rotateTransition.setFromAngle(arrow.prevAngle);
                rotateTransition.setToAngle(angle);
                rotateTransition.play();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    @Override
    public void update(Path path) {
        this.path = path;
        if (timeline != null) {
            timeline.stop();
        }
    }

    private static class Arrow {
        public FontAwesomeIconView view = new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_UP);
        public double progress;
        Double prevX = null;
        Double prevY = null;
        double prevAngle = 0;

        public Arrow(double progress) {
            this.progress = progress;
            view.setFill(Color.BLACK);
            view.setScaleX(0.3);
            view.setScaleY(0.3);
        }
    }
}
