/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.main;

import animatefx.animation.*;
import app.controllers.*;
import app.models.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
    @FXML
    private Label statusbar_errorLog;
    @FXML
    private JFXTextField input_filter;
    @FXML
    private Label export;

    private final Stage maintainabilityIndexResultStage;
    private long start;
    private MaintainaibilityIndexCalculation maintainaibilityIndexCalculation;
    private MaintainabilityIndexResult maintainabilityIndexResult;
    private MethodProperty methodProperty;
    private FilePath filePath;
    private int totolRow;

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


    TreeItem<MaintainabilityIndexProperty> root = new TreeItem<>(new MaintainabilityIndexProperty("","",0.0));

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
        input_filter.setVisible(false);


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
                //statusbar_fileFound.setText(methodProperty.get().size()+" method has been calculated");
                statusbar_fileFound.setText(methodProperty.get().size()+" method");

                populateTreeTable();
                input_filter.setVisible(true);
                new FadeInDown(input_filter).play();
                MI_TreeTableView.setVisible(true);
                new FadeInDown(MI_TreeTableView).play();
                btnClose.setVisible(true);
                new FadeInLeft(btnClose).play();
                btnVisualization.setVisible(true);
                new FadeInRight(btnVisualization).play();
                indicator_pane.setVisible(true);
                new FadeInLeft(indicator_pane).play();

                statusbar_errorLog.setVisible(true);
                statusbar_errorLog.setText(ErrorLog.getInstance().get().size()+" error(s) found");
                statusbar_errorLog.setOnMouseClicked(event -> {
                    new ErrorLogController();
                });

                export.setVisible(true);
                export.setText("Export...");
                export.setOnMouseClicked(event -> {
                    try {
                        exportToExcel();
                        export.setText("Export complete");
                    } catch (IOException ex) {
                        Logger.getLogger(MaintainabilityIndexResultController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });


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

        for (Map.Entry<Integer, List<String>> property : methodProperty.get().entrySet()) {
            int key = property.getKey();
            //System.out.println("ID         : " + key);
            //System.out.println("Class Name : " + property.getValue().get(0));

            //System.out.println("Method Name: " + property.getValue().get(1));
            //System.out.println("MI Value   : " + maintainabilityIndexResult.get().get(key));

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
        id_column.setVisible(true);
        name_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<MaintainabilityIndexProperty, String> param) -> param.getValue().getValue().nameProperty());
        maintainabiliiti_index_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<MaintainabilityIndexProperty, String> param) -> param.getValue().getValue().maintainability_indexProperty());
        status_column.setCellValueFactory((TreeTableColumn.CellDataFeatures<MaintainabilityIndexProperty, String> param) -> param.getValue().getValue().statusProperty());

        maintainabiliiti_index_column.setStyle("-fx-alignment: CENTER-RIGHT;");
        status_column.setStyle("-fx-alignment: CENTER;");

        root.setExpanded(true);
        MI_TreeTableView.setRoot(root);
        MI_TreeTableView.setShowRoot(false);
        totolRow = root.getChildren().size()+ methodProperty.get().size();

        input_filter.textProperty().addListener((observable, oldValue, newValue) -> filterChanged(newValue));

    }

    private void filterChanged(String filter) {
        if (filter.isEmpty()) {
            MI_TreeTableView.setRoot(root);
        }
        else {
            TreeItem<MaintainabilityIndexProperty> filteredRoot = new TreeItem<>();
            filter(root, filter, filteredRoot);
            MI_TreeTableView.setRoot(filteredRoot);
        }
    }


    private void filter(TreeItem<MaintainabilityIndexProperty> root, String filter, TreeItem<MaintainabilityIndexProperty> filteredRoot) {
        for (TreeItem<MaintainabilityIndexProperty> child : root.getChildren()) {
            TreeItem<MaintainabilityIndexProperty> filteredChild = new TreeItem<>();
            filteredChild.setValue(child.getValue());
            filteredChild.setExpanded(true);
            filter(child, filter, filteredChild );
            if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
                filteredRoot.getChildren().add(filteredChild);
            }
        }
    }

    private boolean isMatch(MaintainabilityIndexProperty value, String filter) {
        return value.getName().toLowerCase().contains(filter.toLowerCase()) ||
                value.getMaintainability_index().contains(filter.toLowerCase());
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

                OperandAndOperator.getInstance().getlistMethodOperator(methodKey).entrySet().forEach(op ->{
                    System.out.println(op.getKey() +" = "+ op.getValue());
                });
            }
            detailsController.showStage();
        }
    }

    public void calculateAVG(){
        AvgMaintainabilityIndexCalculations aa = new AvgMaintainabilityIndexCalculations();
        aa.calculate();

        HalsteadMetricsCalculation.getInstance().calculateAvg();
        CyclomaticComplexityCalculations.getInstance().calculateAvg();
    }

    private void exportToExcel() throws IOException{
        Workbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("sample");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < MI_TreeTableView.getColumns().size(); j++) {
            row.createCell(j).setCellValue(MI_TreeTableView.getColumns().get(j).getText());
        }

        for (int i = 0; i < totolRow; i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < MI_TreeTableView.getColumns().size(); j++) {
                if(MI_TreeTableView.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(MI_TreeTableView.getColumns().get(j).getCellData(i).toString());
                }
                else {
                    row.createCell(j).setCellValue("");
                }
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream("workbook.xls")) {
            workbook.write(fileOut);
        }
    }
}
