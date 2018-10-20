package app.controllers;

import app.models.CyclomaticComplexityResult;
import app.models.PredicateNode;

public class CyclomaticComplexityCalculations {
    private static CyclomaticComplexityCalculations instance;
    int methodKey;
    int cyclomaticComplexity;
    PredicateNode predicateNode;
    CyclomaticComplexityResult cyclomaticComplexityResult;

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

    private void save(){
        cyclomaticComplexityResult = CyclomaticComplexityResult.getInstance();
        cyclomaticComplexityResult.setMethodPropertyKey(methodKey);
        cyclomaticComplexityResult.set(this.cyclomaticComplexity);
    }
}

