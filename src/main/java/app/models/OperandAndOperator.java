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

    public int getDistinctOperand(int key){ return listMethodOperand.get(key).size(); }

    public int getDistinctOperator(int key){
        return listMethodOperator.get(key).size();
    }

    public int getTotalOperand(int key){
        return listMethodOperand.get(key).entrySet().stream().mapToInt(Map.Entry::getValue).sum(); }

    public int getTotalOperator(int key){
        return listMethodOperator.get(key).entrySet().stream().mapToInt(Map.Entry::getValue).sum();
    }

    public Map<String, Integer> getlistMethodOperator(int key){
        return this.listMethodOperator.get(key);
    }
    public Map<String, Integer> getlistMethodOperand(int key){
        return this.listMethodOperand.get(key);
    }

    public void clear(){
        this.listMethodOperator.clear();
        this.listMethodOperand.clear();
    }

    public void debug(){
        System.out.println("================= DEBUG FOR CLASS MODEL OperandAndOperator =================");
        MethodProperty methodProperty = MethodProperty.getInstance();
        methodProperty.get().entrySet().forEach(property -> {
            int curKey = property.getKey();
            System.out.println("class name        : " + property.getValue().get(0));
            System.out.println("Method name       : " + property.getValue().get(1));
            System.out.println("source Code       : \n" + property.getValue().get(4));
            System.out.println("disctict operator : " + getDistinctOperator(curKey));
            System.out.println("disctict operand  : " + getDistinctOperand(curKey));
            System.out.println("total operator    : " + getTotalOperator(curKey));
            System.out.println("total operand     : " + getTotalOperand(curKey));
        });
        System.out.println("=============== END DEBUG FOR CLASS MODEL OperandAndOperator ===============");

    }
}
