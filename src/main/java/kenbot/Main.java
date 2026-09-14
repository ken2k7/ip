package kenbot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The JavaFX entry point of Kenbot.
 *
 * <p>Extending {@link Application} is what makes this a JavaFX program: the
 * toolkit starts itself up and then calls {@link #start(Stage)} once the
 * window system is ready. Nothing here is called directly.</p>
 */
public class Main extends Application {

    /**
     * Builds and shows the first window.
     *
     * <p>The stage is the window supplied by the operating system, the scene is
     * everything drawn inside it, and the scene holds exactly one top node,
     * here a label.</p>
     *
     * @param stage the window JavaFX created for this application
     */
    @Override
    public void start(Stage stage) {
        Label helloWorld = new Label("Hello World!");
        Scene scene = new Scene(helloWorld);

        stage.setScene(scene);
        stage.show();
    }
}
