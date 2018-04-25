package edu.wpi.cs3733d18.teamF.gfx.impl.radial;

import com.mrlonee.radialfx.core.RadialMenuItem;
import com.mrlonee.radialfx.core.RadialMenuItemBuilder;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class GenericRadial extends Group {

    private final Group itemsContainer = new Group();
    private final Color baseColor = Color.web("e0e0e0");
    private final Color hoverColor = Color.web("30c0ff");
    private final Color selectionColor = Color.BLACK;
    private final Color valueColor = Color.web("30c0ff");
    private final Color valueHoverColor = Color.web("30c0ff");
    private final double innerRadius = 110;
    private final double radius = 200;
    private final List<RadialMenuItem> items = new ArrayList<RadialMenuItem>();
    private final DoubleProperty initialAngle = new SimpleDoubleProperty(0);
    private final GenericRadialCenter centerNode = new GenericRadialCenter();
    private final Map<RadialMenuItem, List<RadialMenuItem>> itemToValues = new HashMap<RadialMenuItem, List<RadialMenuItem>>();
    private final Map<RadialMenuItem, Group> itemToGroupValue = new HashMap<RadialMenuItem, Group>();
    private final Map<RadialMenuItem, ImageView> itemAndValueToIcon = new HashMap<RadialMenuItem, ImageView>();
    private final Map<RadialMenuItem, ImageView> itemAndValueToWhiteIcon = new HashMap<RadialMenuItem, ImageView>();
    private final Map<RadialMenuItem, RadialMenuItem> valueItemToItem = new HashMap<RadialMenuItem, RadialMenuItem>();
    private final Group notSelectedItemEffect;
    private double menuSize;
    private SelectionEventHandler selectionEventHandler = new SelectionEventHandler();
    private RadialMenuItem selectedItem = null;
    private Transition openAnim;

    public GenericRadial(List<String> iconList) {
        initialAngle.addListener((paramObservableValue, paramT1, paramT2) -> setInitialAngle(paramObservableValue
                .getValue().doubleValue()));
        centerNode.visibleProperty().bind(visibleProperty());
        getChildren().add(itemsContainer);
        getChildren().add(centerNode);

        menuSize = 360.f / iconList.size();

        for (String iconName : iconList) {
            addMenuItem("src/main/resources/edu/wpi/cs3733d18/teamF/icons/gemicon/PNG/32x32/row 1/" + iconName);
        }


        final RadialMenuItem notSelected1 = createNotSelectedItemEffect();
        final RadialMenuItem notSelected2 = createNotSelectedItemEffect();
        notSelected2.setClockwise(false);

        notSelectedItemEffect = new Group(notSelected1, notSelected2);
        notSelectedItemEffect.setVisible(false);
        notSelectedItemEffect.setOpacity(0);

        itemsContainer.getChildren().add(notSelectedItemEffect);

        computeItemsStartAngle();

        setTranslateX(210);
        setTranslateY(210);
    }

    private RadialMenuItem createNotSelectedItemEffect() {
        final RadialMenuItem notSelectedItemEffect = RadialMenuItemBuilder
                .create().length(180).backgroundFill(baseColor).startAngle(0)
                .strokeFill(baseColor).backgroundMouseOnFill(baseColor)
                .strokeMouseOnFill(baseColor).innerRadius(innerRadius)
                .radius(radius).offset(0).clockwise(true).strokeVisible(true)
                .backgroundVisible(true).build();
        return notSelectedItemEffect;
    }

    public void addMenuItem(final String iconPath) {
        final ImageView imageView = getImageView(iconPath);

        final ImageView centerView = getImageView(iconPath.replace("32x32",
                "64x64"));
        final ImageView value1View = getImageView(iconPath.replace("row 1",
                "row 2"));
        final ImageView value2View = getImageView(iconPath.replace("row 1",
                "row 3"));
        final ImageView imageViewWhite = getImageView(iconPath.replace(".png",
                "W.png"));
        final RadialMenuItem item = newRadialMenuItem(imageView, imageViewWhite);
        final RadialMenuItem value1Item = newValueRadialMenuItem(value1View);
        final RadialMenuItem value2Item = newValueRadialMenuItem(value2View);

        valueItemToItem.put(value1Item, item);
        valueItemToItem.put(value2Item, item);
        List<RadialMenuItem> values;
        Group valueGroup;
        if (Math.random() < 0.5) {
            final ImageView value3View = getImageView(iconPath.replace("row 1",
                    "row 4"));
            final RadialMenuItem value3Item = newValueRadialMenuItem(value3View);
            valueItemToItem.put(value3Item, item);
            values = Arrays.asList(value1Item, value2Item, value3Item);
            valueGroup = new Group(value1Item, value2Item, value3Item);
        } else {
            values = Arrays.asList(value1Item, value2Item);
            valueGroup = new Group(value1Item, value2Item);
        }

        itemToValues.put(item, values);
        itemToGroupValue.put(item, valueGroup);
        valueGroup.setVisible(false);

        itemsContainer.getChildren().addAll(item, valueGroup);
        item.addEventHandler(MouseEvent.MOUSE_CLICKED, selectionEventHandler);

        centerNode.addCenterItem(item, centerView);

        item.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(final MouseEvent event) {
                        if (selectedItem == null) {
                            centerNode.displayCenter(event.getSource());
                        }
                    }
                });

        item.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(final MouseEvent event) {
                        if (selectedItem == null) {
                            centerNode.hideCenter(event.getSource());
                        }
                    }
                });

        item.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(final MouseEvent event) {
                        // TODO Animate the little long click spoiler...
                    }
                });

    }

    private RadialMenuItem newValueRadialMenuItem(final ImageView imageView) {
        final RadialMenuItem item = RadialMenuItemBuilder.create()
                .length(menuSize).backgroundFill(valueColor)
                .strokeFill(valueColor).backgroundMouseOnFill(valueHoverColor)
                .strokeMouseOnFill(valueHoverColor).innerRadius(innerRadius)
                .radius(radius).offset(0).clockwise(true).graphic(imageView)
                .backgroundVisible(true).strokeVisible(true).build();

        item.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(final MouseEvent event) {
                final RadialMenuItem valuItem = (RadialMenuItem) event
                        .getSource();
                final RadialMenuItem item = valueItemToItem.get(valuItem);
                closeValueSelection(item);
            }

        });
        itemAndValueToIcon.put(item, imageView);
        return item;
    }

    private RadialMenuItem newRadialMenuItem(final ImageView imageView,
                                             final ImageView imageViewWhite) {
        final RadialMenuItem item = RadialMenuItemBuilder.create()
                .backgroundFill(baseColor).strokeFill(baseColor)
                .backgroundMouseOnFill(hoverColor)
                .strokeMouseOnFill(hoverColor).radius(radius)
                .innerRadius(innerRadius).length(menuSize).clockwise(true)
                .backgroundVisible(true).strokeVisible(true).offset(0).build();

        if (imageViewWhite != null) {
            item.setGraphic(new Group(imageView, imageViewWhite));
            imageViewWhite.setOpacity(0.0);
        } else {
            item.setGraphic(new Group(imageView));
        }
        items.add(item);
        itemAndValueToIcon.put(item, imageView);
        itemAndValueToWhiteIcon.put(item, imageViewWhite);
        return item;
    }

    private void computeItemsStartAngle() {
        double angleOffset = initialAngle.get();
        for (final RadialMenuItem item : items) {
            item.setStartAngle(angleOffset);
            angleOffset = angleOffset + item.getLength();
        }
    }

    private void setInitialAngle(final double angle) {
        initialAngle.set(angle);
        computeItemsStartAngle();
    }

    private ImageView getImageView(final String path) {
        ImageView imageView = null;
        try {
            imageView = ImageViewBuilder.create()
                    .image(new Image(new FileInputStream(path))).build();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        assert (imageView != null);
        return imageView;

    }

    private void openValueSelection(final RadialMenuItem newSelectedItem) {
        selectedItem = newSelectedItem;

        notSelectedItemEffect.toFront();

        itemToGroupValue.get(selectedItem).setVisible(true);
        itemToGroupValue.get(selectedItem).toFront();
        selectedItem.toFront();

        openAnim = createOpenAnimation(selectedItem);
        openAnim.play();

    }

    private void closeValueSelection(final RadialMenuItem newSelectedItem) {
        openAnim.setAutoReverse(true);
        openAnim.setCycleCount(2);
        openAnim.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event) {
                newSelectedItem.setBackgroundFill(baseColor);
                newSelectedItem.setStrokeFill(baseColor);
                newSelectedItem.setBackgroundMouseOnFill(hoverColor);
                newSelectedItem.setStrokeMouseOnFill(hoverColor);
                notSelectedItemEffect.setVisible(false);
                itemToGroupValue.get(newSelectedItem).setVisible(false);
            }

        });
        openAnim.playFrom(Duration.millis(400));
        selectedItem = null;

    }

    private Transition createOpenAnimation(final RadialMenuItem newSelectedItem) {

        // Children slide animation
        final List<RadialMenuItem> children = itemToValues.get(newSelectedItem);

        double startAngleEnd = 0;
        final double startAngleBegin = newSelectedItem.getStartAngle();
        final ParallelTransition transition = new ParallelTransition();

        itemToGroupValue.get(newSelectedItem).setVisible(true);
        int internalCounter = 1;
        for (int i = 0; i < children.size(); i++) {
            final RadialMenuItem it = children.get(i);
            if (i % 2 == 0) {
                startAngleEnd = startAngleBegin + internalCounter
                        * it.getLength();
            } else {
                startAngleEnd = startAngleBegin - internalCounter
                        * it.getLength();
                internalCounter++;
            }

            final Animation itemTransition = new Timeline(new KeyFrame(
                    Duration.ZERO, new KeyValue(it.startAngleProperty(),
                    startAngleBegin)), new KeyFrame(
                    Duration.millis(400), new KeyValue(it.startAngleProperty(),
                    startAngleEnd)));

            transition.getChildren().add(itemTransition);

            final ImageView image = itemAndValueToIcon.get(it);
            image.setOpacity(0.0);
            final Timeline iconTransition = new Timeline(new KeyFrame(
                    Duration.millis(0),
                    new KeyValue(image.opacityProperty(), 0)), new KeyFrame(
                    Duration.millis(300), new KeyValue(image.opacityProperty(),
                    0)), new KeyFrame(Duration.millis(400),
                    new KeyValue(image.opacityProperty(), 1.0)));

            transition.getChildren().add(iconTransition);
        }

        // Selected item background color change
        final DoubleProperty backgroundColorAnimValue = new SimpleDoubleProperty();
        final ChangeListener<? super Number> listener = new ChangeListener<Number>() {

            @Override
            public void changed(final ObservableValue<? extends Number> arg0,
                                final Number arg1, final Number arg2) {
                final Color c = hoverColor.interpolate(selectionColor,
                        arg2.floatValue());

                newSelectedItem.setBackgroundFill(c);
                newSelectedItem.setStrokeFill(c);
                newSelectedItem.setBackgroundMouseOnFill(c);
                newSelectedItem.setStrokeMouseOnFill(c);
            }
        };

        backgroundColorAnimValue.addListener(listener);

        final Animation itemTransition = new Timeline(new KeyFrame(
                Duration.ZERO, new KeyValue(backgroundColorAnimValue, 0)),
                new KeyFrame(Duration.millis(300), new KeyValue(
                        backgroundColorAnimValue, 1.0)));
        transition.getChildren().add(itemTransition);

        // Selected item image icon color change
        final FadeTransition selectedItemImageBlackFade = FadeTransitionBuilder
                .create().node(itemAndValueToIcon.get(newSelectedItem))
                .duration(Duration.millis(400)).fromValue(1.0).toValue(0.0)
                .build();

        final FadeTransition selectedItemImageWhiteFade = FadeTransitionBuilder
                .create().node(itemAndValueToWhiteIcon.get(newSelectedItem))
                .duration(Duration.millis(400)).fromValue(0).toValue(1.0)
                .build();
        transition.getChildren().addAll(selectedItemImageBlackFade,
                selectedItemImageWhiteFade);

        // Unselected items fading
        final FadeTransition notSelectedTransition = FadeTransitionBuilder
                .create().node(notSelectedItemEffect)
                .duration(Duration.millis(200)).delay(Duration.millis(200))
                .fromValue(0).toValue(0.8).build();
        notSelectedItemEffect.setOpacity(0);
        notSelectedItemEffect.setVisible(true);

        transition.getChildren().add(notSelectedTransition);
        return transition;
    }

    public class SelectionEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(final MouseEvent event) {
            final RadialMenuItem newSelectedItem = (RadialMenuItem) event
                    .getSource();

            if (selectedItem == newSelectedItem) {
                closeValueSelection(newSelectedItem);

            } else {
                openValueSelection(newSelectedItem);
            }
        }

    }

}