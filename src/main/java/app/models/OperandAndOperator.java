package app.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperandAndOperator {
    private  static OperandAndOperator instance;
    private int methodPropertyKey;

    Map<Integer, Map<String, Integer>> listMethodOperand = new HashMap<>();
    Map<Integer, Map<String, Integer>> listMethodOperator = new HashMap<>();

    private OperandAndOperator(){};
    public static synchronized OperandAndOperator getInstance() {
        if (instance == null) {
            instance = new OperandAndOperator();
        }
        return instance;
    }

    public void setMethodPropertyKey(int key){
        this.methodPropertyKey = key;
    }

    public void setlistMethodOperand (Map<String, Integer> listOperand){
        this.listMethodOperand.put(this.methodPropertyKey, listOperand);
    }

    public void setlistMethodOperator (Map<String, Integer> lisOperator){
        this.listMethodOperator.put(this.methodPropertyKey, lisOperator);
    }

    public int getDistinctOperand(int key){
        return 0;
    }

    public int getDistinctOperator(int key){
        return 0;
    }

    public int getTotalOperand(int key){
        return 0;
    }

    public int getTotalOperator(int key){
        return 0;
    }
}
