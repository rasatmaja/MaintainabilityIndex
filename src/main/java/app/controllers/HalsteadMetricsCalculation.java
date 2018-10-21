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
        double totalOperator = operandAndOperator.getTotalOperator(this.methodKey);
        double totalOperand = operandAndOperator.getTotalOperand(this.methodKey);
        double distinctOperator = operandAndOperator.getDistinctOperator(this.methodKey);
        double distinctOperand = operandAndOperator.getDistinctOperand(this.methodKey);

        this.lengthOfProgram = totalOperand + totalOperator;
        System.out.println(methodKey + ": " + lengthOfProgram);

        this.vocabularyOfProgram = distinctOperand + distinctOperator;
        System.out.println(methodKey + ": " + vocabularyOfProgram);

        this.volumeOfProgram = (this.lengthOfProgram != 0)? this.lengthOfProgram  * (Math.log(this.vocabularyOfProgram)/Math.log(2)) : 0;
        System.out.println(methodKey + ": " + volumeOfProgram);

        this.difficulty = (distinctOperand != 0 && distinctOperator != 0)? (distinctOperator/2.0) * (totalOperand/distinctOperand) : 0;
        System.out.println(methodKey + ": " + difficulty);

        this.effort = this.difficulty * this.volumeOfProgram;
        System.out.println(methodKey + ": " + effort);

        this.bugExpected = this.volumeOfProgram / 3000;
        System.out.println(methodKey + ": " + bugExpected);

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
