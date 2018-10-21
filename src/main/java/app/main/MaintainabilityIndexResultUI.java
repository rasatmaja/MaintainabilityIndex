package app.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MaintainabilityIndexResultUI{

    private static Stage primaryStage;

    private void setPrimaryStage(Stage stage) {
        MaintainabilityIndexResultUI.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return MaintainabilityIndexResultUI.primaryStage;
    }

    public void open () {
        Parent root = null;
        try {
            Stage stage = new Stage();
            setPrimaryStage(stage);
            root = FXMLLoader.load(getClass().getResource("/fxml/MaintainabilityIndexResult.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            stage.setTitle("Maintainability Index Calculation System");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
