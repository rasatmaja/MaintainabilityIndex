package app.controllers;

import app.models.ClassProperty;
import app.models.HalsteadMetricsResult;
import app.models.MethodProperty;
import app.models.OperandAndOperator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HalsteadMetricsCalculation {
    private static HalsteadMetricsCalculation instance;
    int methodKey;
    OperandAndOperator operandAndOperator;
    HalsteadMetricsResult halsteadMetricsResult;

    ClassProperty classProperty;
    MethodProperty methodProperty;

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
        halsteadMetricsResult = HalsteadMetricsResult.getInstance();
        classProperty = ClassProperty.getInstance();
        methodProperty = MethodProperty.getInstance();

        double totalOperator = operandAndOperator.getTotalOperator(this.methodKey);
        double totalOperand = operandAndOperator.getTotalOperand(this.methodKey);
        double distinctOperator = operandAndOperator.getDistinctOperator(this.methodKey);
        double distinctOperand = operandAndOperator.getDistinctOperand(this.methodKey);

        this.lengthOfProgram = totalOperand + totalOperator;
        //System.out.println(methodKey + ": " + lengthOfProgram);

        this.vocabularyOfProgram = distinctOperand + distinctOperator;
        //System.out.println(methodKey + ": " + vocabularyOfProgram);

        this.volumeOfProgram = (this.lengthOfProgram != 0)? this.lengthOfProgram  * (Math.log(this.vocabularyOfProgram)/Math.log(2)) : 0;
        //System.out.println(methodKey + ": " + volumeOfProgram);

        this.difficulty = (distinctOperand != 0 && distinctOperator != 0)? (distinctOperator/2.0) * (totalOperand/distinctOperand) : 0;
        //System.out.println(methodKey + ": " + difficulty);

        this.effort = this.difficulty * this.volumeOfProgram;
        //System.out.println(methodKey + ": " + effort);

        this.bugExpected = this.volumeOfProgram / 3000;
        //System.out.println(methodKey + ": " + bugExpected);

        save();

        return Double.parseDouble(new DecimalFormat("0.##").format(this.volumeOfProgram));
    }

    public void calculateAvg(){
        halsteadMetricsResult = HalsteadMetricsResult.getInstance();
        classProperty = ClassProperty.getInstance();
        methodProperty = MethodProperty.getInstance();

        classProperty.get().entrySet().forEach( classData -> {
            int classKey = classData.getKey();
            String className = classData.getValue().get(0);
            double lengthOfProgram = 0;
            double vocabularyOfProgram = 0;
            double volumeOfProgram = 0;
            double difficulty = 0;
            double effort = 0 ;
            double bugExpected = 0;

            int count=0;

            for (Map.Entry<Integer, List<String>> methodData : methodProperty.get().entrySet()) {
                String methodClassName = methodData.getValue().get(0);
                if (methodClassName.equalsIgnoreCase(className)){
                    int methodKey = methodData.getKey();
                    count++;

                    lengthOfProgram += halsteadMetricsResult.get().get(methodKey).get(0);
                    vocabularyOfProgram += halsteadMetricsResult.get().get(methodKey).get(1);
                    volumeOfProgram += halsteadMetricsResult.get().get(methodKey).get(2);
                    difficulty += halsteadMetricsResult.get().get(methodKey).get(3);
                    effort += halsteadMetricsResult.get().get(methodKey).get(4);
                    bugExpected += halsteadMetricsResult.get().get(methodKey).get(5);

                    //System.out.println("Method Class Name : " + methodClassName);
                    //System.out.println("Method Name       : " + methodData.getValue().get(1));
                    //System.out.println("AVG Length        : " + lengthOfProgram);
                    //System.out.println("AVG Vocab         : " + vocabularyOfProgram);
                    //System.out.println("AVG Volume        : " + volumeOfProgram);
                    //System.out.println("AVG Diffi         : " + difficulty);
                    //System.out.println("AVG Effort        : " + effort);
                    //System.out.println("AVG Bug           : " + bugExpected);
                    //System.out.println();

                }
            }

            double avgLengthOfProgram = lengthOfProgram / count;
            double avgVocabularyOfProgram = vocabularyOfProgram / count;
            double avgVolumeOfProgram = volumeOfProgram / count;
            double avgDifficulty =difficulty / count;
            double avgEffort = effort / count;
            double avgBugExpected = bugExpected / count;

            //System.out.println("========================== DEBUG AVG HALSTEAD ==========================");
            //System.out.println("Class Name : " + className);
            //System.out.println("AVG Length : " + avgLengthOfProgram);
            //System.out.println("AVG Vocab  : " + avgVocabularyOfProgram);
            //System.out.println("AVG Volume : " + avgVolumeOfProgram);
            //System.out.println("AVG Diffi  : " + avgDifficulty);
            //System.out.println("AVG Effort : " + avgEffort);
            //System.out.println("AVG Bug    : " + avgBugExpected);
            //System.out.println("======================== END DEBUG AVG HALSTEAD ========================");

            halsteadMetricsResult.setListOfAvgHalsteadMetric(className, new ArrayList<>(Arrays.asList(
                    avgLengthOfProgram,
                    avgVocabularyOfProgram,
                    avgVolumeOfProgram,
                    avgDifficulty,
                    avgEffort,
                    avgBugExpected
            )));

        });
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
