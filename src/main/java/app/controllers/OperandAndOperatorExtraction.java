package app.controllers;

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

public class OperandAndOperatorExtraction extends VoidVisitorAdapter<Void> {

    String methodName;
    String className;

    public OperandAndOperatorExtraction(){

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
                    System.out.println("VariableDeclarationExpr: " + "["+node.getBegin().get()+"] " + variable.getName());
                    if (variable.toString().contains("=")) {
                        System.out.println("Get AssignExpr : = ");
                        //menunjukan bahwa ini merupakan deklarasi inisiaalisasi seperti int a = 1;
                        //merupakan operator
                    }
                });
            } else if (node instanceof NameExpr) {
                NameExpr ne = (NameExpr) node;
                System.out.println("Name Expression : " + "["+node.getBegin().get()+"] " + ne.toString());

                //untuk mencari operand dari stament a = 123
                //perlu dibuatkan fungsi untu mengecek apakh name expression meruakan class name. seperti Operator.getInstance;

            } else if (node instanceof PrimitiveType) {
                PrimitiveType pt = (PrimitiveType) node;
                System.out.println("PrimitiveType : " + "["+node.getBegin().get()+"] " + pt.asString());

            } else if (node instanceof StringLiteralExpr) {
                StringLiteralExpr sle = (StringLiteralExpr) node;
                System.out.println("get type String : String");
                System.out.println("StringLiteralExpr : " + sle.asString());

            } else if (node instanceof AssignExpr) {
                AssignExpr ae = (AssignExpr) node;
                System.out.println("AssignExpr   : " + "["+node.getBegin().get()+"] " + ae.getOperator().asString());
                //System.out.println("AssignExpr   : " + ae.asAssignExpr() + " -> " + ae.getOperator().asString());
                //merupakan operator

            } else if (node instanceof IntegerLiteralExpr){
                IntegerLiteralExpr ile = (IntegerLiteralExpr) node;
                System.out.println("IntegerLiteralExpr : " + "["+node.getBegin().get()+"] " + ile.asIntegerLiteralExpr());

            } else if (node instanceof LongLiteralExpr){
                LongLiteralExpr lle = (LongLiteralExpr) node;
                System.out.println("LongLiteralExpr : " + "["+node.getBegin().get()+"] " + lle.asLongLiteralExpr());

            } else if (node instanceof DoubleLiteralExpr){
                DoubleLiteralExpr dle = (DoubleLiteralExpr) node;
                System.out.println("DoubleLiteralExpr : " + "["+node.getBegin().get()+"] " + dle.asDoubleLiteralExpr());

            } else if (node instanceof CharLiteralExpr){
                CharLiteralExpr cle = (CharLiteralExpr) node;
                System.out.println("CharLiteralExpr : " + "["+node.getBegin().get()+"] " + cle.asCharLiteralExpr());

            } else if (node instanceof BooleanLiteralExpr){
                BooleanLiteralExpr ble = (BooleanLiteralExpr) node;
                System.out.println("BooleanLiteralExpr : " + "["+node.getBegin().get()+"] " + ble.asBooleanLiteralExpr());

            } else if (node instanceof CastExpr){
                //perlu pengkondisian apakah didlama casting merupakan tipe prymitif
            } else if (node instanceof BinaryExpr){
                BinaryExpr be = (BinaryExpr) node;
                System.out.println("BinaryExpr : " + "["+node.getBegin().get()+"] " + be.getOperator().asString());

            } else if (node instanceof UnaryExpr){
                UnaryExpr ue = (UnaryExpr) node;
                System.out.println("UnaryExpr : " + "["+node.getBegin().get()+"] " + ue.getOperator().asString());

            } else if(node instanceof ConditionalExpr){
                ConditionalExpr ce = (ConditionalExpr) node;
                System.out.println("ConditionalExpr : " + "["+node.getBegin().get()+"] " + ce.asConditionalExpr());

            } else if(node instanceof IfStmt){
                IfStmt is = (IfStmt) node;
                System.out.println("IfStmt : " + "["+node.getBegin().get()+"] " + is.asIfStmt());

            } else if (node instanceof SwitchStmt){
                SwitchStmt ss = (SwitchStmt) node;
                System.out.println("SwitchStmt : " + "["+node.getBegin().get()+"] " + ss.asSwitchStmt());

            } else if (node instanceof DoStmt){
                DoStmt ds = (DoStmt) node;
                System.out.println("DoStmt : " + "["+node.getBegin().get()+"] " + ds.asDoStmt());

            } else if (node instanceof ForStmt){
                ForStmt ft = (ForStmt) node;
                System.out.println("ForStmt : " + "["+node.getBegin().get()+"] " + ft.asForStmt());

            } else if (node instanceof ForeachStmt){
                ForeachStmt fet = (ForeachStmt) node;
                System.out.println("ForStmt : " + "["+node.getBegin().get()+"] " + fet.asForeachStmt());

            } else if (node instanceof WhileStmt){
                WhileStmt ws = (WhileStmt) node;
                System.out.println("WhileStmt : " + "["+node.getBegin().get()+"] " + ws.asWhileStmt());

            } else if (node instanceof TryStmt){
                TryStmt ts = (TryStmt) node;
                System.out.println("TryStmt : " + "["+node.getBegin().get()+"] " + ts.asTryStmt());

            } else if (node instanceof CatchClause){
                CatchClause cc = (CatchClause) node;
                System.out.println("CatchClause : " + "["+node.getBegin().get()+"] " + cc.toString());

            } else if (node instanceof ThisExpr){
                ThisExpr te = (ThisExpr) node;
                System.out.println("ThisExpr : " + "["+node.getBegin().get()+"] " + te.asThisExpr());

            } else if (node instanceof SuperExpr){
                SuperExpr se = (SuperExpr) node;
                System.out.println("SuperExpr : " + "["+node.getBegin().get()+"] " + se.asSuperExpr());

            } else if (node instanceof ReturnStmt){
                ReturnStmt rs = (ReturnStmt) node;
                //System.out.println("ReturnStmt : " + rs.asReturnStmt());
                System.out.println("ReturnStmt : return");

            } else if (node instanceof ContinueStmt){
                ContinueStmt cs = (ContinueStmt) node;
                System.out.println("ContinueStmt : " + cs.asContinueStmt());

            } else if (node instanceof BreakStmt){
                BreakStmt bs = (BreakStmt) node;
                System.out.println("TryStmt : " + bs.asBreakStmt());
            }
        });
    }
}
