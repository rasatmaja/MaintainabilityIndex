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
    private String className;

    MaintainabilityIndexResult maintainabilityIndexResult;
    HalsteadMetricsResult halsteadMetricsResult;
    CyclomaticComplexityResult cyclomaticComplexityResult;
    ClassProperty classProperty;
    MethodProperty methodProperty;
    OperandAndOperator operandAndOperator;

    DecimalFormat numberFormat = new DecimalFormat("0.##");

    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private String code;

    private ExecutorService executor;


    public DetailsController(){
        operandAndOperator = OperandAndOperator.getInstance();
        classProperty = ClassProperty.getInstance();
        methodProperty = MethodProperty.getInstance();
        halsteadMetricsResult = HalsteadMetricsResult.getInstance();
        cyclomaticComplexityResult = CyclomaticComplexityResult.getInstance();
        maintainabilityIndexResult = MaintainabilityIndexResult.getInstance();

        this.detailStage = new Stage();
        Parent root;
        try {
            //root = FXMLLoader.load(getClass().getResource("/fxml/Details.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Details.fxml"));
            loader.setController(this);
            root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            this.detailStage.setScene(scene);
            this.detailStage.setTitle("Details Maintainability Index Calculatios");
            this.detailStage.initModality(Modality.APPLICATION_MODAL);
            this.detailStage.initOwner(null);
            this.detailStage.setOnCloseRequest(e -> {
                stop();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        System.out.println(methodKey);
        this.detailStage.show();
        populateMethodData();
        codeArea();
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
        label_LOC.setText(methodProperty.get().get(methodKey).get(2));

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

    public void codeArea(){
        executor = Executors.newSingleThreadExecutor();

        code_area.setParagraphGraphicFactory(LineNumberFactory.get(code_area));
        Subscription cleanupWhenDone = code_area.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(code_area.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

        // call when no longer need it: `cleanupWhenFinished.unsubscribe();`

        code_area.replaceText(0, 0, this.code);

    }


    public void stop() {
        executor.shutdown();
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = code_area.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        code_area.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    
}
