package com.zetcode;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

/**
 * ZetCode JavaFX tutorial
 *
 * This program draws three lines which
 * form a rectangle.
 *
 * Author: Jan Bodnar
 * Website: zetcode.com
 * Last modified: June 2015
 */

public class SimpleLinesEx extends Application {

    @Override
    public void start(Stage stage) {

        initUI(stage);
    }

    private void initUI(Stage stage) {

        Pane root = new Pane();

        Canvas canvas = new Canvas(300, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawLines(gc);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 300, 250, Color.WHITESMOKE);

        Arc a2 = new Arc(100, 100, 100, 100, 0 , 90);
        a2.setType(ArcType.OPEN);
        a2.setStroke(Color.BLACK);
        a2.setFill(null);
        a2.setStrokeWidth(3);
        final DoubleProperty doubleProperty = a2.rotateProperty();

//        new AnimationTimer(new );

        root.getChildren().add(a2);

        stage.setTitle("Lines");
        stage.setScene(scene);
        stage.show();
    }

    private void drawLines(GraphicsContext gc) {

        gc.beginPath();
        gc.moveTo(30.5, 30.5);
        gc.lineTo(150.5, 30.5);
        gc.lineTo(150.5, 150.5);
        gc.lineTo(30.5, 30.5);
        gc.stroke();



    }

    public static void main(String[] args) {
        launch(args);
    }
}