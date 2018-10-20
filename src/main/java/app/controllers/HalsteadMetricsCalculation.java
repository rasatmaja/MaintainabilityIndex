package app.controllers;

import app.models.HalsteadMetricsResult;
import app.models.OperandAndOperator;

import java.util.ArrayList;
import java.util.Arrays;

public class HalsteadMetricsCalculation {
    private static HalsteadMetricsCalculation instance;
    int methodKey;
    OperandAndOperator operandAndOperator;
    HalsteadMetricsResult halsteadMetricsResult;

    double lengthOfProgram;
    double vocabularyOfProgram;
    double volumeOfProgram;
    double difficulty;
    double effort;
    double bugExpected;

    private HalsteadMetricsCalculation (){}
    public static synchronized HalsteadMetricsCalculation getInstance() {
        if (instance == null) {
            instance = new HalsteadMetricsCalculation();

        }
        return instance;
    }

    public double calculate(int key){
        this.methodKey = key;

        operandAndOperator = OperandAndOperator.getInstance();
        int totalOperator = operandAndOperator.getTotalOperator(this.methodKey);
        int totalOperand = operandAndOperator.getTotalOperand(this.methodKey);
        int distinctOperator = operandAndOperator.getDistinctOperator(this.methodKey);
        int distinctOperand = operandAndOperator.getDistinctOperand(this.methodKey);

        this.lengthOfProgram = totalOperand + totalOperator;
        this.vocabularyOfProgram = distinctOperand + distinctOperator;
        this.volumeOfProgram = this.lengthOfProgram  * (Math.log(this.vocabularyOfProgram)/Math.log(2));
        this.difficulty = (distinctOperator/2) * (totalOperand/distinctOperand);
        this.effort = this.difficulty * this.volumeOfProgram;
        this.bugExpected = this.volumeOfProgram / 3000;

        save();

        return this.volumeOfProgram;
    }

    private void save(){
        halsteadMetricsResult = HalsteadMetricsResult.getInstance();
        halsteadMetricsResult.setMethodPropertyKey(this.methodKey);
        halsteadMetricsResult.set(new ArrayList<>(Arrays.asList(
            this.lengthOfProgram,
            this.vocabularyOfProgram,
            this.volumeOfProgram,
            this.difficulty,
            this.effort,
            this.bugExpected
        )));
    }
}
