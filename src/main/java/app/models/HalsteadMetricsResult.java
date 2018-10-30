package app.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HalsteadMetricsResult {
    private static HalsteadMetricsResult instance;
    private int methodPropertyKey;
    Map<Integer, List<Double>> listOfHalsteadMetrics = new HashMap<>();
    Map<String, List<Double>> listOfAvgHalsteadMetric = new HashMap<>();

    private HalsteadMetricsResult(){}

    public static synchronized HalsteadMetricsResult getInstance() {
        if (instance == null) {
            instance = new HalsteadMetricsResult();
        }
        return instance;
    }

    /**
     *
     * @param key
     *
     * [0] length of program
     * [1] vocabulary
     * [2] volume
     * [3] difficulty
     * [4] effort
     * [5] bug
     */
    public void setMethodPropertyKey(int key){
        this.methodPropertyKey = key;
    }

    public void set(List<Double> halsteadMeric){
        listOfHalsteadMetrics.put(methodPropertyKey, halsteadMeric);
    }

    public Map<Integer, List<Double>> get(){return this.listOfHalsteadMetrics;};

    public void setListOfAvgHalsteadMetric(String className, List<Double> halsteadMeric){
        listOfAvgHalsteadMetric.put(className, halsteadMeric);
    }

    public  Map<String, List<Double>> getListOfAvgHalsteadMetric(){
        return this.listOfAvgHalsteadMetric;
    }
}
