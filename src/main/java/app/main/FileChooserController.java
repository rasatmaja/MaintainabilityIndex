package app.main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import animatefx.animation.*;
import app.controllers.ASTExtractions;
import app.controllers.FileSearch;
import app.models.ClassProperty;
import app.models.FilePath;
import app.models.Files;
import app.models.MethodProperty;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    @FXML
    private ProgressIndicator statusbar_indicator;
    @FXML
    private FontAwesomeIconView statusbar_complete;
    @FXML
    private VBox pane_home;

    private long start;
    private ObservableList<Files> list_file = FXCollections.observableArrayList();
    private FilePath filePath;
    private ClassProperty classProperty;
    private MethodProperty methodProperty;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filePath = FilePath.getInstance();
        classProperty = ClassProperty.getInstance();
        methodProperty = MethodProperty.getInstance();

        list_files_table.setVisible(false);
        btnBack.setVisible(false);
        btnCalculate.setVisible(false);
    }

    @FXML
    private void openDirectoryChooser(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {
            statusbar_directoryPath.setText("No Directory selected");
        } else {
            start = System.currentTimeMillis();
            setTableListFiles(selectedDirectory.getAbsolutePath());

            pane_listFile.toFront();
            new FadeInRightBig(pane_listFile).play();

            statusbar_directoryPath.setText(selectedDirectory.getName());
            filePath.set(selectedDirectory.getName());

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

                list_files_table.setVisible(true);
                new FadeInDown(list_files_table).play();
                btnBack.setVisible(true);
                new FadeInLeft(btnBack).play();
                btnCalculate.setVisible(true);
                new FadeInRight(btnCalculate).play();
            });

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(fileSearch);
            executorService.shutdownNow();

        } catch (Exception e) {

        }

    }

    @FXML
    private void back(ActionEvent event) {

        new FadeOutDown(list_files_table).play();
        list_files_table.setVisible(false);
        new FadeOutLeft(btnBack).play();
        btnBack.setVisible(true);
        new FadeOutRight(btnCalculate).play();
        btnCalculate.setVisible(true);
        pane_home.toFront();
        new FadeInLeftBig(pane_home).play();
        
        list_file.clear();
        filePath.clear();
        classProperty.clear();
        
        statusbar_directoryPath.setText("No directory open");
        statusbar_fileFound.setText("No java file found");
        statusbar_executionTime.setText("There is no execution yet");
        pane_progress.setVisible(false);
    }

    @FXML
    private void calculate(ActionEvent event){

        try {
            start = System.currentTimeMillis();
            ASTExtractions astExtractions = new ASTExtractions();
            statusbar_scanning.textProperty().bind(astExtractions.messageProperty());


            astExtractions.setOnRunning((succeesesEvent) -> {
                statusbar_complete.setVisible(false);
                statusbar_indicator.setVisible(true);
                pane_progress.setVisible(true);
                statusbar_executionTime.setText("Calculating...");
            });

            astExtractions.setOnSucceeded((succeededEvent) -> {
                statusbar_indicator.setVisible(false);
                statusbar_complete.setVisible(true);
                statusbar_fileFound.setText(list_file.size() + " Java file ");
                long time = (System.currentTimeMillis() - start);
                statusbar_executionTime.setText("Time to extractions: " + time + "ms");
                //debug();
                new MaintainabilityIndexResultController();
            });

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(astExtractions);
            executorService.shutdown();

        } catch (Exception e) {

        }

    }

    public void debug(){

        classProperty.get().entrySet().forEach((classAttribut) -> {
            int key = classAttribut.getKey();
            List<String> values = classAttribut.getValue();
            System.out.println((char)27 + "[30m" + "[DEBUG]");
            System.out.println("Class Name : " + values.get(0));
            System.out.println("LOC        : " + values.get(1));
            System.out.println("Comments   : " + values.get(2));
            //System.out.println("SC         : \n" + values.get(3));
            System.out.println("Class Type : " + values.get(4));
            System.out.println("----------------------------------------");
            System.out.println();
        });

        methodProperty.get().entrySet().forEach(dataMethodProperty ->{
            int key = dataMethodProperty.getKey();
            System.out.println((char)27 + "[34m" + "[DEBUG]");
            System.out.println("Class name  : " + dataMethodProperty.getValue().get(0));
            System.out.println("Method name : " + dataMethodProperty.getValue().get(1));
            System.out.println("LOC         : " + dataMethodProperty.getValue().get(2));
            System.out.println("Comment     : " + dataMethodProperty.getValue().get(3));
            System.out.println("source Code : \n" + dataMethodProperty.getValue().get(4));
            System.out.println("----------------------------------------");
            System.out.println();
        });
    }



}
