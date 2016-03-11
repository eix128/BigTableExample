package main;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

// animates moving a node forever in a random walk pattern.
public class RandomWalk extends Application {

    private static final Random random = new Random(42);

    private static final double W = 200;
    private static final double H = 200;
    private static final double R = 10;

    private static final Node node = new Circle(
            R, Color.FORESTGREEN
    );

    @Override
    public void start(Stage stage) {
        // start from the center of the screen.
        node.relocate(W / 2 - R, H / 2 - R);

        stage.setScene(new Scene(new Pane(node), W, H));
        stage.show();

        walk();
    }

    // start walking from the current position to random points in sequence.
    private void walk() {
        final Point2D to = getRandomPoint();
        final Transition transition = createMovementTransition(
                node,
                to
        );

        transition.setOnFinished(
                walkFrom(to)
        );
        transition.play();
    }

    private EventHandler<ActionEvent> walkFrom(final Point2D from) {
        return event -> {
            // Finished handler might be called a frame before transition complete,
            // leading to glitches if we relocate in the handler.
            // The transition works by manipulating translation values,
            // so zero the translation out before relocating the node.
            node.setTranslateX(0);
            node.setTranslateY(0);

            // After the transition is complete, move the node to the new location.
            // Relocation co-ordinates are adjusted by the circle's radius.
            // For a standard node, the R adjustment would be unnecessary 
            // as most nodes are located at the top left corner of the node 
            // rather than at the center like a circle is.
            node.relocate(
                    from.getX() - R,
                    from.getY() - R
            );

            // Generate the next random point and play a transition to walk to it.
            // I'd rather not use recursion here as if you recurse long enough,
            // then you will end up with a stack overflow, but I'm not quite sure
            // how to do this without recursion.
            final Point2D next = getRandomPoint();
            final Transition transition = createMovementTransition(node, next);
            transition.setOnFinished(walkFrom(next));
            transition.play();
        };
    }

    // We use a PathTransition to move from the current position to the next.
    // For the simple straight-line movement we are doing,
    // a straight TranslateTransition would have been fine.
    // A PathTransition is just used to demonstrate that this
    // can work for the generic path case, not just straight line movement.
    private Transition createMovementTransition(Node node, Point2D to) {
        Path path = new Path(
                new MoveTo(
                        0,
                        0
                ),
                new LineTo(
                        to.getX() - node.getLayoutX(),
                        to.getY() - node.getLayoutY()
                )
        );

        return new PathTransition(
                Duration.seconds(2),
                path,
                node
        );
    }

    // @return a random location within a bounding rectangle (0, 0, W, H)
    // with a margin of R kept between the point and the bounding rectangle edge.
    private Point2D getRandomPoint() {
        return new Point2D(
                random.nextInt((int) (W - 2*R)) + R,
                random.nextInt((int) (H - 2*R)) + R
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}