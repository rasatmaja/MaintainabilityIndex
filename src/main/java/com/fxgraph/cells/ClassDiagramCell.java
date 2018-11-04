/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxgraph.cells;

import app.main.DetailsController;
import app.models.MaintainabilityIndexResult;
import app.models.MethodProperty;
import com.fxgraph.graph.Graph;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;

/**
 *
 * @author rasio
 */
public class ClassDiagramCell extends AbstractCell {

    String classNameProperty;
    String classTypeProperty;
    Label label_MI_value;
    Label label_status_MI;
    DecimalFormat numberFormat = new DecimalFormat("0.##");

    private final Image ICON_HIGH = new Image(getClass().getResourceAsStream("/img/high.png"), 13, 13, false, true);
    private final Image ICON_MODERATE = new Image(getClass().getResourceAsStream("/img/moderate.png"), 13, 13, false, true);
    private final Image ICON_LOW = new Image(getClass().getResourceAsStream("/img/low.png"), 13, 13, false, true);

    public ClassDiagramCell(String className, String classType, Label label_MI_value, Label label_status_MI) {
        this.classNameProperty = className;
        this.classTypeProperty = "<< "+classType+ " Class >>";
        this.label_MI_value = label_MI_value;
        this.label_status_MI = label_status_MI;
    }

    @Override
    public Region getGraphic(Graph graph) {

        final VBox container = new VBox();
        container.getStyleClass().add("containerUML");
        container.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);

        final VBox className = new VBox();
        className.getStyleClass().add("containerClass");
        className.setAlignment(Pos.CENTER);
        className.setPrefSize(Control.USE_COMPUTED_SIZE, 35);

        final VBox method = new VBox();
        method.getStyleClass().add("containerMethod");
        method.setAlignment(Pos.CENTER_LEFT);
        method.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);

        MethodProperty methodProperty = MethodProperty.getInstance();
        MaintainabilityIndexResult maintainabilityIndexResult =  MaintainabilityIndexResult.getInstance();


        final Label lbl_classType = new Label();
        lbl_classType.setText(this.classTypeProperty);
        lbl_classType.getStyleClass().add("classType");

        final Label lbl_className = new Label();
        lbl_className.setText(this.classNameProperty);
        lbl_className.getStyleClass().add("className");

        className.getChildren().addAll(lbl_classType, lbl_className);

        methodProperty.get().entrySet().forEach(methodData -> {
            if(methodData.getValue().get(0).equalsIgnoreCase(this.classNameProperty)){
                int methodKey = methodData.getKey();

                final Label lbl_methodName = new Label();
                lbl_methodName.setText(methodData.getValue().get(6) + ": " + methodData.getValue().get(7));
                lbl_methodName.setStyle("-fx-padding: 3 5 3 5");

                final double miValue = maintainabilityIndexResult.get().get(methodKey);
                if (miValue > 85){
                    lbl_methodName.setGraphic(new ImageView(this.ICON_HIGH));
                } else if (miValue <= 85 && miValue >65){
                    lbl_methodName.setGraphic(new ImageView(this.ICON_MODERATE));
                } else {
                    lbl_methodName.setGraphic(new ImageView(this.ICON_LOW));
                }
                lbl_methodName.setGraphicTextGap(4);

                lbl_methodName.setOnMouseClicked((MouseEvent event) -> {
                    if (event.getClickCount() == 2){
                        DetailsController detailsController = new DetailsController();
                        detailsController.setMethodKey(methodKey);
                        detailsController.showStage();
                    }
                });

                lbl_methodName.setOnMouseEntered((MouseEvent event) -> {
                    label_MI_value.setText(numberFormat.format(miValue));
                    if (miValue > 85){
                        label_status_MI.setText("Highly Maintainable");
                        label_status_MI.setTextFill(Color.web("#16C48D"));
                        label_MI_value.setTextFill(Color.web("#16C48D"));
                    } else if (miValue <= 85 && miValue >65){
                        label_status_MI.setText("Moderately Maintainable");
                        label_status_MI.setTextFill(Color.web("#F3923D"));
                        label_MI_value.setTextFill(Color.web("#F3923D"));
                    } else {
                        label_status_MI.setText("Difﬁcult to Maintain");
                        label_status_MI.setTextFill(Color.web("#DA3B29"));
                        label_MI_value.setTextFill(Color.web("#DA3B29"));
                    }
                });
                method.getChildren().add(lbl_methodName);
            }
        });

        container.getChildren().addAll(className, method);
        final Pane pane = new Pane(container);
        pane.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);

        System.out.println(pane.getWidth());

        return pane;
    }
}
