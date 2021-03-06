import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import kataminoLongButton.KataminoLongButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModeSelectionController implements Initializable {

    @FXML
    private KataminoLongButton kataminoArcadeButton;

    @FXML
    private KataminoLongButton kataminoCustomButton;

    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        kataminoArcadeButton.setButtonName("Classic (Arcade) Mode");
        kataminoCustomButton.setButtonName("Custom Board Mode");
    }

    @FXML
    public void arcadeSelected(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("playerSelection.fxml"));
        root.getChildren().setAll(pane);
    }

    @FXML
    public void customSelected(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("customMenu.fxml"));
        root.getChildren().setAll(pane);

    }

    @FXML
    public void backButtonClicked(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        root.getChildren().setAll(pane);
    }
}
