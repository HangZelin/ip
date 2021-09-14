package duke.uimanager;

import duke.main.Duke;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * @@author Hang Zelin
 *
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    private Rectangle emptySpace;
    private Duke duke;
    private BackgroundImage backgroundImage;
    //
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.jpg"));
    private final Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.jpg"));


    //@@author HangZelin-reused
    //Reused from https://edencoding.com/scene-background/
    //with minor modifications
    private void setBackgroundImage() {
        BackgroundSize backgroundSize;

        backgroundSize = new BackgroundSize(400, 600,
                true, true, true, true);
        //Background image source from
        backgroundImage = new BackgroundImage(new Image("/background/background.jpg"),
                null, null, null, backgroundSize);
    }

    private Rectangle setEmptySpace() {
        //Set empty space between dialogs.
        emptySpace = new Rectangle(400, 40);
        emptySpace.setFill(Color.TRANSPARENT);
        return emptySpace;
    }

    /**
     * Initializes DialogBox and MainWindow stuff.
     */
    @FXML
    public void initialize() {
        //Set scrollPane
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        //Set background
        this.setBackgroundImage();
        dialogContainer.setBackground(new Background(backgroundImage));
    }

    /**
     * Constructor for MainWindow, initialize with a helloMessage.
     *
     * @param d Duke to set ui position.
     */
    public void setDuke(Duke d) {
        duke = d;
        TextUi textUi = new TextUi();
        String response = textUi.helloMessage();
        dialogContainer.getChildren().addAll(
                DialogBox.getDukeDialog(response, dukeImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = duke.getResponse(input);
        dialogContainer.getChildren().addAll(
                setEmptySpace(),
                DialogBox.getUserDialog(input, userImage),
                setEmptySpace(),
                DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
    }
}
