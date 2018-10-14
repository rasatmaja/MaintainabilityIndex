/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.main;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void close(ActionEvent event) {
        MaintainabilityIndexResultUI.getPrimaryStage().close();
    }

    public void calculateHalsteadMetrics(){}
    public void calculateCyclomaticComplexity(){}
    public void calculateMaintainabilityIndex(){}
}
