package app.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintainabilityIndexResult {
    private static MaintainabilityIndexResult instance;
    private int methodPropertyKey;
    Map<Integer, Double> listOfMaintainanibilityIndez = new HashMap<>();
    Map<String, Double> listOfAvgMaintainabilityIndex = new HashMap<>();

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

    public void setListOfAvgMaintainabilityIndex(String className, double value){
        this.listOfAvgMaintainabilityIndex.put(className, value);
    }

    public Map<Integer, Double> get(){
        return listOfMaintainanibilityIndez;
    }

    public Map<String, Double> getListOfAvgMaintainabilityIndex(){return this.listOfAvgMaintainabilityIndex;}

    public void clear(){
        this.listOfAvgMaintainabilityIndex.clear();
        this.listOfMaintainanibilityIndez.clear();
    }
}
