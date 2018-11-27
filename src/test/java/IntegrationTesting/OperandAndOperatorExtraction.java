package IntegrationTesting;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class OperandAndOperatorExtraction extends VoidVisitorAdapter<Void> {

    Map<String, Integer> listOfOperator = new HashMap<>();
    Map<String, Integer> listOfOperand = new HashMap<>();
    int countPredicaeNode = 0;
    String className;
    StubInsertIntoHashMaps stubInsertIntoHashMaps;

    public OperandAndOperatorExtraction() {
        this.stubInsertIntoHashMaps = StubInsertIntoHashMaps.getInstance();
    }


    /* BLOCK UNTUK MENCARI SEBUAH OPERAND */
    public void visit(PrimitiveType pt, Void arg) {
        super.visit(pt, arg);
        stubInsertIntoHashMaps.stub_insertIntoHashMaps("Operand : " + pt.asString());
    }

    public void visit(VariableDeclarationExpr vde, Void arg) {
        super.visit(vde, arg);
        vde.getVariables().forEach(variable -> {
            stubInsertIntoHashMaps.stub_insertIntoHashMaps("Operand : " + variable.getNameAsString());
            if (variable.toString().equalsIgnoreCase("=")) {
                stubInsertIntoHashMaps.stub_insertIntoHashMaps("Operator : =");
            }
        });
    }

    public void visit(NameExpr ne, Void arg) {
        super.visit(ne, arg);
        stubInsertIntoHashMaps.stub_insertIntoHashMaps("Operand : " + ne.toString());
    }

    /* END BLOCK UNTUK MENCARI SEBUAH OPERAND */
 /* BLOCK UNTUK MENCARI SEBUAH OPERATOR */
    public void visit(AssignExpr ae, Void arg) {
        super.visit(ae, arg);
        stubInsertIntoHashMaps.stub_insertIntoHashMaps("Operator : =");
    }

    public void visit(BinaryExpr be, Void arg) {
        super.visit(be, arg);
        stubInsertIntoHashMaps.stub_insertIntoHashMaps("Operator : "+ be.getOperator().asString());
    }

    public void visit(UnaryExpr ue, Void arg) {
        super.visit(ue, arg);
        stubInsertIntoHashMaps.stub_insertIntoHashMaps("Operator : "+ ue.getOperator().asString());
    }
    /* END BLOCK UNTUK MENCARI SEBUAH OPERATOR */
}
