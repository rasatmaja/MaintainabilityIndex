package app.main;

import app.models.ErrorLog;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ErrorLogController {
    @FXML
    private JFXListView<HBox> error_listview;

    private Stage errorLogStage;

    public ErrorLogController(){
        this.errorLogStage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ErrorLog.fxml"));
            loader.setController(this);
            root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            this.errorLogStage.setScene(scene);
            this.errorLogStage.setTitle("Error Logs");
            this.errorLogStage.initModality(Modality.APPLICATION_MODAL);
            this.errorLogStage.initOwner(null);
            this.errorLogStage.show();
            populateErrorLogs();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateErrorLogs(){
        ErrorLog.getInstance().get().entrySet().forEach(error -> {
            final HBox list = new HBox();
            list.getStyleClass().add("errorList");
            list.setPrefWidth(547);
            final VBox property = new VBox();
            property.setSpacing(2);

            final Label file_name = new Label(error.getValue().get(0));
            file_name.getStyleClass().add("errorListName");
            final Label locations = new Label(error.getValue().get(1));
            locations.setWrapText(true);
            locations.getStyleClass().add("errorListLocations");
            final Label message = new Label(error.getValue().get(2));
            message.getStyleClass().add("errorListMessage");

            property.getChildren().addAll(file_name, locations, message);
            final ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/img/error.png")));
            icon.setPreserveRatio(true);
            icon.setFitWidth(60);
            list.getChildren().addAll(icon, property);

            list.setOnMouseClicked(event -> {
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + error.getValue().get(1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            error_listview.getItems().add(list);
        });
    }
}
