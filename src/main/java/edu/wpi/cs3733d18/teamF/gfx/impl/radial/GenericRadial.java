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
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

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
    private final Group textsGroup = new Group();
    private final double innerRadius = 100;
    private final double radius = 250;
    private final List<RadialMenuItem> items = new ArrayList<RadialMenuItem>();
    private final DoubleProperty initialAngle = new SimpleDoubleProperty(0);
    private final GenericRadialCenter centerNode = new GenericRadialCenter();
    private final Map<RadialMenuItem, List<RadialMenuItem>> itemToValues = new HashMap<RadialMenuItem, List<RadialMenuItem>>();
    private final Map<RadialMenuItem, Group> itemToGroupValue = new HashMap<RadialMenuItem, Group>();
    private final Map<RadialMenuItem, ImageView> itemAndValueToIcon = new HashMap<RadialMenuItem, ImageView>();
    private final Map<RadialMenuItem, ImageView> itemAndValueToWhiteIcon = new HashMap<RadialMenuItem, ImageView>();
    private final Map<RadialMenuItem, RadialMenuItem> valueItemToItem = new HashMap<RadialMenuItem, RadialMenuItem>();
    private final Group notSelectedItemEffect;
    private final Map<RadialMenuItem, List<Text>> itemToTexts;
    private double animDuration = 350;
    private double menuSize;
    private SelectionEventHandler selectionEventHandler = new SelectionEventHandler();
    private RadialMenuItem selectedItem = null;
    private Transition openAnim;
    private double centerClosedRadius = 28;
    private double centerOpenedRadius = 40;

    public GenericRadial(List<Pair<Pair<String, String>, Runnable>> selections) {
        initialAngle.addListener((paramObservableValue, paramT1, paramT2) -> setInitialAngle(paramObservableValue
                .getValue().doubleValue()));
        centerNode.visibleProperty().bind(visibleProperty());
        getChildren().add(itemsContainer);
        getChildren().add(centerNode);

        itemToTexts = new HashMap<>();

        menuSize = 360.f / selections.size();

        for (Pair<Pair<String, String>, Runnable> selection : selections) {
            addMenuItem("edu/wpi/cs3733d18/teamF/icons/gemicon/PNG/64x64/row 1/" + selection.getKey().getKey()
                    , selection.getKey().getValue()
                    , selection.getValue());
        }
        getChildren().addAll(textsGroup);

        final List<KeyValue> keyValueZero = new ArrayList<KeyValue>();
        final List<KeyValue> keyValueFinal = new ArrayList<KeyValue>();

        computeItemsStartAngle();

        final ParallelTransition openTransition = new ParallelTransition();
        for (final RadialMenuItem item : items) {
            keyValueZero.add(new KeyValue(item.innerRadiusProperty(),
                    centerClosedRadius));
            keyValueZero.add(new KeyValue(item.radiusProperty(),
                    centerClosedRadius));

            keyValueFinal.add(new KeyValue(item.innerRadiusProperty(),
                    innerRadius));
            keyValueFinal.add(new KeyValue(item.radiusProperty(), radius));

            final Animation textTransition = getTextOpenTransition(item);
            openTransition.getChildren().add(textTransition);
        }

        openTransition.play();

        final RadialMenuItem notSelected1 = createNotSelectedItemEffect();
        final RadialMenuItem notSelected2 = createNotSelectedItemEffect();
        notSelected2.setClockwise(false);

        notSelectedItemEffect = new Group(notSelected1, notSelected2);
        notSelectedItemEffect.setVisible(false);
        notSelectedItemEffect.setOpacity(0);

        itemsContainer.getChildren().add(notSelectedItemEffect);
    }

    public Animation getTextOpenTransition(final RadialMenuItem item) {
        final List<Text> texts = itemToTexts.get(item);
        final double textRadius = (innerRadius + radius) / 2.0;
        final double startAngle = item.getStartAngle();
        final double length = item.getLength() * 0.9;
        final double angleOffset = item.getLength() * 0.1;
        final double angleStep = (length) / (texts.size() + 1);

        for (final Text charText : texts) {
            charText.setEffect(null);
            charText.setVisible(true);
        }

        final DoubleProperty animValue = new SimpleDoubleProperty();
        final ChangeListener<? super Number> listener = new ChangeListener<Number>() {

            Font[] fonts = new Font[]{
                    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 6),
                    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 7),
                    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 8),
                    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 10),
                    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 20)};

            @Override
            public void changed(
                    final ObservableValue<? extends Number> obsValue,
                    final Number previousValue, final Number newValue) {
                final double textRotationOffset = 180;
                final double radius = centerClosedRadius
                        + (textRadius - centerClosedRadius)
                        * newValue.doubleValue() + 100;

                double letterAngle = startAngle + angleStep + angleOffset
                        + ((1 - newValue.doubleValue()) * textRotationOffset);

                final Font f = getTextFont(newValue.doubleValue());

                for (final Text charText : texts) {
                    charText.setRotate(0);
                    charText.setFont(f);
                    final Bounds bounds = charText.getBoundsInParent();
                    final double lettertWidth = bounds.getWidth();
                    final double lettertHeight = bounds.getHeight();

                    final double currentX = xCenterOnCircle(letterAngle,
                            radius, lettertWidth);
                    final double currentY = yCenterLetterOnCircle(letterAngle,
                            radius, lettertHeight);
                    final double rotate = rotate(letterAngle);

                    charText.setTranslateX(currentX);
                    charText.setTranslateY(currentY);
                    charText.setRotate(rotate);

                    letterAngle += angleStep;
                }

            }


            private Font getTextFont(final double newValue) {
                final int fontArrayIndex;
                if (newValue < 0.2) {
                    fontArrayIndex = 0;
                } else if (newValue < 0.4) {
                    fontArrayIndex = 1;
                } else if (newValue < 0.6) {
                    fontArrayIndex = 2;
                } else if (newValue < 0.8) {
                    fontArrayIndex = 3;
                } else {
                    fontArrayIndex = 4;
                }
                return fonts[fontArrayIndex];
            }
        };
        animValue.addListener(listener);

        final Animation itemTransition = new Timeline(new KeyFrame(
                Duration.ZERO, new KeyValue(animValue, 0)), new KeyFrame(
                Duration.millis(animDuration), new KeyValue(animValue, 1.0)));
        itemTransition.setOnFinished(new EventHandler<ActionEvent>() {

            boolean visible = false;

            @Override
            public void handle(final ActionEvent event) {
                for (final Text charText : texts) {
                    charText.setEffect(new Glow());
                    if (visible) {
                        charText.setVisible(false);
                    }
                }
                visible = !visible;
            }

        });
        return itemTransition;

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

    private List<Text> getTextNodes(final String title, final double startAngle) {
        final List<Text> texts = new ArrayList<Text>();
        final char[] titleCharArray = title.toCharArray();

        for (int i = titleCharArray.length - 1; i >= 0; i--) {
            final Text charText = new Text(
                    Character.toString(titleCharArray[i]));
            charText.setFontSmoothingType(FontSmoothingType.LCD);
            charText.setSmooth(true);
            charText.setMouseTransparent(true);
            charText.setBlendMode(BlendMode.COLOR_BURN);
            texts.add(charText);
        }

        return texts;
    }

    private double xCenterOnCircle(final double angle, final double radius,
                                   final double width) {
        return radius * Math.cos(Math.toRadians(angle)) - width / 2.0;
    }

    private double yCenterLetterOnCircle(final double angle,
                                         final double radius, final double height) {
        return -radius * Math.sin(Math.toRadians(angle)) + height / 4.0;
    }

    private double rotate(final double angle) {
        final double rotate = 90 - angle;
        return rotate;
    }


    public void addMenuItem(final String iconPath, String value, Runnable fun) {
        final ImageView imageView = getImageView(iconPath);

        final ImageView centerView = getImageView(iconPath.replace("32x32",
                "64x64"));
        final ImageView imageViewWhite = getImageView(iconPath.replace(".png",
                ".png"));
        final RadialMenuItem item = newRadialMenuItem(imageView, imageViewWhite);

        List<RadialMenuItem> values = new LinkedList<>();

        List<Text> texts = getTextNodes(value, item.getStartAngle());

        itemToValues.put(item, values);
        itemToTexts.put(item, texts);
        textsGroup.getChildren().addAll(texts);

        itemsContainer.getChildren().addAll(item);
        item.addEventHandler(MouseEvent.MOUSE_CLICKED, selectionEventHandler);

        centerNode.addCenterItem(item, centerView);

        item.addEventHandler(MouseEvent.MOUSE_ENTERED,
                event -> {
                    if (selectedItem == null) {
                        centerNode.displayCenter(event.getSource());
                    }
                });

        item.addEventHandler(MouseEvent.MOUSE_EXITED,
                event -> {
                    if (selectedItem == null) {
                        centerNode.hideCenter(event.getSource());
                    }
                });

        item.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> fun.run()
        );

    }

    private RadialMenuItem newValueRadialMenuItem(final ImageView imageView) {
        final RadialMenuItem item = RadialMenuItemBuilder.create()
                .length(menuSize).backgroundFill(valueColor)
                .strokeFill(valueColor).backgroundMouseOnFill(valueHoverColor)
                .strokeMouseOnFill(valueHoverColor).innerRadius(innerRadius)
                .radius(radius).offset(0).clockwise(true).graphic(imageView)
                .backgroundVisible(true).strokeVisible(true).build();

        item.setOnMouseClicked(event -> {
            final RadialMenuItem valuItem = (RadialMenuItem) event
                    .getSource();
            final RadialMenuItem item1 = valueItemToItem.get(valuItem);
            closeValueSelection(item1);
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
        imageView = ImageViewBuilder.create()
                .image(new Image(path)).build();
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
                // closeValueSelection(newSelectedItem);

            } else {
                // openValueSelection(newSelectedItem);
            }
        }

    }

}