package kenbot;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * One line of the conversation: a message beside the picture of whoever said it.
 *
 * <p>This is a custom control, so it is loaded differently from the main
 * window. Rather than the FXML naming its controller, the constructor hands the
 * loader this very object as both the root and the controller. That is what
 * {@code <fx:root>} in {@code DialogBox.fxml} means, and it is what lets a
 * dialog box be created with {@code new} like any other object.</p>
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            // Both must be set before load(): afterwards the loader has already
            // decided what to build and who to wire it to.
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Creates a dialog box for something the user said.
     *
     * @param text what the user typed
     * @param img the user's picture
     * @return a dialog box aligned to the right
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Creates a dialog box for something Kenbot said.
     *
     * @param text Kenbot's reply
     * @param img Kenbot's picture
     * @return a dialog box aligned to the left, mirroring the user's
     */
    public static DialogBox getKenbotDialog(String text, Image img) {
        DialogBox box = new DialogBox(text, img);
        box.flip();
        return box;
    }

    /**
     * Mirrors this dialog box so the picture is on the left and the text on the
     * right, which is what tells the two speakers apart at a glance.
     */
    private void flip() {
        // Copied out first: getChildren() is a live list the scene graph is
        // watching, so it cannot be reversed in place.
        ObservableList<Node> reordered = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(reordered);
        this.getChildren().setAll(reordered);
        this.setAlignment(Pos.TOP_LEFT);
    }
}
