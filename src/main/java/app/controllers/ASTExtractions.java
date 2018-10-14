package app.controllers;

import app.models.FilePath;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ASTExtractions extends Task<Void> {
    private  String FILE_PATH;
    CompilationUnit cu;
    FilePath filePath;

    public ASTExtractions () {
        filePath = FilePath.getInstance();
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
            updateMessage("Extracting : " + values.get(0));

            FILE_PATH = values.get(1);
            try {
                cu = JavaParser.parse(new File(FILE_PATH));
                ClassAndMethodPropertyExtraction cae = new ClassAndMethodPropertyExtraction();
                cae.visit(cu, null);

            } catch (Exception e) {
                //e.printStackTrace();
                System.out.print((char)27 + "[31m" + "[ERROR] : ");
                System.out.println(FILE_PATH + " : The identifier doesn't represent a java file");
                System.out.println();
            }

        });
    }

    private void operandAndOperatorExtraction(){

    }


}
