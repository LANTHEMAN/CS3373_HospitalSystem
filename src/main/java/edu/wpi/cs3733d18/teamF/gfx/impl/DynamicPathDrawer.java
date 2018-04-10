package edu.wpi.cs3733d18.teamF.gfx.impl;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.PathDrawable;
import edu.wpi.cs3733d18.teamF.graph.Edge;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.lang.Math.ceil;

public class DynamicPathDrawer extends PathDrawable {

    private ArrayList<Arrow> arrows = new ArrayList<>();
    Timeline timeline = null;

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

        double len = path.getLength();
        double divDist = 120;
        int divs = (int) ceil(len / divDist);

        for (int i = 0; i < divs; i++) {
            arrows.add(new Arrow(i * divDist));
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            for (Arrow arrow : arrows) {
                arrow.progress += .8;
                Pair<Pair<Node, Node>, Double> pathPos = getPathPos(arrow.progress);
                if (pathPos == null) {
                    arrow.progress -= path.getLength();
                    continue;
                }

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

                double angle = Math.atan2(dst.getPosition().getY() - src.getPosition().getY()
                        , dst.getPosition().getX() - src.getPosition().getX());
                arrow.view.setRotate(Math.toDegrees(angle) + 90);
                arrow.view.setX((posX1 + ((posX2 - posX1) * (distAlongPath/src.displacementTo(dst))) - 35)  * pane.getMaxWidth() / imageWidth);
                arrow.view.setY((posY1 + ((posY2 - posY1) * (distAlongPath/src.displacementTo(dst))) + 28) * pane.getMaxHeight() / imageHeight);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);

        for(Arrow arrow : arrows){
            pane.getChildren().add(arrow.view);
        }
        timeline.play();
    }

    private static class Arrow {
        public FontAwesomeIconView view = new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_UP);
        public double progress;

        public Arrow(double progress) {
            this.progress = progress;
            view.setFill(Color.BLACK);
            view.setScaleX(0.3);
            view.setScaleY(0.3);
        }
    }

    @Override
    public void update(Path path){
        this.path = path;
        if(timeline != null){
            timeline.stop();
        }
    }
}
