/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.main;

import app.controllers.MaintainaibilityIndexCalculation;
import app.models.FilePath;
import app.models.MaintainabilityIndexProperty;
import app.models.MaintainabilityIndexResult;
import app.models.MethodProperty;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author rasio
 */
public class MaintainabilityIndexResultController implements Initializable {

    public TreeTableView<MaintainabilityIndexProperty> MI_TreeTableView;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
                populateTreeTable();

                statusbar_indicator.setVisible(false);
                statusbar_complete.setVisible(true);

                long time = (System.currentTimeMillis() - start);
                statusbar_executionTime.setText("Time to calculations: " + time + "ms");

                statusbar_fileFound.setText(methodProperty.get().size()+" method has been calculated");
            });

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(maintainaibilityIndexCalculation);
            executorService.shutdown();

        }catch (Exception e) {

        }
    }    

    @FXML
    private void close(ActionEvent event) {
        MaintainabilityIndexResultUI.getPrimaryStage().close();
    }

    @FXML
    public void Visualization(ActionEvent actionEvent) {
        //OperandAndOperator operandAndOperator = OperandAndOperator.getInstance();
        //operandAndOperator.debug();


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

            Image icon;

            if (miValue > 85){
                icon = new Image(getClass().getResourceAsStream("/img/high.png"), 10, 10, false, true);
            } else if (miValue <= 85 && miValue >65){
                icon = new Image(getClass().getResourceAsStream("/img/moderate.png"), 10, 10, false, true);
            } else {
                icon = new Image(getClass().getResourceAsStream("/img/low.png"), 10, 10, false, true);
            }

            if (tempClassName != classNameProperty) {
                tempClassName = classNameProperty;
                className = new TreeItem<>(new MaintainabilityIndexProperty("", property.getValue().get(0), 0.0), new ImageView(icon));
                TreeItem<MaintainabilityIndexProperty> method = new TreeItem<>(new MaintainabilityIndexProperty(property.getKey().toString(), methodNameProperty, miValue), new ImageView(icon));

                className.getChildren().add(method);
                className.setExpanded(true);
                root.getChildren().add(className);
            } else{
                TreeItem<MaintainabilityIndexProperty> method = new TreeItem<>(new MaintainabilityIndexProperty(property.getKey().toString(), methodNameProperty, miValue), new ImageView(icon));
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
            System.out.println(MI_TreeTableView.getSelectionModel().getSelectedItem().valueProperty().getValue().getName());
            System.out.println(MI_TreeTableView.getSelectionModel().getSelectedItem().valueProperty().getValue().getStatus());
            System.out.println(MI_TreeTableView.getSelectionModel().getSelectedItem().valueProperty().getValue().getId());
        }
    }
}
