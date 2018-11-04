package app.main;

import app.models.*;
import com.fxgraph.cells.ClassDiagramCell;
import com.fxgraph.graph.ICell;
import com.fxgraph.graph.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @FXML
    private ProgressIndicator statusbar_indicator;

    @FXML
    private FontAwesomeIconView statusbar_complete;

    @FXML
    private Label statusbar_scanning;

    @FXML
    private HBox pane_progress;

    @FXML
    private Label statusbar_fileFound;


    private final Stage visualizationStage;

    FilePath filePath;
    long start;
    Graph graph;
    int totalsNode;

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

            graph = new Graph();
            graph_area.setCenter(graph.getCanvas());

            //add();
            ExecutorService service = Executors.newFixedThreadPool(1);

            addGraphComponents.setOnRunning(task -> {
                pane_progress.setVisible(true);
                statusbar_complete.setVisible(false);
                statusbar_indicator.setVisible(true);
                statusbar_scanning.setVisible(true);
                this.visualizationStage.show();
            });

            addGraphComponents.setOnSucceeded(task -> {
                graph.endUpdate();
                graph.layout(new AbegoTreeLayout());

                statusbar_indicator.setVisible(false);
                statusbar_complete.setVisible(true);
                long time = (System.currentTimeMillis() - start);
                statusbar_executionTime.setText("Time to execution: " + time + "ms");
                statusbar_fileFound.setText("There are " +totalsNode+" nodes");
            });

            addGraphComponents.setOnFailed(task-> {
                service.shutdownNow();
                service.submit(addGraphComponents);
            });

            statusbar_scanning.textProperty().bind(addGraphComponents.messageProperty());
            service.submit(addGraphComponents);
            service.shutdown();

            statusbar_directoryPath.setText(filePath.getRootDorectory());

            this.visualizationStage.setScene(scene);
            this.visualizationStage.setTitle("Visualizations");
            this.visualizationStage.initModality(Modality.APPLICATION_MODAL);
            this.visualizationStage.initOwner(null);


            this.visualizationStage.setOnCloseRequest(event -> {
                service.shutdownNow();
            });

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

    Task addGraphComponents = new Task<Void>() {
        @Override public Void call() {

            Map<String, ICell> cell = new HashMap<>();

            final Model model = graph.getModel();
            graph.beginUpdate();

            ClassProperty classProperty = ClassProperty.getInstance();
            classProperty.get().entrySet().forEach(classData-> {
                final String className = classData.getValue().get(0);
                updateMessage("add graph: "+className);
                final String classType = classData.getValue().get(4);
                final ICell classUML = new ClassDiagramCell(className, classType, label_MI_value, label_status_MI);
                cell.put(className, classUML);
                model.addCell(classUML);
            });
            totalsNode = cell.size();

            ClassEdgeProperty classEdgeProperty = ClassEdgeProperty.getInstance();
            classEdgeProperty.debug();

            try{
                classEdgeProperty.get().entrySet().forEach(edge -> {
                    final String source = edge.getValue().get(0);
                    final String target = edge.getValue().get(1);
                    updateMessage("add edge: "+source + " -> " + target);
                    if (cell.get(source) == null || cell.get(target) == null){
                        System.out.println("source "+cell.get(source));
                        System.out.println("target "+target);
                    } else {
                        ICell sourceCell = cell.get(source);
                        ICell targetCell = cell.get(target);
                        model.addEdge(sourceCell, targetCell);
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            updateMessage("Done!");
        }

        @Override
        protected void failed() {
            super.failed();
            updateMessage("Task failed");
            updateMessage("Retasking...");
        }
    };
}
