/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.main;

import app.models.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

/**
 * FXML Controller class
 *
 * @author rasio
 */
public class DetailsController implements Initializable {

    @FXML
    private Label statusbar_directoryPath;
    @FXML
    private Label statusbar_fileFound;
    @FXML
    private Label statusbar_executionTime;
    @FXML
    private Label label_status_MI;
    @FXML
    private HBox pane_progress;
    @FXML
    private ProgressIndicator statusbar_indicator;
    @FXML
    private FontAwesomeIconView statusbar_complete;
    @FXML
    private Label statusbar_scanning;
    @FXML
    private Label label_MI_value;
    @FXML
    private Label label_HL;
    @FXML
    private Label label_HVc;
    @FXML
    private Label label_HV;
    @FXML
    private Label label_HD;
    @FXML
    private Label label_HE;
    @FXML
    private Label label_HB;
    @FXML
    private Label label_CC;
    @FXML
    private Label label_LOC;
    @FXML
    private Label label_operand;
    @FXML
    private Label label_operator;
    @FXML
    private Label label_name;
    @FXML
    private AnchorPane panel_code_area;
    @FXML
    private CodeArea code_area;

    private final Stage detailStage;
    private int methodKey;
    private String className = "";
    private String code;

    MaintainabilityIndexResult maintainabilityIndexResult;
    HalsteadMetricsResult halsteadMetricsResult;
    CyclomaticComplexityResult cyclomaticComplexityResult;
    ClassProperty classProperty;
    MethodProperty methodProperty;
    OperandAndOperator operandAndOperator;
    CodeAreaController codeAreaController;
    FilePath filePath;
    long start;
    DecimalFormat numberFormat = new DecimalFormat("0.##");

    public DetailsController(){
        operandAndOperator = OperandAndOperator.getInstance();
        classProperty = ClassProperty.getInstance();
        methodProperty = MethodProperty.getInstance();
        halsteadMetricsResult = HalsteadMetricsResult.getInstance();
        cyclomaticComplexityResult = CyclomaticComplexityResult.getInstance();
        maintainabilityIndexResult = MaintainabilityIndexResult.getInstance();
        this.filePath = FilePath.getInstance();
        start = System.currentTimeMillis();

        this.detailStage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Details.fxml"));
            loader.setController(this);
            root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            this.detailStage.setScene(scene);
            this.detailStage.setTitle("Details Maintainability Index Calculatios");
            this.detailStage.initModality(Modality.APPLICATION_MODAL);
            this.detailStage.initOwner(null);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        System.out.println(methodKey);
        this.detailStage.show();
        statusbar_directoryPath.setText(filePath.getRootDorectory());
        if(!this.className.equalsIgnoreCase("")){
            populateClassData();
        }else{
            populateMethodData();
        }

        codeAreaController = new CodeAreaController(code_area, this.code);
        this.detailStage.setOnCloseRequest(e -> {
            codeAreaController.stop();
        });

        long time = (System.currentTimeMillis() - start);
        statusbar_executionTime.setText("Time to execution: " + time + "ms");
    }

    public void setMethodKey(int key){
        this.methodKey = key;
    }

    public void setClassName(String className){
        this.className = className;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void populateMethodData(){
        label_name.setText(methodProperty.get().get(methodKey).get(1));

        double miValue = maintainabilityIndexResult.get().get(methodKey);
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

        label_CC.setText(cyclomaticComplexityResult.get().get(methodKey).toString());
        int loc = Integer.valueOf(methodProperty.get().get(methodKey).get(2));
        double comment = Double.valueOf(methodProperty.get().get(methodKey).get(3));
        double perCM = 100 * comment / loc;
        //System.out.println("perCM: " + methodProperty.get().get(methodKey).get(3));
        label_LOC.setText(loc+"/ "+numberFormat.format(perCM));

        int distinctOperand = operandAndOperator.getDistinctOperand(methodKey);
        int distinctOperator = operandAndOperator.getDistinctOperator(methodKey);
        int totlaOperand = operandAndOperator.getTotalOperand(methodKey);
        int totlaOperator = operandAndOperator.getTotalOperator(methodKey);

        label_operand.setText(distinctOperand + "/ " + totlaOperand);
        label_operator.setText(distinctOperator + "/ " + totlaOperator);

        label_HL.setText(numberFormat.format(halsteadMetricsResult.get().get(methodKey).get(0)));
        label_HVc.setText(numberFormat.format(halsteadMetricsResult.get().get(methodKey).get(1)));
        label_HV.setText(numberFormat.format(halsteadMetricsResult.get().get(methodKey).get(2)));
        label_HD.setText(numberFormat.format(halsteadMetricsResult.get().get(methodKey).get(3)));
        label_HE.setText(numberFormat.format(halsteadMetricsResult.get().get(methodKey).get(4)));
        label_HB.setText(numberFormat.format(halsteadMetricsResult.get().get(methodKey).get(5)));

        this.code = methodProperty.get().get(methodKey).get(4);
    }

    public void populateClassData(){


        classProperty.get().entrySet().forEach(classData -> {
            if(classData.getValue().get(0).equalsIgnoreCase(this.className)){
                label_LOC.setText(classData.getValue().get(1));
                this.code = classData.getValue().get(3);
                label_name.setText(classData.getValue().get(0));
            }
        });

        double miValue = maintainabilityIndexResult.getListOfAvgMaintainabilityIndex().get(this.className);
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

        label_CC.setText(cyclomaticComplexityResult.getListOfAvgCyclomaticComplexity().get(this.className).toString());

        label_operand.setText("~");
        label_operator.setText("~");

        label_HL.setText(numberFormat.format(halsteadMetricsResult.getListOfAvgHalsteadMetric().get(this.className).get(0)));
        label_HVc.setText(numberFormat.format(halsteadMetricsResult.getListOfAvgHalsteadMetric().get(this.className).get(1)));
        label_HV.setText(numberFormat.format(halsteadMetricsResult.getListOfAvgHalsteadMetric().get(this.className).get(2)));
        label_HD.setText(numberFormat.format(halsteadMetricsResult.getListOfAvgHalsteadMetric().get(this.className).get(3)));
        label_HE.setText(numberFormat.format(halsteadMetricsResult.getListOfAvgHalsteadMetric().get(this.className).get(4)));
        label_HB.setText(numberFormat.format(halsteadMetricsResult.getListOfAvgHalsteadMetric().get(this.className).get(5)));
    }

}
