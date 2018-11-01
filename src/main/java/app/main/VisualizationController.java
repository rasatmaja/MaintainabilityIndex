package app.main;

import app.models.*;
import com.fxgraph.cells.ClassDiagramCell;
import com.fxgraph.graph.ICell;
import com.fxgraph.graph.Model;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import com.fxgraph.layout.AbegoTreeLayout;
import com.fxgraph.graph.Graph;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class VisualizationController implements Initializable {

    @FXML
    private BorderPane graph_area;

    @FXML
    private Label label_MI_value;

    @FXML
    private Label label_status_MI;

    @FXML
    private VBox indicator_pane;

    @FXML
    private Label statusbar_directoryPath;

    @FXML
    private Label statusbar_executionTime;

    private final Stage visualizationStage;

    FilePath filePath;
    long start;
    Graph graph;

    public VisualizationController(){
        this.filePath = FilePath.getInstance();
        start = System.currentTimeMillis();

        this.visualizationStage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Visualization.fxml"));
            loader.setController(this);
            root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");


            addGraphComponents();


            this.visualizationStage.setScene(scene);
            this.visualizationStage.setTitle("Visualizations");
            this.visualizationStage.initModality(Modality.APPLICATION_MODAL);
            this.visualizationStage.initOwner(null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void showStage() {
        this.visualizationStage.show();
        statusbar_directoryPath.setText(filePath.getRootDorectory());

        long time = (System.currentTimeMillis() - start);
        statusbar_executionTime.setText("Time to execution: " + time + "ms");
    }

    private void addGraphComponents() {
        Map<String, ICell> cell = new HashMap<>();

        graph = new Graph();
        graph_area.setCenter(graph.getCanvas());

        final Model model = graph.getModel();
        graph.beginUpdate();

        ClassProperty classProperty = ClassProperty.getInstance();
        classProperty.get().entrySet().forEach(classData-> {
            final String className = classData.getValue().get(0);
            final String classType = classData.getValue().get(4);
            final ICell classUML = new ClassDiagramCell(className, classType, label_MI_value, label_status_MI);
            cell.put(className, classUML);
            model.addCell(classUML);
        });

        //model.addEdge(cellC, cellK);

        graph.endUpdate();

        graph.layout(new AbegoTreeLayout());
    }
}
