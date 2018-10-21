package app.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintainabilityIndexResult {
    private static MaintainabilityIndexResult instance;
    private int methodPropertyKey;
    Map<Integer, Double> listOfMaintainanibilityIndez = new HashMap<>();

    private MaintainabilityIndexResult(){}
    public static synchronized MaintainabilityIndexResult getInstance() {
        if (instance == null) {
            instance = new MaintainabilityIndexResult();
        }
        return instance;
    }

    public void setMethodPropertyKey(int key){
        this.methodPropertyKey = key;
    }

    public void set(double maintainabilitiIndex){
        listOfMaintainanibilityIndez.put(this.methodPropertyKey, maintainabilitiIndex);
    }

    public Map<Integer, Double> get(){
        return listOfMaintainanibilityIndez;
    }
}
