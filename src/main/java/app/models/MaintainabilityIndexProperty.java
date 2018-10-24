package app.models;

import javafx.beans.property.SimpleStringProperty;

import java.text.DecimalFormat;

public class MaintainabilityIndexProperty {
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty maintainability_index;
    private final SimpleStringProperty status;

    DecimalFormat numberFormat = new DecimalFormat("0.##");

    public MaintainabilityIndexProperty(String idmethod, String name, double value){
        super();
        this.id = new SimpleStringProperty(idmethod);
        this.name = new SimpleStringProperty(name);
        this.maintainability_index = new SimpleStringProperty(numberFormat.format(value));
        if (value > 85){
            this.status = new SimpleStringProperty("Highly maintainable");
        } else if (value <= 85 && value >65){
            this.status = new SimpleStringProperty("Moderately maintainable");
        } else {
            this.status = new SimpleStringProperty("Difﬁcult to maintain");
        }
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getMaintainability_index() {
        return maintainability_index.get();
    }

    public SimpleStringProperty maintainability_indexProperty() {
        return maintainability_index;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
