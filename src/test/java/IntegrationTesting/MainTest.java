/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegrationTesting;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.stmt.BlockStmt;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rasio
 */
public class MainTest {

    private String sourceCode;
    private OperandAndOperatorExtraction operandAndOperatorExtraction;
    private StubInsertIntoHashMaps stubInsertIntoHashMaps;
    private ArrayList<String> expectedResult = new ArrayList<>();
    @Before
    public void setUp() {
        sourceCode = "{\n"
                + "	int a;\n"
                + "	int b;\n"
                + "	a = 9;\n"
                + "   }";
        expectedResult.add("Operand : int");
        expectedResult.add("Operand : a");
        expectedResult.add("Operand : int");
        expectedResult.add("Operand : b");
        expectedResult.add("Operand : a");
        expectedResult.add("Operator : =");
        
        operandAndOperatorExtraction = new OperandAndOperatorExtraction();
        stubInsertIntoHashMaps = StubInsertIntoHashMaps.getInstance();
    }

    @Test
    public void testIntegrationBetwenMethodVisitAndStubInserIntoHashmaps() {

        BlockStmt bodyMethod = JavaParser.parseBlock(sourceCode);
        operandAndOperatorExtraction.visit(bodyMethod, null);
        
        stubInsertIntoHashMaps.getList().forEach(node -> {System.out.println(node);});

        assertEquals(expectedResult.get(0), stubInsertIntoHashMaps.getList().get(0));
        assertEquals(expectedResult.get(1), stubInsertIntoHashMaps.getList().get(1));
        assertEquals(expectedResult.get(2), stubInsertIntoHashMaps.getList().get(2));
        assertEquals(expectedResult.get(3), stubInsertIntoHashMaps.getList().get(3));
        assertEquals(expectedResult.get(4), stubInsertIntoHashMaps.getList().get(4));
        assertEquals(expectedResult.get(5), stubInsertIntoHashMaps.getList().get(5));

    }

}
