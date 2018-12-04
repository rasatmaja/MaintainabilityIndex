/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegrationsTesting;

import IntegrationTesting.OperandAndOperatorExtraction;
import IntegrationTesting.StubInsertIntoHashMaps;
import app.controllers.ClassAndMethodPropertyExtraction;
import app.models.ClassProperty;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author rasio
 */
public class MainTest {
    private String sourceCode;
    private ArrayList<String> expectedResult = new ArrayList<>();
    private ClassAndMethodPropertyExtraction cmpe;
    private ClassProperty cp;

    @Before
    public void setUp() {
        sourceCode = "abstract class Shape{\n" +
                     "    abstract void draw();  \n" +
                     "} ";
        expectedResult.add("Shape");
        expectedResult.add("1");
        expectedResult.add("0");
        expectedResult.add(sourceCode);
        expectedResult.add("Abstract");

        cmpe = new ClassAndMethodPropertyExtraction();
        cp = ClassProperty.getInstance();
    }

    /*
    * Pada pengujian ini akan memastikan bahwa pada method visit pada class ClassAndMethodPropertExtractions
    * dapat berinterksi dengan method set yang ada pada class ClassProperty
    * */
    @Test
    public void intergrtion_testing_between_method_visit_and_set() {
        CompilationUnit cu = JavaParser.parse(sourceCode);
        cmpe.visit(cu, null);
        cp.get().entrySet().forEach(classProperty -> {
            System.out.println("Nama Class     [0] = " + classProperty.getValue().get(0));
            System.out.println("Jumlah LOC     [1] = " + classProperty.getValue().get(1));
            System.out.println("Jumlah Comment [2] = " + classProperty.getValue().get(2));
            //System.out.println("[3] = " + classProperty.getValue().get(3));
            System.out.println("Tipe Class     [4] = " + classProperty.getValue().get(4));
        });
        cp.get().entrySet().forEach(classProperty -> {
            assertEquals(expectedResult.get(0), classProperty.getValue().get(0));
            assertEquals(expectedResult.get(1), classProperty.getValue().get(1));
            assertEquals(expectedResult.get(2), classProperty.getValue().get(2));
            //assertEquals(expectedResult.get(3), classProperty.getValue().get(3));
            assertEquals(expectedResult.get(4), classProperty.getValue().get(4));

        });

    }

}
