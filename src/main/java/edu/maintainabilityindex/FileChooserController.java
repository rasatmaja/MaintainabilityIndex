package edu.maintainabilityindex;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;


/**
 * FXML Controller class
 *
 * @author rasio
 */
public class FileChooserController implements Initializable {

    @FXML
    private Label status;
    @FXML
    private JFXListView<Label> listFile;
    @FXML
    private Label totalFile;

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
            status.setText("No Directory selected");
        } else {
            status.setText(selectedDirectory.getAbsolutePath());
            walk(selectedDirectory.getAbsolutePath());
        }
    }
    
    public void walk( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;
               
        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );
                System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {
                System.out.println( "File:" + f.getAbsoluteFile() );
                Label file = new Label(f.getName());
                listFile.getItems().add(file);
            }
        }
        
        totalFile.setText(listFile.getItems().size()+" Java file ");
    }

}
