package UnitTesting;

import app.controllers.CyclomaticComplexityCalculations;
import app.controllers.HalsteadMetricsCalculation;
import app.controllers.MaintainaibilityIndexCalculation;
import app.models.MaintainabilityIndexResult;
import app.models.MethodProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;

public class Method_call_Test {

    @Before
    public void setUp() {

    }

    /*
    * pada jalur ini memastikan bahawa ketika method property = 0
    * */
    @Test
    public void jalur1() {
        MaintainabilityIndexResult mir = MaintainabilityIndexResult.getInstance();

        MethodProperty mp = MethodProperty.getInstance();
        HalsteadMetricsCalculation hmc = Mockito.mock(HalsteadMetricsCalculation.class);
        CyclomaticComplexityCalculations ccc = Mockito.mock(CyclomaticComplexityCalculations.class);

        Mockito.when(hmc.calculate(Mockito.anyInt())).thenReturn(0.0);
        Mockito.when(ccc.calclulate(Mockito.anyInt())).thenReturn(0);
        MaintainaibilityIndexCalculation mic = new MaintainaibilityIndexCalculation(mp, hmc, ccc);
        mic.callTest();

        System.out.println("Hasil Pengujian Jalur 1:");
        assertEquals(0, mir.get().size());
        if (mir.get().size() == 0){
            System.out.println("Tidak ada nilai Maintainability Index yang tersimpan di list");
        }

        mp.clear();
        mir.clear();
    }
    @Test
    public void jalur2() {
        MaintainabilityIndexResult mir = MaintainabilityIndexResult.getInstance();

        MethodProperty mp = MethodProperty.getInstance();
        mp.set("Test1", "", 0, 0, "",
                "", "", "", "", "");

        HalsteadMetricsCalculation hmc = Mockito.mock(HalsteadMetricsCalculation.class);
        CyclomaticComplexityCalculations ccc = Mockito.mock(CyclomaticComplexityCalculations.class);

        Mockito.when(hmc.calculate(Mockito.anyInt())).thenReturn(6.34);
        Mockito.when(ccc.calclulate(Mockito.anyInt())).thenReturn(0);
        MaintainaibilityIndexCalculation mic = new MaintainaibilityIndexCalculation(mp, hmc, ccc);
        mic.callTest();

        System.out.println("Hasil Pengujian Jalur 2:");
        assertEquals(0, mir.get().get(0).intValue());

        if (mir.get().size() == 1){
            System.out.println("Nilai Maintainability Index = " + mir.get().get(0));
        }

        mp.clear();
        mir.clear();
    }

    @Test
    public void jalur3() {
        MaintainabilityIndexResult mir = MaintainabilityIndexResult.getInstance();

        MethodProperty mp = MethodProperty.getInstance();
        mp.set("Test1", "", 1, 1, "",
                "", "", "", "", "");

        HalsteadMetricsCalculation hmc = Mockito.mock(HalsteadMetricsCalculation.class);
        CyclomaticComplexityCalculations ccc = Mockito.mock(CyclomaticComplexityCalculations.class);

        Mockito.when(hmc.calculate(Mockito.anyInt())).thenReturn(0.0);
        Mockito.when(ccc.calclulate(Mockito.anyInt())).thenReturn(0);
        MaintainaibilityIndexCalculation mic = new MaintainaibilityIndexCalculation(mp, hmc, ccc);
        mic.callTest();

        System.out.println("Hasil Pengujian Jalur 3:");
        assertEquals(0, mir.get().get(0).intValue());

        if (mir.get().size() == 1){
            System.out.println("Nilai Maintainability Index = " + mir.get().get(0));
        }

        mp.clear();
        mir.clear();
    }

    @Test
    public void jalur4() {
        MaintainabilityIndexResult mir = MaintainabilityIndexResult.getInstance();

        MethodProperty mp = MethodProperty.getInstance();
        mp.set("Test1", "", 1, 0, "",
                "", "", "", "", "");

        HalsteadMetricsCalculation hmc = Mockito.mock(HalsteadMetricsCalculation.class);
        CyclomaticComplexityCalculations ccc = Mockito.mock(CyclomaticComplexityCalculations.class);

        Mockito.when(hmc.calculate(Mockito.anyInt())).thenReturn(6.34);
        Mockito.when(ccc.calclulate(Mockito.anyInt())).thenReturn(0);

        MaintainaibilityIndexCalculation mic = new MaintainaibilityIndexCalculation(mp, hmc, ccc);
        mic.callTest();

        System.out.println("Hasil Pengujian Jalur 4:");
        assertEquals(161, mir.get().get(0).intValue());

        if (mir.get().size() == 1){
            System.out.println("Nilai Maintainability Index = " + mir.get().get(0));
        }

        mp.clear();
        mir.clear();
    }
}
