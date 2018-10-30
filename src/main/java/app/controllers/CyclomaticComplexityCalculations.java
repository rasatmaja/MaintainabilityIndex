package app.controllers;

import app.models.ClassProperty;
import app.models.CyclomaticComplexityResult;
import app.models.MethodProperty;
import app.models.PredicateNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CyclomaticComplexityCalculations {
    private static CyclomaticComplexityCalculations instance;
    int methodKey;
    int cyclomaticComplexity;
    PredicateNode predicateNode;
    CyclomaticComplexityResult cyclomaticComplexityResult;

    ClassProperty classProperty;
    MethodProperty methodProperty;

    private CyclomaticComplexityCalculations(){};
    public static synchronized CyclomaticComplexityCalculations getInstance(){
        if (instance == null) {
            instance = new CyclomaticComplexityCalculations();

        }
        return instance;
    }

    public int calclulate(int key){
        this.methodKey = key;
        predicateNode = PredicateNode.getInstance();
        this.cyclomaticComplexity = predicateNode.getPredicateNode(this.methodKey) + 1;

        save();
        return this.cyclomaticComplexity;
    }

    public void calculateAvg(){
        classProperty = ClassProperty.getInstance();
        methodProperty = MethodProperty.getInstance();
        cyclomaticComplexityResult = CyclomaticComplexityResult.getInstance();

        classProperty.get().entrySet().forEach( classData -> {
            int classKey = classData.getKey();
            String className = classData.getValue().get(0);
            double cyclomaticComplexity = 0;
            int count=0;

            for (Map.Entry<Integer, List<String>> methodData : methodProperty.get().entrySet()) {
                String methodClassName = methodData.getValue().get(0);
                if (methodClassName.equalsIgnoreCase(className)){
                    int methodKey = methodData.getKey();
                    count++;

                    cyclomaticComplexity = cyclomaticComplexityResult.get().get(methodKey);

                    System.out.println("Method Class Name : " + methodClassName);
                    System.out.println("Method Name       : " + methodData.getValue().get(1));
                    System.out.println("Cyclomatoc        : " + cyclomaticComplexity);
                    System.out.println();

                }
            }

            double avgCyclomaticComplexity = cyclomaticComplexity / count;

            System.out.println("========================== DEBUG AVG CC ==========================");
            System.out.println("Class Name : " + className);
            System.out.println("AVG Length : " + avgCyclomaticComplexity);

            System.out.println("======================== END DEBUG AVG CC ========================");

            cyclomaticComplexityResult.setListOfAvgCyclomaticComplexity(className, avgCyclomaticComplexity);

        });
    }

    private void save(){
        cyclomaticComplexityResult = CyclomaticComplexityResult.getInstance();
        cyclomaticComplexityResult.setMethodPropertyKey(methodKey);
        cyclomaticComplexityResult.set(this.cyclomaticComplexity);
    }
}

