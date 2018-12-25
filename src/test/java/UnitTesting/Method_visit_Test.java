package UnitTesting;

import app.controllers.ClassAndMethodPropertyExtraction;
import app.models.ClassProperty;
import app.models.MethodProperty;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

//TODO: perlu dibuatkan mock untuk ClassPropert dan Methodroperty

public class Method_visit_Test {
    private String sourceCode;
    private ArrayList<String> expectedResult = new ArrayList<>();
    private ClassAndMethodPropertyExtraction cmpe;
    private ClassProperty cp;
    private MethodProperty mp;

    @Test
    public void jalur1() {
        sourceCode = "abstract class Shape{\n" +
                "    abstract void draw();  \n" +
                "}";

        cp = Mockito.mock(ClassProperty.class);
        mp = Mockito.mock(MethodProperty.class);
        cmpe = new ClassAndMethodPropertyExtraction(cp, mp);

        CompilationUnit cu = JavaParser.parse(sourceCode);
        cmpe.visit(cu, null);

        System.out.println("Hasil Pengujian Jalur 1:");
        Mockito.verify(cp, times( 1 ) ).set(anyString(), anyInt(),
                anyInt(), anyString(),  eq("Abstract"));
        System.out.println("Data Class dengan tipe Abstract berhasil disimpan ");
        System.out.println();

        cp.clear();
    }

    @Test
    public void jalur2() {
        sourceCode = "interface Vehicle { \n" +
                "    void changeGear(int a); \n" +
                "    void speedUp(int a); \n" +
                "    void applyBrakes(int a); \n" +
                "}";
        cp = Mockito.mock(ClassProperty.class);
        mp = Mockito.mock(MethodProperty.class);
        cmpe = new ClassAndMethodPropertyExtraction(cp, mp);

        CompilationUnit cu = JavaParser.parse(sourceCode);
        cmpe.visit(cu, null);

        System.out.println("Hasil Pengujian Jalur 2:");
        Mockito.verify(cp, times( 1 ) ).set(anyString(), anyInt(),
                anyInt(), anyString(),  eq("Interface"));

        System.out.println("Data Class dengan tipe Interface berhasil disimpan ");
        System.out.println();

        cp.clear();
    }

    @Test
    public void jalur3() {
        sourceCode = "bukan bahasa pemrograman java";
        cp = Mockito.mock(ClassProperty.class);
        mp = Mockito.mock(MethodProperty.class);
        cmpe = new ClassAndMethodPropertyExtraction(cp, mp);

        try{
            CompilationUnit cu = JavaParser.parse(sourceCode);
            cmpe.visit(cu, null);
        } catch (Exception e){
        }

        System.out.println("Hasil Pengujian Jalur 3:");
        Mockito.verify(cp, times( 0 ) ).set(anyString(), anyInt(),
                anyInt(), anyString(),  anyString());

        System.out.println("Tidak ada data tersimpan dalam list");
        System.out.println();
        cp.clear();
    }

    @Test
    public void jalur4() {
        sourceCode = "class Mobil{" +"\n" + "} ";
        cp = Mockito.mock(ClassProperty.class);
        mp = Mockito.mock(MethodProperty.class);
        cmpe = new ClassAndMethodPropertyExtraction(cp, mp);

        try{
            CompilationUnit cu = JavaParser.parse(sourceCode);
            cmpe.visit(cu, null);
        } catch (Exception e){
        }

        System.out.println("Hasil Pengujian Jalur 4:");
        Mockito.verify(cp, times( 1 ) ).set(anyString(), anyInt(),
                anyInt(), anyString(),  eq("Concrete"));

        System.out.println("Data Class dengan tipe Concrete berhasil disimpan ");
        System.out.println();
        cp.clear();
    }

    @Test
    public void jalur5() {
        sourceCode = "public class Files {\n" +
                "    public String getFile_name() { \n" +
                "        return file_name.get(); \n" +
                "    }\n" +
                "}";

        cp = Mockito.mock(ClassProperty.class);
        mp = Mockito.mock(MethodProperty.class);
        cmpe = new ClassAndMethodPropertyExtraction(cp, mp);

        try{
            CompilationUnit cu = JavaParser.parse(sourceCode);
            cmpe.visit(cu, null);
        } catch (Exception e){
        }

        System.out.println("Hasil Pengujian Jalur 5:");
        Mockito.verify(cp, times( 1 ) ).set(anyString(), anyInt(),
                anyInt(), anyString(),  eq("Concrete"));
        System.out.println("Data Class dengan tipe Concrete berhasil disimpan ");

        Mockito.verify(mp, times(1)).set(anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString(),
                anyString(), eq("method"), anyString(), anyString());
        System.out.println("Data Method berhasil disimpan");

        System.out.println();
        cp.clear();
    }

    @Test
    public void jalur6() {
        sourceCode = "public class Files {\n" +
                "    public Files (String file_name, String size, String date_modified) {\n" +
                "        this.file_name = new SimpleStringProperty(file_name);\n" +
                "        this.size = new SimpleStringProperty(size);\n" +
                "        this.date_modified = new SimpleStringProperty(date_modified);\n" +
                "    }\n" +
                "}";

        cp = Mockito.mock(ClassProperty.class);
        mp = Mockito.mock(MethodProperty.class);
        cmpe = new ClassAndMethodPropertyExtraction(cp, mp);

        try{
            CompilationUnit cu = JavaParser.parse(sourceCode);
            cmpe.visit(cu, null);
        } catch (Exception e){
        }

        System.out.println("Hasil Pengujian Jalur 6:");
        Mockito.verify(cp, times( 1 ) ).set(anyString(), anyInt(),
                anyInt(), anyString(),  eq("Concrete"));
        System.out.println("Data Class dengan tipe Concrete berhasil disimpan ");

        Mockito.verify(mp, times(1)).set(anyString(), anyString(),
                anyInt(), anyInt(), anyString(), anyString(),
                anyString(), eq("constructor"), anyString(), anyString());
        System.out.println("Data Constructor berhasil disimpan");

        System.out.println();
        cp.clear();
    }
}
