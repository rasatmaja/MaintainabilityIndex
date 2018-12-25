package UnitTesting;

import app.controllers.CyclomaticComplexityCalculations;
import app.controllers.HalsteadMetricsCalculation;
import app.controllers.MaintainaibilityIndexCalculation;
import app.models.MaintainabilityIndexResult;
import app.models.MethodProperty;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Method_call_Test {
    /*
    * pada jalur ini memastikan bahawa ketika method property = 0
    * */
    @Test
    public void jalur1() {
        Map<Integer, List<String>> lifOfMethodProperty = new HashMap<>();

        MaintainabilityIndexResult mir = Mockito.mock(MaintainabilityIndexResult.class);
        MethodProperty mp = Mockito.mock(MethodProperty.class);
        HalsteadMetricsCalculation hmc = Mockito.mock(HalsteadMetricsCalculation.class);
        CyclomaticComplexityCalculations ccc = Mockito.mock(CyclomaticComplexityCalculations.class);

        Mockito.when(mp.get()).thenReturn(lifOfMethodProperty);
        Mockito.when(hmc.calculate(Mockito.anyInt())).thenReturn(0.0);
        Mockito.when(ccc.calclulate(Mockito.anyInt())).thenReturn(0);

        MaintainaibilityIndexCalculation mic = new MaintainaibilityIndexCalculation(mp, hmc, ccc, mir);
        mic.callTest();

        System.out.println("Hasil Pengujian Jalur 1:");

        assertEquals("0.0", String.valueOf(mic.getResultMI()));
        Mockito.verify(mir, Mockito.times(0)).set(0.0);

        if (mic.getResultMI() == 0.0){
            System.out.println("Tidak ada nilai Maintainability Index yang tersimpan di list");
        }

        mp.clear();
        mir.clear();
    }
    @Test
    public void jalur2() {

        Map<Integer, List<String>> lifOfMethodProperty = new HashMap<>();
        lifOfMethodProperty.put(0, new ArrayList<String>() {{
            add("Test Jalur 2");
            add("");
            add("0");
            add("0");
        }});

        MaintainabilityIndexResult mir = Mockito.mock(MaintainabilityIndexResult.class);
        MethodProperty mp = Mockito.mock(MethodProperty.class);
        HalsteadMetricsCalculation hmc = Mockito.mock(HalsteadMetricsCalculation.class);
        CyclomaticComplexityCalculations ccc = Mockito.mock(CyclomaticComplexityCalculations.class);

        Mockito.when(mp.get()).thenReturn(lifOfMethodProperty);
        Mockito.when(hmc.calculate(Mockito.anyInt())).thenReturn(6.34);
        Mockito.when(ccc.calclulate(Mockito.anyInt())).thenReturn(0);
        MaintainaibilityIndexCalculation mic = new MaintainaibilityIndexCalculation(mp, hmc, ccc, mir);
        mic.callTest();

        System.out.println("Hasil Pengujian Jalur 2:");
        assertEquals("0.0", String.valueOf(mic.getResultMI()));
        Mockito.verify(mir, Mockito.times(1)).set(0.0);

        if (mic.getResultMI() == 0.0){
            System.out.println("Nilai Maintainability Index = " + mic.getResultMI());
        }

        mp.clear();
        mir.clear();
    }

    @Test
    public void jalur3() {
        Map<Integer, List<String>> lifOfMethodProperty = new HashMap<>();
        lifOfMethodProperty.put(0, new ArrayList<String>() {{
            add("Test Jalur 3");
            add("");
            add("1");
            add("1");
        }});

        MethodProperty mp = Mockito.mock(MethodProperty.class);
        HalsteadMetricsCalculation hmc = Mockito.mock(HalsteadMetricsCalculation.class);
        CyclomaticComplexityCalculations ccc = Mockito.mock(CyclomaticComplexityCalculations.class);

        MaintainabilityIndexResult mir = Mockito.mock(MaintainabilityIndexResult.class);
        Mockito.when(mp.get()).thenReturn(lifOfMethodProperty);
        Mockito.when(hmc.calculate(Mockito.anyInt())).thenReturn(0.0);
        Mockito.when(ccc.calclulate(Mockito.anyInt())).thenReturn(0);

        MaintainaibilityIndexCalculation mic = new MaintainaibilityIndexCalculation(mp, hmc, ccc, mir);
        mic.callTest();

        System.out.println("Hasil Pengujian Jalur 3:");

        assertEquals("0.0", String.valueOf(mic.getResultMI()));
        Mockito.verify(mir, Mockito.times(1)).set(0.0);

        if (mic.getResultMI() == 0.0){
            System.out.println("Nilai Maintainability Index = " + mic.getResultMI());
        }

        mp.clear();
        mir.clear();
    }

    @Test
    public void jalur4() {

        Map<Integer, List<String>> lifOfMethodProperty = new HashMap<>();
        lifOfMethodProperty.put(0, new ArrayList<String>() {{
            add("Test Jalur 4");
            add("");
            add("1");
            add("0");
        }});

        MaintainabilityIndexResult mir = Mockito.mock(MaintainabilityIndexResult.class);
        MethodProperty mp = Mockito.mock(MethodProperty.class);
        HalsteadMetricsCalculation hmc = Mockito.mock(HalsteadMetricsCalculation.class);
        CyclomaticComplexityCalculations ccc = Mockito.mock(CyclomaticComplexityCalculations.class);

        Mockito.when(mp.get()).thenReturn(lifOfMethodProperty);
        Mockito.when(hmc.calculate(Mockito.anyInt())).thenReturn(6.34);
        Mockito.when(ccc.calclulate(Mockito.anyInt())).thenReturn(0);

        MaintainaibilityIndexCalculation mic = new MaintainaibilityIndexCalculation(mp, hmc, ccc, mir);
        mic.callTest();

        System.out.println("Hasil Pengujian Jalur 4:");
        assertEquals("161.3962304040645", String.valueOf(mic.getResultMI()));
        Mockito.verify(mir, Mockito.times(1)).set(161.3962304040645);

        if (mic.getResultMI() == 161.3962304040645){
            System.out.println("Nilai Maintainability Index = " + mic.getResultMI());
        }

        mp.clear();
        mir.clear();
    }
}
