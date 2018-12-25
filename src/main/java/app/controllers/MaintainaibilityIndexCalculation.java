package app.controllers;

import app.models.MaintainabilityIndexResult;
import app.models.MethodProperty;
import app.models.OperandAndOperator;
import app.models.PredicateNode;
import javafx.concurrent.Task;

import java.text.DecimalFormat;

public class MaintainaibilityIndexCalculation extends Task<Void> {

    private MethodProperty methodProperty;
    private HalsteadMetricsCalculation halsteadMetricsCalculation;
    private CyclomaticComplexityCalculations cyclomaticComplexityCalculations;
    private MaintainabilityIndexResult maintainabilityIndexResult;
    private double resultMI;

    public MaintainaibilityIndexCalculation (){
        methodProperty = MethodProperty.getInstance();
        halsteadMetricsCalculation = HalsteadMetricsCalculation.getInstance();
        cyclomaticComplexityCalculations = CyclomaticComplexityCalculations.getInstance();
        maintainabilityIndexResult = MaintainabilityIndexResult.getInstance();
    }

    public MaintainaibilityIndexCalculation(MethodProperty mp, HalsteadMetricsCalculation hmc,
            CyclomaticComplexityCalculations ccc, MaintainabilityIndexResult mir){
        methodProperty = mp;
        halsteadMetricsCalculation = hmc;
        cyclomaticComplexityCalculations = ccc;
        maintainabilityIndexResult = mir;
    }

    @Override
    protected Void call() {
        updateMessage("Start caculating... ");
        methodProperty.get().entrySet().forEach(method ->{
            int methodKey = method.getKey();

            updateMessage("calculating method: " + method.getValue().get(6));

            double halsteadValume = halsteadMetricsCalculation.calculate(methodKey);
            //System.out.println("HV: " + halsteadValume);

            int cyclomaticComplexity = cyclomaticComplexityCalculations.calclulate(methodKey);
            //System.out.println("CC: " + cyclomaticComplexity);

            int loc = Integer.valueOf(method.getValue().get(2));
            //System.out.println("LOC: " + loc);

            //double perCM = (loc != 0) ? Integer.valueOf(method.getValue().get(3)) / Integer.valueOf(method.getValue().get(2)) : 0;
            double comment = Double.valueOf(methodProperty.get().get(methodKey).get(3));
            double perCM = (loc != 0) ? 100 * comment / loc : 0;
            perCM = Double.parseDouble(new DecimalFormat("0.##").format(perCM));

            double maintainabilityIndex = (loc != 0 && halsteadValume != 0) ?
                                          171 - (5.2 * Math.log(halsteadValume)) - (0.23 * cyclomaticComplexity) - (16.2 * Math.log(loc)) + (50 * Math.sin(Math.toRadians(Math.sqrt(2.46 * perCM))))
                                          : 0;
            //System.out.println("MI: " + maintainabilityIndex);

            maintainabilityIndexResult.setMethodPropertyKey(methodKey);
            maintainabilityIndexResult.set(maintainabilityIndex);

        });

        updateMessage("Calculation is complete ");
        return null;
    }

    public void callTest(){
        methodProperty.get().entrySet().forEach(method ->{
            int methodKey = method.getKey();
            //System.out.println(methodKey);

            double halsteadValume = halsteadMetricsCalculation.calculate(methodKey);
            //System.out.println("HV: " + halsteadValume);

            int cyclomaticComplexity = cyclomaticComplexityCalculations.calclulate(methodKey);
            //System.out.println("CC: " + cyclomaticComplexity);

            int loc = Integer.valueOf(method.getValue().get(2));
            //System.out.println("LOC: " + loc);

            //double perCM = (loc != 0) ? Integer.valueOf(method.getValue().get(3)) / Integer.valueOf(method.getValue().get(2)) : 0;
            double comment = Double.valueOf(methodProperty.get().get(methodKey).get(3));
            double perCM = (loc != 0) ? 100 * comment / loc : 0;
            perCM = Double.parseDouble(new DecimalFormat("0.##").format(perCM));

            double maintainabilityIndex = (loc != 0 && halsteadValume != 0) ?
                    171 - (5.2 * Math.log(halsteadValume)) - (0.23 * cyclomaticComplexity) - (16.2 * Math.log(loc)) + (50 * Math.sin(Math.toRadians(Math.sqrt(2.46 * perCM))))
                    : 0;
            //System.out.println("MI: " + maintainabilityIndex);

            maintainabilityIndexResult.setMethodPropertyKey(methodKey);
            maintainabilityIndexResult.set(maintainabilityIndex);

            this.resultMI = maintainabilityIndex;

        });
    }

    public double getResultMI() {
        return this.resultMI;
    }
}
