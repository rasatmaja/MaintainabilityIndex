package app.models;

import java.util.HashMap;
import java.util.Map;

public class PredicateNode {
    private static PredicateNode instance;
    private int methodPropertyKey;
    Map<Integer, Integer> listPredicateNode = new HashMap<>();

    public static PredicateNode getInstance() {
        if (instance == null) {
            instance = new PredicateNode();
        }
        return instance;
    }

    private PredicateNode() {
    }

    public void setMethodPropertyKey(int key){
        this.methodPropertyKey = key;

    }

    public void setPredicateNode(int numberOfPredicateNode){
        this.listPredicateNode.put(this.methodPropertyKey, numberOfPredicateNode);
    }

    public int getPredicateNode(int key){
        return listPredicateNode.get(key);
    }
}
