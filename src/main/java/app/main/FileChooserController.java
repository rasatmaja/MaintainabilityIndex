package app.main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import animatefx.animation.FadeInLeftBig;
import animatefx.animation.FadeInRightBig;
import app.controllers.FileSearch;
import app.models.Files;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author rasio
 */
public class FileChooserController implements Initializable {

    @FXML
    private Label statusbar_directoryPath;
    @FXML
    private Label statusbar_fileFound;
    @FXML
    private Pane pane1;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnCalculate;
    @FXML
    private Label statusbar_executionTime;
    @FXML
    private AnchorPane pane_listFile;
    @FXML
    private TableView<Files> list_files_table;
    @FXML
    private TableColumn<Files, String> file_name_column;
    @FXML
    private TableColumn<Files, String> size_column;
    @FXML
    private TableColumn<Files, String> date_modified_column;
    @FXML
    private HBox pane_progress;
    @FXML
    private Label statusbar_scanning;

    long start;
    public ObservableList<Files> list_file = FXCollections.observableArrayList();
    @FXML
    private ProgressIndicator statusbar_indicator;
    @FXML
    private FontAwesomeIconView statusbar_complete;
    @FXML
    private VBox pane_home;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void openDirectoryChooser(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {
            statusbar_directoryPath.setText("No Directory selected");
        } else {
            list_file.clear();

            start = System.currentTimeMillis();
            setTableListFiles(selectedDirectory.getAbsolutePath());

            pane_listFile.toFront();
            new FadeInRightBig(pane_listFile).play();

            statusbar_directoryPath.setText(selectedDirectory.getAbsolutePath());

        }
    }

    private void setTableListFiles(String path) {
        file_name_column.setCellValueFactory(new PropertyValueFactory<>("file_name"));
        size_column.setCellValueFactory(new PropertyValueFactory<>("size"));
        size_column.setStyle("-fx-alignment: CENTER-RIGHT;");
        date_modified_column.setCellValueFactory(new PropertyValueFactory<>("date_modified"));
        date_modified_column.setStyle("-fx-alignment: CENTER;");
        
        try {
            FileSearch fileSearch = new FileSearch(path);
            statusbar_scanning.textProperty().bind(fileSearch.messageProperty());
            
            fileSearch.setOnRunning((succeesesEvent) -> {
                statusbar_complete.setVisible(false);
                statusbar_indicator.setVisible(true);
                pane_progress.setVisible(true);                
            });
            
            fileSearch.setOnSucceeded((succeededEvent) -> {
                list_file = fileSearch.getValue();
                list_files_table.setItems(list_file);
                
                statusbar_indicator.setVisible(false);
                statusbar_complete.setVisible(true);
                statusbar_fileFound.setText(list_file.size() + " Java file ");
                long time = (System.currentTimeMillis() - start);
                statusbar_executionTime.setText("Time to execution: " + time + "ms");
            });

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(fileSearch);
            executorService.shutdown();

        } catch (Exception e) {

        }

    }

    @FXML
    private void back(ActionEvent event) {
        pane_home.toFront();
        new FadeInLeftBig(pane_home).play();

        statusbar_directoryPath.setText("No directory open");
        statusbar_fileFound.setText("No java file found");
        statusbar_executionTime.setText("There is no execution yet");
        pane_progress.setVisible(false);
    }

    @FXML
    private void calculate(ActionEvent event) {
    }

}
