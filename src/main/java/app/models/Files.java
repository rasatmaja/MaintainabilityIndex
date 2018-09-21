/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.models;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author rasio
 */
public class Files {
    private final SimpleStringProperty file_name;
    private final SimpleStringProperty size;
    private final SimpleStringProperty date_modified;

    public Files(String file_name, String size, String date_modified) {
        super();
        this.file_name = new SimpleStringProperty(file_name);
        this.size = new SimpleStringProperty(size);
        this.date_modified = new SimpleStringProperty(date_modified);
    }

    public String getFile_name() {
        return file_name.get();
    }

    public String getSize() {
        return size.get();
    }

    public String getDate_modified() {
        return date_modified.get();
    }

}
