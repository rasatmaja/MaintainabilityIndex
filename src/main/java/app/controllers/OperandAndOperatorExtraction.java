package app.controllers;

import app.models.ClassEdgeProperty;
import app.models.OperandAndOperator;
import app.models.PredicateNode;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class OperandAndOperatorExtraction extends VoidVisitorAdapter<Void> {

    private Map<String, Integer> listOfOperator = new HashMap<>();
    private Map<String, Integer> listOfOperand = new HashMap<>();
    private int countPredicaeNode = 0;
    private OperandAndOperator operandAndOperator;
    private PredicateNode predicateNode;
    private ClassEdgeProperty classEdgeProperty;
    private String className;

    public OperandAndOperatorExtraction(String className, int key){
        this.className = className;
        classEdgeProperty = ClassEdgeProperty.getInstance();
        predicateNode = PredicateNode.getInstance();
        predicateNode.setMethodPropertyKey(key);
        operandAndOperator = OperandAndOperator.getInstance();
        operandAndOperator.setMethodPropertyKey(key);
    }

    //untuk tujuan pengujian
    public OperandAndOperatorExtraction(){}

    /* BLOCK UNTUK MENCARI DEKLARASI ARRAY */
    public void visit(ArrayType at, Void arg){
        super.visit(at, arg);
        System.out.println("OPERAND: "+at.asArrayType()+ "-> [" + at.getBegin().get() + "]-[ArrayType]");
        //array type masih ada dupikat ketika dimensi aaray lebhh dari 1
        insertIntoHasMaps("OPERAND", at.asArrayType().asString());

    }
    public void visit(ArrayCreationExpr ace, Void arg) {
        super.visit(ace, arg);
        System.out.println("OPERATOR:  new -> [" + ace.getBegin().get() + "]-[ArrayCreationExpr]");
        insertIntoHasMaps("OPERATOR", "new");
        listOfOperator.remove(ace.getElementType());
        int arrayLevel = ace.getLevels().toArray().length;
        String arrayDataType = ace.getElementType().asString();
        for (int i = 0; i< arrayLevel; i++ ) {
            arrayDataType+="[]";
        }
        System.out.println("OPERAND: "+arrayDataType+" -> [" + ace.getBegin().get() + "]-[ArrayCreationExpr]");
        insertIntoHasMaps("OPERAND", arrayDataType);
    }
    public void visit(ArrayInitializerExpr aie, Void arg) {
        super.visit(aie, arg);
        System.out.println("OPERATOR : = " + " -> ["+aie.getBegin().get()+"]-[AssignExpr]");
        insertIntoHasMaps("OPERATOR", "=");
        insertIntoHasMaps("OPERATOR", "{}");
        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");
    }
    public void visit (ArrayAccessExpr aae, Void arg) {
        super.visit(aae, arg);
        System.out.println("OPERAND: "+aae.toString()+" -> [" + aae.getBegin().get() + "]-[ArrayAccessExpr]");
        insertIntoHasMaps("OPERAND", aae.toString());
    }
    /* END BLOCK UNTUK MENCARI DEKLARASI ARRAY */

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
            //if(variable.toString().equalsIgnoreCase("=")){
            if(variable.toString().contains("=")){
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
        System.out.println("OPERATOR :  "+ ae.getOperator().asString() + " -> ["+ae.getBegin().get()+"]-[AssignExpr]");
        insertIntoHasMaps("OPERATOR", ae.getOperator().asString());
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
        insertIntoHasMaps("OPERATOR", "()");

        if(is.hasThenBlock()){
            insertIntoHasMaps("OPERATOR", "{}");
        }
        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");

        /* Menemukan compound condition */
        String[] compoundConditions = {"||", "&&"};
        for (String compoundCondition : compoundConditions) {
            if (is.getCondition().toString().contains(compoundCondition)) {
                //System.out.println("ada compound: " + compoundCondition);
                countPredicaeNode +=StringUtils.countMatches(is.getCondition().toString(), compoundCondition);
            }
        }

        /* Menemukan else block*/
        if (is.hasElseBlock()){
            insertIntoHasMaps("OPERATOR", "{}");
            //insertIntoHasMaps("OPERATOR", "{");
            //insertIntoHasMaps("OPERATOR", "}");
        }
    }

    public void visit(SwitchStmt ss, Void arg) {
        super.visit(ss, arg);
        System.out.println("OPERATOR : switch" + " -> ["+ss.getBegin().get()+"]-[SwitchStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "switch");
        insertIntoHasMaps("OPERATOR", "{}");
        insertIntoHasMaps("OPERATOR", "()");

        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");
    }

    public void visit(DoStmt ds, Void arg) {
        super.visit(ds, arg);
        System.out.println("OPERATOR : do" + " -> ["+ds.getBegin().get()+"]-[DoStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "do");
        insertIntoHasMaps("OPERATOR", "{}");
        insertIntoHasMaps("OPERATOR", "()");

        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");
    }

    public void visit(WhileStmt ws, Void arg) {
        super.visit(ws, arg);
        System.out.println("OPERATOR : while" + " -> ["+ws.getBegin().get()+"]-[WhileStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "while");
        insertIntoHasMaps("OPERATOR", "{}");
        insertIntoHasMaps("OPERATOR", "()");

        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");
    }

    public void visit(ForStmt fs, Void arg) {
        super.visit(fs, arg);
        System.out.println("OPERATOR : for" + " -> ["+fs.getBegin().get()+"]-[ForStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "for");
        insertIntoHasMaps("OPERATOR", "()");
        insertIntoHasMaps("OPERATOR", "{}");

        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");
    }

    public void visit(ForeachStmt fes, Void arg) {
        super.visit(fes, arg);
        System.out.println("OPERATOR : foreach" + " -> ["+fes.getBegin().get()+"]-[ForeachStmt]");
        countPredicaeNode++;
        insertIntoHasMaps("OPERATOR", "foreach");
        insertIntoHasMaps("OPERATOR", "()");
        insertIntoHasMaps("OPERATOR", "{}");

        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");
    }
    /* END BLOCK UNTUK MENCARI OPERATOR PEDICATE NODE */

    public void visit(TryStmt ts, Void arg) {
        super.visit(ts, arg);
        System.out.println("OPERATOR : try" + " -> ["+ts.getBegin().get()+"]-[TryStmt]");
        insertIntoHasMaps("OPERATOR", "try");
        insertIntoHasMaps("OPERATOR", "{}");

        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");
    }

    public void visit(CatchClause ts, Void arg) {
        super.visit(ts, arg);
        System.out.println("OPERATOR : catch" + " -> ["+ts.getBegin().get()+"]-[CatchClause]");
        insertIntoHasMaps("OPERATOR", "catch");
        insertIntoHasMaps("OPERATOR", "()");
        insertIntoHasMaps("OPERATOR", "{}");

        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
        //insertIntoHasMaps("OPERATOR", "{");
        //insertIntoHasMaps("OPERATOR", "}");
    }

    public void visit(ThisExpr te, Void arg) {
        super.visit(te, arg);
        System.out.println("OPERAND : this" + " -> ["+te.getBegin().get()+"]-[ThisExpr]");
        insertIntoHasMaps("OPERAND", "this");
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

    public void visit(MethodCallExpr mce, Void arg){
        super.visit(mce, arg);
        System.out.println("OPERAND : " +mce.getName()+ " -> ["+mce.getBegin().get()+"]-[MethodCallExpr]");
        insertIntoHasMaps("OPERAND", mce.getName().asString());

        System.out.println("OPERATOR : () -> ["+mce.getBegin().get()+"]-[MethodCallExpr]");
        insertIntoHasMaps("OPERATOR", "()");

        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
    }

    public void visit(ObjectCreationExpr mce, Void arg){
        super.visit(mce, arg);
        System.out.println("OPERATOR : new  -> ["+mce.getBegin().get()+"]-[ObjectCreationExpr]");
        insertIntoHasMaps("OPERATOR", "new");
        insertIntoHasMaps("OPERATOR", "()");

        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
    }

    public void visit(ClassOrInterfaceType coit, Void arg){
        super.visit(coit, arg);
        System.out.println("OPERAND : " +coit.toString()+ " -> ["+coit.getBegin().get()+"]-[ClassOrInterfaceType]");
        insertIntoHasMaps("OPERAND", coit.toString());
        classEdgeProperty.set(this.className, coit.toString());
    }
    public void visit(LambdaExpr le, Void arg) {
        super.visit(le, arg);
        insertIntoHasMaps("OPERATOR", "->");
        le.getParameters().forEach((parameter) -> {
            System.out.println("OPERAND : " + parameter.toString() + " -> ["+parameter.getBegin().get()+"]-[LambdaExpr]");
            insertIntoHasMaps("OPERAND", parameter.getName().asString());
        });
        //Belum menentukan apakah "->" termasuk operand atau operator
        if(le.isEnclosingParameters()){
            insertIntoHasMaps("OPERATOR", "()");
            //insertIntoHasMaps("OPERATOR", "(");
            //insertIntoHasMaps("OPERATOR", ")");
        }
        if(le.getBody().isBlockStmt()) {
            insertIntoHasMaps("OPERATOR", "{}");
            //insertIntoHasMaps("OPERATOR", "{");
            //insertIntoHasMaps("OPERATOR", "}");
        }
    }
    public void visit(FieldAccessExpr fae, Void arg) {
        super.visit(fae, arg);
        System.out.println("OPERAND  : " + fae.getName()+ " -> [" + fae.getBegin().get() + "]-[FieldAccessExpr]");
        insertIntoHasMaps("OPERAND",fae.getName().asString());
    }

    public void visit(ExplicitConstructorInvocationStmt pt, Void arg) {
        super.visit(pt, arg);
        String a = pt.toString().replaceAll("\\(.*?\\);", "");
        System.out.println("OPERAND  : " + a + " -> [" + pt.getBegin().get() + "]-[ExplicitConstructorInvocationStmt]");
        insertIntoHasMaps("OPERAND", a);
        insertIntoHasMaps("OPERATOR", "()");
        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
    }

    public void visit(CastExpr md, Void arg) {
        super.visit(md, arg);
        System.out.println("OPERATOR : () " + " -> [" + md.getBegin().get() + "]-[CastExpr]");
        insertIntoHasMaps("OPERATOR", "()");
        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
    }

    public void visit(EnclosedExpr md, Void arg) {
        super.visit(md, arg);
        System.out.println("OPERATOR : () " + " -> [" + md.getBegin().get() + "]-[EnclosedExpr]");
        insertIntoHasMaps("OPERATOR", "()");
        //insertIntoHasMaps("OPERATOR", "(");
        //insertIntoHasMaps("OPERATOR", ")");
    }

    public void visit(ThrowStmt md, Void arg) {
        super.visit(md, arg);
        System.out.println("OPERATOR : throw " + " -> [" + md.getBegin().get() + "]-[ThrowStmt]");
        insertIntoHasMaps("OPERATOR", "throw");
    }

    public void visit(ConditionalExpr md, Void arg) {
        /*
        * untuk mencari konsidi seperti int c = a > b ? 10 : 0;
        * */
        super.visit(md, arg);
        System.out.println("OPERATOR : ? " + " -> [" + md.getBegin().get() + "]-[ConditionalExpr]");
        insertIntoHasMaps("OPERATOR", "?");
        insertIntoHasMaps("OPERATOR", ":");

        /* Ada penambahan predicate node untuk cyclomatic complexity*/
        countPredicaeNode++;
        String[] compoundConditions = {"||", "&&"};
        for (String compoundCondition : compoundConditions) {
            if (md.getCondition().toString().contains(compoundCondition)) {
                //System.out.println("ada compound: " + compoundCondition);
                countPredicaeNode += StringUtils.countMatches(md.getCondition().toString(), compoundCondition);
            }
        }
    }

    public void visit(ClassExpr pt, Void arg) {
        super.visit(pt, arg);
        System.out.println("OPERAND  : " + pt.asClassExpr()+ " -> [" + pt.getBegin().get() + "]-[ClassExpr]");
        insertIntoHasMaps("OPERAND", "class");
    }

    public void visit(TypeExpr pt, Void arg) {
        super.visit(pt, arg);
        System.out.println("OPERAND  : " + pt.getType()+ " -> [" + pt.getBegin().get() + "]-[TypeExpr]");
        insertIntoHasMaps("OPERAND", pt.getType().toString());
    }

    public void visit(MethodReferenceExpr pt, Void arg) {
        super.visit(pt, arg);
        System.out.println("OPERAND  : " + pt.getIdentifier()+ " -> [" + pt.getBegin().get() + "]-[MethodReferenceExpr]");
        insertIntoHasMaps("OPERAND", pt.getIdentifier());
        insertIntoHasMaps("OPERATOR", "::");
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
        if(listOfOperator.isEmpty() || listOfOperand.isEmpty()){
            System.out.println("There is no body method");
            System.out.println("nilai distinc opeator " + listOfOperator.size());
            System.out.println("nilai distinc operand " + listOfOperand.size());
            countPredicaeNode = -1;
            System.out.println("nilai predicate node " + countPredicaeNode);
        }
        predicateNode.setPredicateNode(countPredicaeNode);
        System.out.println("Operator and Operand's Data has been saved");
    }

    // untuk percobaan pengujian unit
    public Map<String, Integer> getListOfOperator(){
        return this.listOfOperator;
    }

    // untuk percobaan pengujian unit
    public Map<String, Integer> getListOfOperand(){
        return this.listOfOperand;
    }
}
