package edu.maintainabilityindex;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInLeft;
import animatefx.animation.FadeInLeftBig;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeInRightBig;
import animatefx.animation.FadeOutLeft;
import animatefx.animation.Swing;
import animatefx.animation.ZoomInRight;
import animatefx.animation.ZoomOutLeft;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private JFXListView<Label> listFile;
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
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void openDirectoryChooser(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {
            statusbar_directoryPath.setText("No Directory selected");
        } else {
            listFile.getItems().clear();
                        
            long start = System.currentTimeMillis();
            fileSearch(selectedDirectory.getAbsolutePath());
            long time = (System.currentTimeMillis() - start);
            
            statusbar_directoryPath.setText(selectedDirectory.getAbsolutePath());
            statusbar_fileFound.setText(listFile.getItems().size() + " Java file ");
            statusbar_executionTime.setText("Time to execution: "+time+"ms");
           
            pane_listFile.toFront();
            new FadeInRightBig(pane_listFile).play();
            
            //progressbar_scanning.setVisible(false);
        }
    }

    public void fileSearch(String path) {
        File root = new File(path);
        File[] list = root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".java") || file.isDirectory();
            }
        });

        if (list == null) {
            return;
        }

        for (File f : list) {
            
            if (f.isDirectory()) {
                fileSearch(f.getAbsolutePath());
                System.out.println("Dir:" + f.getAbsoluteFile());
                
            } else {
                System.out.println("File:" + f.getAbsoluteFile());
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                System.out.println(sdf.format(f.lastModified()));
                System.out.println("Size: "+(f.length())+" byte");
                Label file = new Label(f.getName());
                listFile.getItems().add(file);
            }
        }

        
    }

    @FXML
    private void back(ActionEvent event) {
        pane1.toFront();
        new FadeInLeftBig(pane1).play();
        
        statusbar_directoryPath.setText("No directory open");
        statusbar_fileFound.setText("No java file found");
        statusbar_executionTime.setText("There is no execution yet");
    }

    @FXML
    private void calculate(ActionEvent event) {
    }

}
