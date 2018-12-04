package app.controllers;

import app.models.ErrorLog;
import app.models.FilePath;
import app.models.MethodProperty;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.javaparser.JavaParser.parse;
import static com.github.javaparser.JavaParser.parseJavadoc;

public class ASTExtractions extends Task<Void> {
    private  String FILE_PATH;
    CompilationUnit cu;
    FilePath filePath;
    MethodProperty methodProperty;

    public ASTExtractions () {
        filePath = FilePath.getInstance();
        methodProperty = MethodProperty.getInstance();
    }

    @Override
    protected Void call() throws Exception {
        updateMessage("Start extracting... ");
        classAndMethodExtraction();
        operandAndOperatorExtraction();
        updateMessage("Extraction is complete ");
        return null;
    }

    private void classAndMethodExtraction () throws FileNotFoundException {

        filePath.get().entrySet().forEach((entry) -> {
            int key = entry.getKey();
            List<String> values = entry.getValue();
            updateMessage("Extracting file: " + values.get(0));

            FILE_PATH = values.get(1);
            try {
                cu = parse(new File(FILE_PATH));
                ClassAndMethodPropertyExtraction cae = new ClassAndMethodPropertyExtraction();
                cae.visit(cu, null);

            } catch (Exception e) {
                //e.printStackTrace();
                //System.out.print((char)27 + "[31m" + "[ERROR] : ");
                //System.out.println(FILE_PATH + " : The identifier doesn't represent a java file");
                //System.out.println((char)27 + "[30m");
                //System.out.println();
                System.out.println(e.getMessage());
                ErrorLog.getInstance().set(new ArrayList<>(Arrays.asList(
                        values.get(0),
                        values.get(1),
                        "The identifier doesn't represent a java file"
                )));
            }
        });
    }

    private void operandAndOperatorExtraction(){
        methodProperty.get().entrySet().forEach(dataMethodProperty ->{
            int dataMethodkey = dataMethodProperty.getKey();
            String className = dataMethodProperty.getValue().get(0);

            //System.out.println("Key         : " + dataMethodkey);
            System.out.println("Class name  : " + dataMethodProperty.getValue().get(0));
            System.out.println("Method name : " + dataMethodProperty.getValue().get(1));
            //System.out.println("Body Method : \n" + dataMethodProperty.getValue().get(5));
            updateMessage("Extracting method: " + dataMethodProperty.getValue().get(6)+"()");

            try {
                //String sourceCode = dataMethodProperty.getValue().get(5)
                       // .replaceAll("super\\(.*?\\);", "")
                        //.replaceAll("this\\(.*?\\);", "");
               // BlockStmt bodyMethod = JavaParser.parseBlock(sourceCode);
                if (dataMethodProperty.getValue().get(7).equalsIgnoreCase("constructor")){
                    String sourceCode = dataMethodProperty.getValue().get(5);
                    CompilationUnit bodyMethod = JavaParser.parse(sourceCode);
                    OperandAndOperatorExtraction operandAndOperatorExtraction = new OperandAndOperatorExtraction(className, dataMethodkey);
                    operandAndOperatorExtraction.visit(bodyMethod, null);
                    operandAndOperatorExtraction.save();
                } else if (dataMethodProperty.getValue().get(7).equalsIgnoreCase("method")){
                    String sourceCode = dataMethodProperty.getValue().get(5);
                    BlockStmt bodyMethod = JavaParser.parseBlock(sourceCode);
                    OperandAndOperatorExtraction operandAndOperatorExtraction = new OperandAndOperatorExtraction(className, dataMethodkey);
                    operandAndOperatorExtraction.visit(bodyMethod, null);
                    operandAndOperatorExtraction.save();
                }

                //OperandAndOperatorExtraction operandAndOperatorExtraction = new OperandAndOperatorExtraction(className, dataMethodkey);
                //operandAndOperatorExtraction.visit(bodyMethod, null);
                //operandAndOperatorExtraction.save();

                System.out.println("------------------------------------------------------------------------------");
            } catch (Exception e){
                e.printStackTrace();
            }
        });

    }


}
