package app.controllers;

import app.models.OperandAndOperator;
import app.models.PredicateNode;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class OperandAndOperatorExtraction extends VoidVisitorAdapter<Void> {

    Map<String, Integer> listOfOperator = new HashMap<>();
    Map<String, Integer> listOfOperand = new HashMap<>();
    int countPredicaeNode;
    OperandAndOperator operandAndOperator;
    PredicateNode predicateNode;

    public OperandAndOperatorExtraction(int key){
        predicateNode = PredicateNode.getInstance();
        predicateNode.setMethodPropertyKey(key);
        operandAndOperator = OperandAndOperator.getInstance();
        operandAndOperator.setMethodPropertyKey(key);
    }


    public void visit(BlockStmt bs, Void arg){
        super.visit(bs, arg);
        if(bs.isEmpty()){
            System.out.println("There is no body method");
            System.out.println("nilai distinc opeator " + listOfOperator.size());
            System.out.println("nilai distinc operand " + listOfOperand.size());
            countPredicaeNode = -1;
            System.out.println("nilai predicate node " + countPredicaeNode);
        }
    }

    /* BLOCK UNTUK MENCARI SEBUAH OPERAND */
    public void visit(PrimitiveType pt, Void arg) {
        super.visit(pt, arg);
        System.out.println("OPERAND  : " + pt.asString() + " -> ["+pt.getBegin().get()+"]-[PrimitiveType]");
        insertIntoHasMaps("OPERAND", pt.asString());
    }
    public void visit(VariableDeclarationExpr vde, Void arg) {
        super.visit(vde, arg);

        //untuk mengantisipasi ketika ada multiple deklarasi inisialisasi seperti int a=8,b=9;
        vde.getVariables().forEach(variable -> {
            System.out.println("OPERAND  : " + variable.getNameAsString() + " -> ["+vde.getBegin().get()+"]-[VariableDeclarationExpr]");
            insertIntoHasMaps("OPERAND", variable.getNameAsString());
            if(variable.toString().equalsIgnoreCase("=")){
                //menunjukan bahwa ini merupakan deklarasi inisiaalisasi seperti int a = 1;
                //merupakan operator
                System.out.println("OPERATOR : = " + " -> ["+vde.getBegin().get()+"]-[AssignExpr]");
                insertIntoHasMaps("OPERATOR", "=");
            }
        });
    }
    public void visit(NameExpr ne, Void arg) {
        super.visit(ne, arg);
        System.out.println("OPERAND  : " + ne.toString()+ " -> ["+ne.getBegin().get()+"]-[NameExpr]");
        insertIntoHasMaps("OPERAND", ne.toString());
    }
    /* END BLOCK UNTUK MENCARI SEBUAH OPERAND */


    /* BLOCK UNTUK MENCARI SEBUAH OPERATOR */
    public void visit(AssignExpr ae, Void arg) {
        super.visit(ae, arg);
        System.out.println("OPERATOR : = " + " -> ["+ae.getBegin().get()+"]-[AssignExpr]");
        insertIntoHasMaps("OPERATOR", "=");
    }
    public void visit(BinaryExpr be, Void arg) {
        super.visit(be, arg);
        System.out.println("OPERATOR : "+ be.getOperator().asString() + " -> ["+be.getBegin().get()+"]-[BinaryExpr]");
        insertIntoHasMaps("OPERATOR", be.getOperator().asString());
    }
    public void visit(UnaryExpr ue, Void arg) {
        super.visit(ue, arg);
        System.out.println("OPERATOR : "+ ue.getOperator().asString() + " -> ["+ue.getBegin().get()+"]-[UnaryExpr]");
        insertIntoHasMaps("OPERATOR", ue.getOperator().asString());
    }
    /* END BLOCK UNTUK MENCARI SEBUAH OPERATOR */


    /* BLOCK UNTUK MENCARI SEBUAH NILAI */
    public void visit(StringLiteralExpr sle, Void arg) {
        super.visit(sle, arg);
        System.out.println("OPERAND  : "+ sle.asString() + " -> ["+sle.getBegin().get()+"]-[StringLiteralExpr]");
        insertIntoHasMaps("OPERAND", sle.asString());
    }
    public void visit(IntegerLiteralExpr ile, Void arg) {
        super.visit(ile, arg);
        System.out.println("OPERAND  : "+ ile.toString()+ " -> ["+ile.getBegin().get()+"]-[IntegerLiteralExpr]");
        insertIntoHasMaps("OPERAND", ile.toString());
    }
    public void visit(LongLiteralExpr lle, Void arg) {
        super.visit(lle, arg);
        System.out.println("OPERAND  : "+ lle.toString()+ " -> ["+lle.getBegin().get()+"]-[LongLiteralExpr]");
        insertIntoHasMaps("OPERAND", lle.toString());
    }
    public void visit(DoubleLiteralExpr dle, Void arg) {
        super.visit(dle, arg);
        System.out.println("OPERAND  : "+ dle.toString()+ " -> ["+dle.getBegin().get()+"]-[DoubleLiteralExpr]");
        insertIntoHasMaps("OPERAND", dle.toString());
    }
    public void visit(CharLiteralExpr cle, Void arg) {
        super.visit(cle, arg);
        System.out.println("OPERAND  : "+ cle.toString()+ " -> ["+cle.getBegin().get()+"]-[CharLiteralExpr]");
        insertIntoHasMaps("OPERAND", cle.toString());
    }
    public void visit(BooleanLiteralExpr ble, Void arg) {
        super.visit(ble, arg);
        System.out.println("OPERAND  : "+ ble.toString()+ " -> ["+ble.getBegin().get()+"]-[BooleanLiteralExpr]");
        insertIntoHasMaps("OPERAND", ble.toString());
    }
    public void visit(NullLiteralExpr nle, Void arg) {
        super.visit(nle, arg);
        System.out.println("OPERAND  : "+ nle.toString()+ " -> ["+nle.getBegin().get()+"]-[NullLiteralExpr]");
        insertIntoHasMaps("OPERAND", nle.toString());
    }
    /* END BLOCK UNTUK MENCARI SEBUAH NILAI */


    /* BLOCK UNTUK MENCARI OPERATOR PEDICATE NODE */
    public void visit(IfStmt is, Void arg) {
        super.visit(is, arg);
        System.out.println("OPERATOR : if" + " -> ["+is.getBegin().get()+"]-[IfStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "if");

    }

    public void visit(SwitchStmt ss, Void arg) {
        super.visit(ss, arg);
        System.out.println("OPERATOR : switch" + " -> ["+ss.getBegin().get()+"]-[SwitchStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "switch");
    }

    public void visit(DoStmt ds, Void arg) {
        super.visit(ds, arg);
        System.out.println("OPERATOR : do" + " -> ["+ds.getBegin().get()+"]-[DoStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "do");
    }

    public void visit(WhileStmt ws, Void arg) {
        super.visit(ws, arg);
        System.out.println("OPERATOR : while" + " -> ["+ws.getBegin().get()+"]-[WhileStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "while");
    }

    public void visit(ForStmt fs, Void arg) {
        super.visit(fs, arg);
        System.out.println("OPERATOR : for" + " -> ["+fs.getBegin().get()+"]-[ForStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "for");
    }

    public void visit(ForeachStmt fes, Void arg) {
        super.visit(fes, arg);
        System.out.println("OPERATOR : foreach" + " -> ["+fes.getBegin().get()+"]-[ForeachStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "foreach");
    }
    /* END BLOCK UNTUK MENCARI OPERATOR PEDICATE NODE */

    public void visit(TryStmt ts, Void arg) {
        super.visit(ts, arg);
        System.out.println("OPERATOR : try" + " -> ["+ts.getBegin().get()+"]-[TryStmt]");
        insertIntoHasMaps("OPERATOR", "try");
    }

    public void visit(CatchClause ts, Void arg) {
        super.visit(ts, arg);
        System.out.println("OPERATOR : catch" + " -> ["+ts.getBegin().get()+"]-[CatchClause]");
        insertIntoHasMaps("OPERATOR", "catch");
    }

    public void visit(ThisExpr te, Void arg) {
        super.visit(te, arg);
        System.out.println("OPERATOR : this" + " -> ["+te.getBegin().get()+"]-[ThisExpr]");
        insertIntoHasMaps("OPERATOR", "this");
    }

    public void visit(ReturnStmt rs, Void arg) {
        super.visit(rs, arg);
        System.out.println("OPERATOR : return" + " -> ["+rs.getBegin().get()+"]-[ReturnStmt]");
        insertIntoHasMaps("OPERATOR", "return");
    }

    public void visit(ContinueStmt cs, Void arg) {
        super.visit(cs, arg);
        System.out.println("OPERATOR : continue" + " -> ["+cs.getBegin().get()+"]-[ContinueStmt]");
        insertIntoHasMaps("OPERATOR", "continue");
    }

    public void visit(BreakStmt bs, Void arg) {
        super.visit(bs, arg);
        System.out.println("OPERATOR : break" + " -> ["+bs.getBegin().get()+"]-[BreakStmt]");
        insertIntoHasMaps("OPERATOR", "break");
    }

    public void insertIntoHasMaps(String category, String node){
        if (category.equalsIgnoreCase("OPERATOR")){

            if(!listOfOperator.containsKey(node)){
                listOfOperator.put(node, 1);
                System.out.println("OPERATOR: " + node + " = 1");
            } else {
                int numberOfValueOfKey = listOfOperator.get(node) + 1;
                listOfOperator.remove(node);
                listOfOperator.put(node, numberOfValueOfKey);
                System.out.println("Update OPERATOR: " + node + " = " + numberOfValueOfKey);
            }
        } else if (category.equalsIgnoreCase("OPERAND")){
            if(!listOfOperand.containsKey(node)){
                listOfOperand.put(node, 1);
                System.out.println("OPERAND: " + node + " = 1");
            } else {
                int numberOfValueOfKey = listOfOperand.get(node) + 1;
                listOfOperand.remove(node);
                listOfOperand.put(node, numberOfValueOfKey);
                System.out.println("Update OPERAND: " + node + " = " + numberOfValueOfKey);
            }
        }
    }

    public void save(){
        operandAndOperator.setlistMethodOperand(listOfOperand);
        operandAndOperator.setlistMethodOperator(listOfOperator);
        predicateNode.setPredicateNode(countPredicaeNode);
        System.out.println("Operator and Operand's Data has been saved");
    }




}
