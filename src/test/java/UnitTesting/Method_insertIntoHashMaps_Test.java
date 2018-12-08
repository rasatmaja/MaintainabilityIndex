package UnitTesting;

import app.controllers.OperandAndOperatorExtraction;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class Method_insertIntoHashMaps_Test {

    /*
    * Category node ≠ “OPERATOR”
    * Category node ≠ “OPERAND”
    *
    * exppected result: Node tidak tersimpan pada list
    * */
    @Test
    public void jalur1(){
        OperandAndOperatorExtraction ooe = new OperandAndOperatorExtraction();
        ooe.insertIntoHasMaps("bukan Operand atau operand", "test jalur 1");
        ooe.insertIntoHasMaps("bukan Operator", "test jalur 1");

        System.out.println("Hasil Pengujian Jalur 1:");
        assertEquals(0, ooe.getListOfOperand().size());
        assertEquals(0, ooe.getListOfOperator().size());

        if (ooe.getListOfOperand().size() == 0){
            System.out.println("Node tidak tersimpan pada list");
        }
        System.out.println();

    }

    /*
     * Category node = “OPERATOR”
     * node = “+”
     * exppected result: Node tersimpan di dalam list operator dengan jumlah satu
     * */
    @Test
    public void jalur2(){
        OperandAndOperatorExtraction ooe = new OperandAndOperatorExtraction();
        ooe.insertIntoHasMaps("OPERATOR", "+");

        System.out.println("Hasil Pengujian Jalur 1:");
        assertTrue(ooe.getListOfOperator().containsKey("+"));
        assertEquals(1, ooe.getListOfOperator().get("+").intValue());

        if (ooe.getListOfOperator().containsKey("+")){
            System.out.println("Node Operator (+) dengan jumlah " + ooe.getListOfOperator().get("+") + " Tesimpan pada list");
        }
        System.out.println();
    }

    /*
     * Category node = “OPERATOR”
     * node = “+”
     * node = "+"
     * exppected result: Node tersimpan di dalam list operator dengan jumlah satu
     * */
    @Test
    public void jalur3(){
        OperandAndOperatorExtraction ooe = new OperandAndOperatorExtraction();
        ooe.insertIntoHasMaps("OPERATOR", "+");
        ooe.insertIntoHasMaps("OPERATOR", "+");

        System.out.println("Hasil Pengujian Jalur 3:");
        assertTrue(ooe.getListOfOperator().containsKey("+"));
        assertEquals(2, ooe.getListOfOperator().get("+").intValue());

        if (ooe.getListOfOperator().containsKey("+")){
            System.out.println("Node Operator (+) dengan jumlah " + ooe.getListOfOperator().get("+") + " Tesimpan pada list");
        }
        System.out.println();
    }

    /*
     * Category node = “OPERAND”
     * node = “int”
     * exppected result: Node tersimpan di dalam list operator dengan jumlah satu
     * */
    @Test
    public void jalur4(){
        OperandAndOperatorExtraction ooe = new OperandAndOperatorExtraction();
        ooe.insertIntoHasMaps("OPERAND", "int");

        System.out.println("Hasil Pengujian Jalur 4:");
        assertTrue(ooe.getListOfOperand().containsKey("int"));
        assertEquals(1, ooe.getListOfOperand().get("int").intValue());

        if (ooe.getListOfOperand().containsKey("int")){
            System.out.println("Node Operand (int) dengan jumlah " + ooe.getListOfOperand().get("int") + " Tesimpan pada list");
        }
        System.out.println();
    }

    /*
     * Category node = “OPERAND”
     * node = “int”
     * exppected result: Node tersimpan di dalam list operator dengan jumlah satu
     * */
    @Test
    public void jalur5(){
        OperandAndOperatorExtraction ooe = new OperandAndOperatorExtraction();
        ooe.insertIntoHasMaps("OPERAND", "int");
        ooe.insertIntoHasMaps("OPERAND", "int");

        System.out.println("Hasil Pengujian Jalur 5:");
        assertTrue(ooe.getListOfOperand().containsKey("int"));
        assertEquals(2, ooe.getListOfOperand().get("int").intValue());

        if (ooe.getListOfOperand().containsKey("int")){
            System.out.println("Node Operand (int) dengan jumlah " + ooe.getListOfOperand().get("int") + " Tesimpan pada list");
        }
        System.out.println();
    }
}
