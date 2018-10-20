package app.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HalsteadMetricsResult {
    private static HalsteadMetricsResult instance;
    private int methodPropertyKey;
    Map<Integer, List<Double>> listOfHalsteadMetrics = new HashMap<>();
    private HalsteadMetricsResult(){}

    public static synchronized HalsteadMetricsResult getInstance() {
        if (instance == null) {
            instance = new HalsteadMetricsResult();
        }
        return instance;
    }

    public void setMethodPropertyKey(int key){
        this.methodPropertyKey = key;
    }

    public void set(List<Double> halsteadMeric){
        listOfHalsteadMetrics.put(methodPropertyKey, halsteadMeric);
    }
}
