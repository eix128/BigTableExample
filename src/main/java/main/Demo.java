package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Created by kadir.basol on 8.3.2016.
 */
public class Demo extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        stackPane.getChildren().addAll(infrastructurePane(), getFilterPane());
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();
    }

    public Pane getFilterPane() {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Option 1", "Option 2", "Option 3");
        ComboBox<String> combo = new ComboBox<String>(options);

        HBox pane = new HBox();
        pane.setPadding(new Insets(20));
        pane.setStyle("-fx-background-color: rgba(255,0,85,0.4)");
        pane.getChildren().add(combo);
        pane.setMaxHeight(40);
        // Optional
        //pane.setEffect(new DropShadow(15, Color.RED));
        return pane;
    }

    public ScrollPane infrastructurePane() {

        final FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(5);
        flow.setHgap(5);
        flow.setAlignment(Pos.CENTER);

        final ScrollPane scroll = new ScrollPane();

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setContent(flow);
//        scroll.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
//            @Override
//            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
//                flow.setPrefWidth(bounds.getWidth());
//                flow.setPrefHeight(bounds.getHeight());
//            }
//        });

        //flow.setPrefWrapLength(170); // preferred width allows for two columns
        flow.setStyle("-fx-background-color: yellow;");

        for (int i = 0; i < 28; i++) {
            flow.getChildren().add(generateRectangle());
        }

        String cssURL = "/com/dx57dc/css/ButtonsDemo.css";
        String css = this.getClass().getResource(cssURL).toExternalForm();
        flow.getStylesheets().add(css);

        return scroll;
    }

    public Rectangle generateRectangle() {

        final Rectangle rect2 = new Rectangle(10, 10, 10, 10);
        rect2.setId("app");
        rect2.setArcHeight(8);
        rect2.setArcWidth(8);
        //rect2.setX(10);
        //rect2.setY(160);
        rect2.setStrokeWidth(1);
        rect2.setStroke(Color.WHITE);
        rect2.setWidth(220);
        rect2.setHeight(180);

        rect2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rect2.setFill(Color.ALICEBLUE);
            }
        });

        return rect2;
    }

    public static void main(String[] args) {
        launch(args);
    }
}