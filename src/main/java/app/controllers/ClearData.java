package app.controllers;

import app.models.*;

import java.lang.reflect.Method;

public class ClearData {
    ClassProperty classProperty;
    PredicateNode predicateNode;
    MethodProperty  methodProperty;
    ClassEdgeProperty classEdgeProperty;
    OperandAndOperator operandAndOperator;
    HalsteadMetricsResult halsteadMetricsResult;
    CyclomaticComplexityResult cyclomaticComplexityResult;
    MaintainabilityIndexResult maintainabilityIndexResult;


    public ClearData(){
        classProperty= ClassProperty.getInstance();
        predicateNode = PredicateNode.getInstance();
        methodProperty = MethodProperty.getInstance();
        classEdgeProperty = ClassEdgeProperty.getInstance();
        operandAndOperator = OperandAndOperator.getInstance();
        halsteadMetricsResult = HalsteadMetricsResult.getInstance();
        cyclomaticComplexityResult = CyclomaticComplexityResult.getInstance();
        maintainabilityIndexResult = MaintainabilityIndexResult.getInstance();
    }

    public void execute(){
        classProperty.clear();
        predicateNode.clear();
        methodProperty.clear();
        classEdgeProperty.clear();
        operandAndOperator.clear();
        halsteadMetricsResult.clear();
        cyclomaticComplexityResult.clear();
        maintainabilityIndexResult.clear();
    }
}
