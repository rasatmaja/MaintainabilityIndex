package app.controllers;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class OperandAndOperatorExtraction extends VoidVisitorAdapter<Void> {

    @Override
    public void visit(BlockStmt blockStmt, Void arg) {
        super.visit(blockStmt, arg);
        blockStmt.walk(node -> {
            if (node instanceof VariableDeclarationExpr){
                VariableDeclarationExpr vde = (VariableDeclarationExpr) node;

                //untuk mengantisipasi ketika ada multiple deklarasi inisialisasi seperti int a=8,b=9;
                vde.getVariables().forEach(variable -> {
                    System.out.println("VariableDeclarationExpr: " + variable.toString() +"->" + variable.getName());
                    if(variable.toString().contains("=")){
                        System.out.println("Get AssignExpr : = ");
                        //menunjukan bahwa ini merupakan deklarasi inisiaalisasi seperti int a = 1;
                    }
                });
            }else if (node instanceof AssignExpr) {
                AssignExpr ae = (AssignExpr) node;
                System.out.println("AssignExpr   : " + ae.asAssignExpr()+ " -> "+ae.getOperator().asString());
                System.out.println("Get Variable : " + ae.getTarget());
            }
        });

    }
}
