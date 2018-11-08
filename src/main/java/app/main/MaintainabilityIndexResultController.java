/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.main;

import animatefx.animation.*;
import app.controllers.*;
import app.models.FilePath;
import app.models.MaintainabilityIndexProperty;
import app.models.MaintainabilityIndexResult;
import app.models.MethodProperty;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rasio
 */
public class MaintainabilityIndexResultController implements Initializable {

    public TreeTableView<MaintainabilityIndexProperty> MI_TreeTableView;
    @FXML
    public VBox indicator_pane;
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
    private JFXButton btnClose;
    @FXML
    private JFXButton btnVisualization;

    private final Stage maintainabilityIndexResultStage;
    long start;
    MaintainaibilityIndexCalculation maintainaibilityIndexCalculation;
    MaintainabilityIndexResult maintainabilityIndexResult;
    MethodProperty methodProperty;
    FilePath filePath;

    @FXML
    private TreeTableColumn<MaintainabilityIndexProperty, String> id_column;
    @FXML
    private TreeTableColumn<MaintainabilityIndexProperty, String> name_column;
    @FXML
    private TreeTableColumn<MaintainabilityIndexProperty, String> maintainabiliiti_index_column;
    @FXML
    private TreeTableColumn<MaintainabilityIndexProperty, String> status_column;

    private final Image ICON_HIGH = new Image(getClass().getResourceAsStream("/img/high.png"), 13, 13, false, true);
    private final Image ICON_MODERATE = new Image(getClass().getResourceAsStream("/img/moderate.png"), 13, 13, false, true);
    private final Image ICON_LOW = new Image(getClass().getResourceAsStream("/img/low.png"), 13, 13, false, true);

    public MaintainabilityIndexResultController(){
        this.maintainabilityIndexResultStage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MaintainabilityIndexResult.fxml"));
            loader.setController(this);
            root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            this.maintainabilityIndexResultStage.setScene(scene);
            this.maintainabilityIndexResultStage.setTitle("Maintainability Index Result");
            this.maintainabilityIndexResultStage.initModality(Modality.APPLICATION_MODAL);
            this.maintainabilityIndexResultStage.initOwner(null);
            this.maintainabilityIndexResultStage.show();
            this.maintainabilityIndexResultStage.setOnCloseRequest(event -> {
                clearAllDataModel();
            });
            start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void close(ActionEvent event) {
        clearAllDataModel();
    }
    private void clearAllDataModel(){
        this.maintainabilityIndexResultStage.close();
        ClearData clearData = new ClearData();
        clearData.execute();
    }

    @FXML
    public void Visualization(ActionEvent actionEvent) {
        new VisualizationController();
    }

    public void start(){
        MI_TreeTableView.setVisible(false);
        btnClose.setVisible(false);
        btnVisualization.setVisible(false);
        indicator_pane.setVisible(false);


        this.maintainabilityIndexResult = MaintainabilityIndexResult.getInstance();
        this.methodProperty = MethodProperty.getInstance();
        this.maintainaibilityIndexCalculation = new MaintainaibilityIndexCalculation();
        this.filePath = FilePath.getInstance();

        statusbar_directoryPath.setText(filePath.getRootDorectory());

        try {
            start = System.currentTimeMillis();
            statusbar_scanning.textProperty().bind(this.maintainaibilityIndexCalculation.messageProperty());

            this.maintainaibilityIndexCalculation.setOnRunning((succeesesEvent) -> {
                statusbar_complete.setVisible(false);
                statusbar_indicator.setVisible(true);
                pane_progress.setVisible(true);
                statusbar_executionTime.setText("Calculating...");
                statusbar_fileFound.setText("Calculating...");
            });

            this.maintainaibilityIndexCalculation.setOnSucceeded(succeededEvent -> {
                calculateAVG();

                statusbar_indicator.setVisible(false);
                statusbar_complete.setVisible(true);

                long time = (System.currentTimeMillis() - start);
                statusbar_executionTime.setText("Time to calculations: " + time + "ms");
                statusbar_fileFound.setText(methodProperty.get().size()+" method has been calculated");

                populateTreeTable();

                MI_TreeTableView.setVisible(true);
                new FadeInDown(MI_TreeTableView).play();
                btnClose.setVisible(true);
                new FadeInLeft(btnClose).play();
                btnVisualization.setVisible(true);
                new FadeInRight(btnVisualization).play();
                indicator_pane.setVisible(true);
                new FadeInLeft(indicator_pane).play();
            });

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(maintainaibilityIndexCalculation);
            executorService.shutdownNow();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateTreeTable(){

        String tempClassName ="";
        TreeItem<MaintainabilityIndexProperty> className = null;
        TreeItem<MaintainabilityIndexProperty> root = new TreeItem<>(new MaintainabilityIndexProperty("","",0.0));
        for (Map.Entry<Integer, List<String>> property : methodProperty.get().entrySet()) {
            int key = property.getKey();
            System.out.println("ID         : " + key);
            System.out.println("Class Name : " + property.getValue().get(0));

            System.out.println("Method Name: " + property.getValue().get(1));
            System.out.println("MI Value   : " + maintainabilityIndexResult.get().get(key));

            String classNameProperty = property.getValue().get(0);
            String methodNameProperty = property.getValue().get(1);
            double miValue = maintainabilityIndexResult.get().get(key);
            double avgMI = maintainabilityIndexResult.getListOfAvgMaintainabilityIndex().get(classNameProperty);

            Image iconMethod;
            Image iconClass;

            if (miValue > 85 ){
                iconMethod = this.ICON_HIGH;
            } else if (miValue <= 85 && miValue >65){
                iconMethod = this.ICON_MODERATE;
            } else {
                iconMethod = this.ICON_LOW;
            }

            if (avgMI > 85){
                iconClass = this.ICON_HIGH;
            } else if (avgMI <= 85 && avgMI >65){
                iconClass = this.ICON_MODERATE;
            } else {
                iconClass = this.ICON_LOW;
            }

            if (tempClassName != classNameProperty) {
                tempClassName = classNameProperty;

                className = new TreeItem<>(new MaintainabilityIndexProperty("", property.getValue().get(0), avgMI), new ImageView(iconClass));
                TreeItem<MaintainabilityIndexProperty> method = new TreeItem<>(new MaintainabilityIndexProperty(property.getKey().toString(), methodNameProperty, miValue), new ImageView(iconMethod));

                className.getChildren().add(method);
                className.setExpanded(true);
                root.getChildren().add(className);
            } else{
                TreeItem<MaintainabilityIndexProperty> method = new TreeItem<>(new MaintainabilityIndexProperty(property.getKey().toString(), methodNameProperty, miValue), new ImageView(iconMethod));
                className.getChildren().add(method);
                className.setExpanded(true);

            }
        }


        id_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<MaintainabilityIndexProperty, String> param) -> param.getValue().getValue().idProperty());
        id_column.setVisible(false);
        name_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<MaintainabilityIndexProperty, String> param) -> param.getValue().getValue().nameProperty());
        maintainabiliiti_index_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<MaintainabilityIndexProperty, String> param) -> param.getValue().getValue().maintainability_indexProperty());
        status_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<MaintainabilityIndexProperty, String> param) -> param.getValue().getValue().statusProperty());

        maintainabiliiti_index_column.setStyle("-fx-alignment: CENTER-RIGHT;");
        status_column.setStyle("-fx-alignment: CENTER;");

        root.setExpanded(true);
        MI_TreeTableView.setRoot(root);
        MI_TreeTableView.setShowRoot(false);

    }

    @FXML
    public void treeTableClick (MouseEvent event){
        if(event.getClickCount() == 2){
            DetailsController detailsController = new DetailsController();

            if (MI_TreeTableView.getSelectionModel().getSelectedItem().valueProperty().getValue().getId().equalsIgnoreCase("")){
                String className = MI_TreeTableView.getSelectionModel().getSelectedItem().valueProperty().getValue().getName();
                detailsController.setClassName(className);
            } else{
                int methodKey = Integer.valueOf(MI_TreeTableView.getSelectionModel().getSelectedItem().valueProperty().getValue().getId());
                detailsController.setMethodKey(methodKey);
            }
            detailsController.showStage();
        }
    }

    public void calculateAVG(){
        AvgMaintainabilityIndexCalculations aa = new AvgMaintainabilityIndexCalculations();
        aa.calculate();

        HalsteadMetricsCalculation halsteadMetricsCalculation = HalsteadMetricsCalculation.getInstance();
        CyclomaticComplexityCalculations cyclomaticComplexityCalculations = CyclomaticComplexityCalculations.getInstance();

        halsteadMetricsCalculation.calculateAvg();
        cyclomaticComplexityCalculations.calculateAvg();
    }
}
