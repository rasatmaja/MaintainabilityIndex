package app.controllers;

import app.models.OperandAndOperator;
import app.models.PredicateNode;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
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

    @Override
    public void visit(BlockStmt blockStmt, Void arg) {
        super.visit(blockStmt, arg);
        blockStmt.walk(node -> {
            if (node instanceof VariableDeclarationExpr) {
                VariableDeclarationExpr vde = (VariableDeclarationExpr) node;

                //untuk mengantisipasi ketika ada multiple deklarasi inisialisasi seperti int a=8,b=9;
                vde.getVariables().forEach(variable -> {
                    //System.out.println("VariableDeclarationExpr: " + variable.toString() + "->" + variable.getName());
                    //System.out.println("VariableDeclarationExpr: " + "["+node.getBegin().get()+"] " + variable.getName().toString());
                    insertIntoHasMaps("OPERAND", variable.getName().toString());
                    if (variable.toString().contains("=")) {
                        //System.out.println("Get AssignExpr : = ");
                        insertIntoHasMaps("OPERATOR", "=");
                        //menunjukan bahwa ini merupakan deklarasi inisiaalisasi seperti int a = 1;
                        //merupakan operator
                    }
                });
            } else if (node instanceof NameExpr) {
                NameExpr ne = (NameExpr) node;
                System.out.println("Name Expression : " + "["+node.getBegin().get()+"] " + ne.toString());
                insertIntoHasMaps("OPERAND", ne.toString());
                //untuk mencari operand dari stament a = 123
                //perlu dibuatkan fungsi untu mengecek apakh name expression meruakan class name. seperti Operator.getInstance;

            } else if (node instanceof PrimitiveType) {
                PrimitiveType pt = (PrimitiveType) node;
                //System.out.println("PrimitiveType : " + "["+node.getBegin().get()+"] " + pt.asString());
                insertIntoHasMaps("OPERAND", pt.asString());

            } else if (node instanceof StringLiteralExpr) {
                StringLiteralExpr sle = (StringLiteralExpr) node;
                //dalam node ini akan menemukan tipe variable STRING dan nilainya;
                System.out.println("get type String : String");
                insertIntoHasMaps("OPERAND", "String");
                //System.out.println("StringLiteralExpr : " + sle.asString());
                insertIntoHasMaps("OPERAND", sle.asString());

            } else if (node instanceof AssignExpr) {
                AssignExpr ae = (AssignExpr) node;
                System.out.println("AssignExpr   : " + "["+node.getBegin().get()+"] " + ae.getOperator().asString());
                //System.out.println("AssignExpr   : " + ae.asAssignExpr() + " -> " + ae.getOperator().asString());
                //merupakan operator
                insertIntoHasMaps("OPERATOR", ae.getOperator().asString());

            } else if (node instanceof IntegerLiteralExpr){
                //Mencari nilai INTERGER -> 1234
                IntegerLiteralExpr ile = (IntegerLiteralExpr) node;
                //System.out.println("IntegerLiteralExpr : " + "["+node.getBegin().get()+"] " + ile.toString());

                insertIntoHasMaps("OPERAND", ile.toString());

            } else if (node instanceof LongLiteralExpr){
                LongLiteralExpr lle = (LongLiteralExpr) node;
                //System.out.println("LongLiteralExpr : " + "["+node.getBegin().get()+"] " + lle.asLongLiteralExpr());

                insertIntoHasMaps("OPERAND", lle.toString());

            } else if (node instanceof DoubleLiteralExpr){
                DoubleLiteralExpr dle = (DoubleLiteralExpr) node;
                //System.out.println("DoubleLiteralExpr : " + "["+node.getBegin().get()+"] " + dle.asDoubleLiteralExpr());

                insertIntoHasMaps("OPERAND", dle.toString());

            } else if (node instanceof CharLiteralExpr){
                //untuk mencari nilai dari variable char -> 'a'

                CharLiteralExpr cle = (CharLiteralExpr) node;
                //System.out.println("CharLiteralExpr : " + "["+node.getBegin().get()+"] " + cle.asCharLiteralExpr());

                insertIntoHasMaps("OPERAND", cle.toString());

            } else if (node instanceof BooleanLiteralExpr){
                //untuk mencari nilai dari variable boolean -> 'true'/'false'

                BooleanLiteralExpr ble = (BooleanLiteralExpr) node;
                //System.out.println("BooleanLiteralExpr : " + "["+node.getBegin().get()+"] " + ble.asBooleanLiteralExpr());

                insertIntoHasMaps("OPERAND", ble.toString());

            } else if (node instanceof CastExpr){
                //perlu pengkondisian apakah didlama casting merupakan tipe prymitif
            } else if (node instanceof BinaryExpr){
                //untuk mencari operator +, - * /

                BinaryExpr be = (BinaryExpr) node;
                //System.out.println("BinaryExpr : " + "["+node.getBegin().get()+"] " + be.getOperator().asString());

                insertIntoHasMaps("OPERATOR", be.getOperator().asString());

            } else if (node instanceof UnaryExpr){
                //untuk mencari operator seperti ++, --
                UnaryExpr ue = (UnaryExpr) node;
                //System.out.println("UnaryExpr : " + "["+node.getBegin().get()+"] " + ue.getOperator().asString());

                insertIntoHasMaps("OPERATOR", ue.getOperator().asString());

            } else if(node instanceof ConditionalExpr){
                ConditionalExpr ce = (ConditionalExpr) node;
                System.out.println("ConditionalExpr : " + "["+node.getBegin().get()+"] " + ce.asConditionalExpr());

            } else if(node instanceof IfStmt){

                countPredicaeNode++;

                IfStmt is = (IfStmt) node;
                System.out.println("IfStmt : " + "["+node.getBegin().get()+"] " + is.asIfStmt());
                insertIntoHasMaps("OPERATOR", "if");

            } else if (node instanceof SwitchStmt){

                countPredicaeNode++;

                SwitchStmt ss = (SwitchStmt) node;
                System.out.println("SwitchStmt : " + "["+node.getBegin().get()+"] " + ss.asSwitchStmt());

                insertIntoHasMaps("OPERATOR", "Switch");

            } else if (node instanceof DoStmt){

                countPredicaeNode++;

                DoStmt ds = (DoStmt) node;
                System.out.println("DoStmt : " + "["+node.getBegin().get()+"] " + ds.asDoStmt());

                insertIntoHasMaps("OPERATOR", "do");

            } else if (node instanceof ForStmt){

                countPredicaeNode++;

                ForStmt ft = (ForStmt) node;
                System.out.println("ForStmt : " + "["+node.getBegin().get()+"] " + ft.asForStmt());

                insertIntoHasMaps("OPERATOR", "for");

            } else if (node instanceof ForeachStmt){

                countPredicaeNode++;

                ForeachStmt fet = (ForeachStmt) node;
                System.out.println("ForStmt : " + "["+node.getBegin().get()+"] " + fet.asForeachStmt());

                insertIntoHasMaps("OPERATOR", "for");

            } else if (node instanceof WhileStmt){

                countPredicaeNode++;

                WhileStmt ws = (WhileStmt) node;
                System.out.println("WhileStmt : " + "["+node.getBegin().get()+"] " + ws.asWhileStmt());

                insertIntoHasMaps("OPERATOR", "while");

            } else if (node instanceof TryStmt){
                TryStmt ts = (TryStmt) node;
                System.out.println("TryStmt : " + "["+node.getBegin().get()+"] " + ts.asTryStmt());

                insertIntoHasMaps("OPERATOR", "try");

            } else if (node instanceof CatchClause){
                CatchClause cc = (CatchClause) node;
                System.out.println("CatchClause : " + "["+node.getBegin().get()+"] " + cc.toString());

                insertIntoHasMaps("OPERATOR", "catch");

            } else if (node instanceof ThisExpr){
                ThisExpr te = (ThisExpr) node;
                System.out.println("ThisExpr : " + "["+node.getBegin().get()+"] " + te.asThisExpr());

                insertIntoHasMaps("OPERATOR", "this");

            } else if (node instanceof SuperExpr){
                SuperExpr se = (SuperExpr) node;
                System.out.println("SuperExpr : " + "["+node.getBegin().get()+"] " + se.asSuperExpr());

                insertIntoHasMaps("OPERATOR", "super");

            } else if (node instanceof ReturnStmt){
                ReturnStmt rs = (ReturnStmt) node;
                //System.out.println("ReturnStmt : " + rs.asReturnStmt());
                System.out.println("ReturnStmt : return");

                insertIntoHasMaps("OPERATOR", "return");

            } else if (node instanceof ContinueStmt){
                ContinueStmt cs = (ContinueStmt) node;
                System.out.println("ContinueStmt : " + cs.asContinueStmt());

                insertIntoHasMaps("OPERATOR", "continue");

            } else if (node instanceof BreakStmt){
                BreakStmt bs = (BreakStmt) node;
                System.out.println("BreakStmt : " + bs.asBreakStmt());

                insertIntoHasMaps("OPERATOR", "break");
            }
        });
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
