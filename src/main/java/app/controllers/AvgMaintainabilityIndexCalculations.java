package app.controllers;

import app.models.*;

import java.util.List;
import java.util.Map;

public class AvgMaintainabilityIndexCalculations {

    ClassProperty classProperty;
    MethodProperty methodProperty;
    HalsteadMetricsResult halsteadMetricsResult;
    CyclomaticComplexityResult cyclomaticComplexityResult;
    MaintainabilityIndexResult maintainabilityIndexResult;

    public AvgMaintainabilityIndexCalculations(){
        classProperty = ClassProperty.getInstance();
         methodProperty = MethodProperty.getInstance();
         halsteadMetricsResult = HalsteadMetricsResult.getInstance();
         cyclomaticComplexityResult = CyclomaticComplexityResult.getInstance();
         maintainabilityIndexResult = MaintainabilityIndexResult.getInstance();;
    }

    public void calculate(){
        //System.out.println();
        //System.out.println("========================= AVG MI =================================");

        classProperty.get().entrySet().forEach(classData ->{
            int classKey = classData.getKey();
            double totalHV = 0;
            double totalCC = 0;
            int count = 0;

            String className = classData.getValue().get(0);
            //System.out.println("Class Name : " + className);

            for (Map.Entry<Integer, List<String>> methodData : methodProperty.get().entrySet()) {
                String methodClassName = methodData.getValue().get(0);

                if (className.equalsIgnoreCase(methodClassName)) {
                    //System.out.println("Method Class Name : " + methodClassName);
                   // System.out.println("Method Name       : " + methodData.getValue().get(1));

                    int methodKey = methodData.getKey();
                    count ++;
                    totalHV += halsteadMetricsResult.get().get(methodKey).get(2);
                    totalCC += cyclomaticComplexityResult.get().get(methodKey);

                    //System.out.println("method key   : " + methodKey);
                    //System.out.println("HV           : " + halsteadMetricsResult.get().get(methodKey).get(2));
                    //System.out.println("CC           : " + cyclomaticComplexityResult.get().get(methodKey));
                    //System.out.println();
                }
            }

            double avgHV = totalHV / count;
            double avgCC = totalCC / count;
            //System.out.println("Avg HV : " + avgHV);
            //System.out.println("Avg CC : " + avgCC);

            double loc = Double.valueOf(classData.getValue().get(1));
            double perCM = (loc != 0) ? Double.valueOf(classData.getValue().get(2)) / loc : 0;
            //System.out.println("LOC    : " +loc);
            //System.out.println("perCM  : " + perCM);

            double maintainabilityIndex = (loc != 0 && avgHV != 0) ?
                    171 - 5.2 * (Math.log(avgHV)) - (0.23 * avgCC) - (16.2 * Math.log(loc)) + (50 * Math.sin(Math.sqrt(2.46 * perCM)))
                    : 0;
            //System.out.println("MI: " + maintainabilityIndex);
            //System.out.println();
            //System.out.println("\"=========================END AVG MI =================================\"");

            maintainabilityIndexResult.setListOfAvgMaintainabilityIndex(className, maintainabilityIndex);

        });
    }
}
