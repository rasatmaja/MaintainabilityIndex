/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.main;

import app.controllers.MaintainaibilityIndexCalculation;
import app.models.OperandAndOperator;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author rasio
 */
public class MaintainabilityIndexResultController implements Initializable {

    @FXML
    private Label statusbar_directoryPath;
    @FXML
    private Label statusbar_fileFound;
    @FXML
    private Label statusbar_executionTime;
    @FXML
    private HBox pane_progress;
    @FXML
    private ProgressIndicator statusbar_indicator;
    @FXML
    private FontAwesomeIconView statusbar_complete;
    @FXML
    private Label statusbar_scanning;
    @FXML
    private AnchorPane pane_listFile;
    @FXML
    private TableColumn<?, ?> file_name_column;
    @FXML
    private TableColumn<?, ?> size_column;
    @FXML
    private TableColumn<?, ?> date_modified_column;
    @FXML
    private JFXButton btnClose;
    @FXML
    private JFXButton btnVisualization;
    @FXML
    private TableView<?> maintainabilityIndexResult_table;

    long start;
    MaintainaibilityIndexCalculation maintainaibilityIndexCalculation;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       this.maintainaibilityIndexCalculation = new MaintainaibilityIndexCalculation();
    }    

    @FXML
    private void close(ActionEvent event) {
        MaintainabilityIndexResultUI.getPrimaryStage().close();
    }

    public void Visualization(ActionEvent actionEvent) {
        OperandAndOperator operandAndOperator = OperandAndOperator.getInstance();
        operandAndOperator.debug();

        try {
            start = System.currentTimeMillis();
            statusbar_scanning.textProperty().bind(this.maintainaibilityIndexCalculation.messageProperty());

            this.maintainaibilityIndexCalculation.setOnRunning((succeesesEvent) -> {
                statusbar_complete.setVisible(false);
                statusbar_indicator.setVisible(true);
                pane_progress.setVisible(true);
                statusbar_executionTime.setText("Calculating...");
            });

            this.maintainaibilityIndexCalculation.setOnSucceeded(succeededEvent -> {
                statusbar_indicator.setVisible(false);
                statusbar_complete.setVisible(true);

                long time = (System.currentTimeMillis() - start);
                statusbar_executionTime.setText("Time to extractions: " + time + "ms");
            });

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(maintainaibilityIndexCalculation);
            executorService.shutdown();

        }catch (Exception e) {

        }
    }
}
