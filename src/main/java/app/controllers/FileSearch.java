/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

/**
 *
 * @author rasio
 */
public class FileSearch extends Task<Long> {
    private String path;

    public FileSearch(String path){
        this.path = path;
    }
    
    @Override
    protected Long call() throws Exception {
        fileWalker(path);
        return null;
        
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
                fileWalker(f.getAbsolutePath());
                System.out.println("Dir:" + f.getAbsoluteFile());
                
            } else {
                System.out.println("File:" + f.getAbsoluteFile());
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                System.out.println(sdf.format(f.lastModified()));
                System.out.println("Size: "+(f.length())+" byte");
                Label file = new Label(f.getName());
                //listFile.getItems().add(file);
            }
        }
      
    }
    
}
