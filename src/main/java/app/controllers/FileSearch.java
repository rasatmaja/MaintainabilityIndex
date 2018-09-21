/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.models.Files;
import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

/**
 *
 * @author rasio
 */
public class FileSearch extends Task<ObservableList<Files>> {
    private String path;
    public ObservableList<Files> list_file = FXCollections.observableArrayList();

    public FileSearch(String path){
        this.path = path;
    }
    
    @Override
    protected ObservableList<Files> call() throws Exception {
        updateMessage("Start scanning... ");
        fileWalker(path);
        updateMessage("Scan is complete ");
        return this.list_file;
        
    }
    
    public void fileWalker(String path) {
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
                System.out.println("Dir:" + f.getAbsoluteFile());
                updateMessage("Scanning: "+ f.getName());
                fileWalker(f.getAbsolutePath());
                
            } else {
                System.out.println("File:" + f.getAbsoluteFile());
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                list_file.add( new Files (f.getName(), f.length()+" byte", sdf.format(f.lastModified())));
            }
        }
      
    }
    
}
