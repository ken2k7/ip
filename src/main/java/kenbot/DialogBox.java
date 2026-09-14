package kenbot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * One line of the conversation: a message beside the picture of whoever said it.
 *
 * <p>Extending {@link HBox} means a dialog box <em>is</em> a horizontal layout
 * rather than merely containing one, so it can be dropped straight into another
 * container without unwrapping.</p>
 */
public class DialogBox extends HBox {

    private final Label text;
    private final ImageView displayPicture;

    /**
     * Creates a dialog box showing one message.
     *
     * @param message what was said
     * @param picture the speaker's picture
     */
    public DialogBox(String message, Image picture) {
        text = new Label(message);
        displayPicture = new ImageView(picture);

        // Wrapping matters because a task description can be longer than the
        // window is wide; without it the text would be cut off.
        text.setWrapText(true);
        displayPicture.setFitWidth(100.0);
        displayPicture.setFitHeight(100.0);
        this.setAlignment(Pos.TOP_RIGHT);

        this.getChildren().addAll(text, displayPicture);
    }

    /**
     * Creates a dialog box for something the user said.
     *
     * @param message what the user typed
     * @param picture the user's picture
     * @return a dialog box aligned to the right
     */
    public static DialogBox getUserDialog(String message, Image picture) {
        return new DialogBox(message, picture);
    }

    /**
     * Creates a dialog box for something Kenbot said.
     *
     * @param message Kenbot's reply
     * @param picture Kenbot's picture
     * @return a dialog box aligned to the left, mirroring the user's
     */
    public static DialogBox getKenbotDialog(String message, Image picture) {
        DialogBox box = new DialogBox(message, picture);
        box.flip();
        return box;
    }

    /**
     * Mirrors this dialog box so the picture is on the left and the text on the
     * right, which is what tells the two speakers apart at a glance.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> reordered = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(reordered);
        this.getChildren().setAll(reordered);
    }
}
