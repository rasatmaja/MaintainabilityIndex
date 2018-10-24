package app.controllers;

import app.models.MaintainabilityIndexResult;
import app.models.MethodProperty;
import app.models.OperandAndOperator;
import app.models.PredicateNode;
import javafx.concurrent.Task;

public class MaintainaibilityIndexCalculation extends Task<Void> {

    MethodProperty methodProperty;
    OperandAndOperator operandAndOperator;
    PredicateNode predicateNode;

    HalsteadMetricsCalculation halsteadMetricsCalculation;
    CyclomaticComplexityCalculations cyclomaticComplexityCalculations;
    MaintainabilityIndexResult maintainabilityIndexResult;

    public MaintainaibilityIndexCalculation (){
        methodProperty = MethodProperty.getInstance();
        operandAndOperator = OperandAndOperator.getInstance();
        predicateNode = PredicateNode.getInstance();

        halsteadMetricsCalculation = HalsteadMetricsCalculation.getInstance();
        cyclomaticComplexityCalculations = CyclomaticComplexityCalculations.getInstance();
        maintainabilityIndexResult = MaintainabilityIndexResult.getInstance();
    }

    @Override
    protected Void call() {
        updateMessage("Start caculating... ");

        methodProperty.get().entrySet().forEach(method ->{
            int methodKey = method.getKey();

            updateMessage("calculating method: " + method.getValue().get(1));

            double halsteadValume = halsteadMetricsCalculation.calculate(methodKey);
            System.out.println("HV: " + halsteadValume);

            int cyclomaticComplexity = cyclomaticComplexityCalculations.calclulate(methodKey);
            System.out.println("CC: " + cyclomaticComplexity);

            int loc = Integer.valueOf(method.getValue().get(2));
            System.out.println("LOC: " + loc);

            double perCM = (loc != 0) ? Integer.valueOf(method.getValue().get(3)) / Integer.valueOf(method.getValue().get(2)) : 0;
            System.out.println("perCM: " + perCM);

            double maintainabilityIndex = (loc != 0 && halsteadValume != 0) ?
                                          171 - 5.2 * (Math.log(halsteadValume)) - (0.23 * cyclomaticComplexity) - (16.2 * Math.log(loc)) + (50 * Math.sin(Math.sqrt(2.46 * perCM)))
                                          : 0;
            System.out.println("MI: " + maintainabilityIndex);

            maintainabilityIndexResult.setMethodPropertyKey(methodKey);
            maintainabilityIndexResult.set(maintainabilityIndex);

        });

        updateMessage("Calculation is complete ");
        return null;
    }



}
