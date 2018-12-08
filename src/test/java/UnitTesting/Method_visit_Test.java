package UnitTesting;

import app.controllers.ClassAndMethodPropertyExtraction;
import app.models.ClassProperty;
import app.models.MethodProperty;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

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
                "} ";
        expectedResult.add("Shape");
        expectedResult.add("1");
        expectedResult.add("0");
        expectedResult.add(sourceCode);
        expectedResult.add("Abstract");

        cmpe = new ClassAndMethodPropertyExtraction();
        cp = ClassProperty.getInstance();

        CompilationUnit cu = JavaParser.parse(sourceCode);
        cmpe.visit(cu, null);

        System.out.println("Hasil Pengujian Jalur 1:");
        cp.get().entrySet().forEach(classProperty -> {
            assertEquals(expectedResult.get(0), classProperty.getValue().get(0));
            assertEquals(expectedResult.get(1), classProperty.getValue().get(1));
            assertEquals(expectedResult.get(2), classProperty.getValue().get(2));
            //assertEquals(expectedResult.get(3), classProperty.getValue().get(3));
            assertEquals(expectedResult.get(4), classProperty.getValue().get(4));

            System.out.println("Data Class " + classProperty.getValue().get(0) +
                    " dengan tipe " + classProperty.getValue().get(4) +" berhasil disimpan");
        });
        System.out.println();

        cp.clear();
        expectedResult.clear();
    }

    @Test
    public void jalur2() {
        sourceCode = "interface Vehicle { \n" +
                "    void changeGear(int a); \n" +
                "    void speedUp(int a); \n" +
                "    void applyBrakes(int a); \n" +
                "}";
        expectedResult.add("Vehicle");
        expectedResult.add("3");
        expectedResult.add("0");
        expectedResult.add(sourceCode);
        expectedResult.add("Interface");

        cmpe = new ClassAndMethodPropertyExtraction();
        cp = ClassProperty.getInstance();

        CompilationUnit cu = JavaParser.parse(sourceCode);
        cmpe.visit(cu, null);

        System.out.println("Hasil Pengujian Jalur 2:");
        cp.get().entrySet().forEach(classProperty -> {
            assertEquals(expectedResult.get(0), classProperty.getValue().get(0));
            assertEquals(expectedResult.get(1), classProperty.getValue().get(1));
            assertEquals(expectedResult.get(2), classProperty.getValue().get(2));
            //assertEquals(expectedResult.get(3), classProperty.getValue().get(3));
            assertEquals(expectedResult.get(4), classProperty.getValue().get(4));

            System.out.println("Data Class " + classProperty.getValue().get(0) +
                    " dengan tipe " + classProperty.getValue().get(4) +" berhasil disimpan");
        });
        System.out.println();

        cp.clear();
        expectedResult.clear();
    }

    @Test
    public void jalur3() {
        sourceCode = "bukan bahasa pemrograman java";

        cmpe = new ClassAndMethodPropertyExtraction();
        cp = ClassProperty.getInstance();
        try{
            CompilationUnit cu = JavaParser.parse(sourceCode);
            cmpe.visit(cu, null);
        } catch (Exception e){
        }

        System.out.println("Hasil Pengujian Jalur 3:");
        assertEquals(0, cp.get().size());
        if (cp.get().size()==0){
            System.out.println("Tidak ada data tersimpan dalam list");
        }
        System.out.println();

        cp.clear();
        expectedResult.clear();
    }

    @Test
    public void jalur4() {
        sourceCode = "class Mobil{" +"\n" + "} ";

        expectedResult.add("Mobil");
        expectedResult.add("0");
        expectedResult.add("0");
        expectedResult.add(sourceCode);
        expectedResult.add("Concrete");

        cmpe = new ClassAndMethodPropertyExtraction();
        cp = ClassProperty.getInstance();
        try{
            CompilationUnit cu = JavaParser.parse(sourceCode);
            cmpe.visit(cu, null);
        } catch (Exception e){
        }

        assertEquals(1, cp.get().size());
        System.out.println("Hasil Pengujian Jalur 4:");
        cp.get().entrySet().forEach(classProperty -> {
            assertEquals(expectedResult.get(0), classProperty.getValue().get(0));
            assertEquals(expectedResult.get(1), classProperty.getValue().get(1));
            assertEquals(expectedResult.get(2), classProperty.getValue().get(2));
            //assertEquals(expectedResult.get(3), classProperty.getValue().get(3));
            assertEquals(expectedResult.get(4), classProperty.getValue().get(4));

            System.out.println("Data Class " + classProperty.getValue().get(0) +
                    " dengan tipe " + classProperty.getValue().get(4) +" berhasil disimpan");
        });
        System.out.println();

        cp.clear();
        expectedResult.clear();
    }

    //TODO: membuat jalur 5 dan 6
    @Test
    public void jalur5() {
        sourceCode = "public class Files {\n" +
                "    public String getFile_name() { \n" +
                "        return file_name.get(); \n" +
                "    }\n" +
                "}";

        expectedResult.add("Files");
        expectedResult.add("3");
        expectedResult.add("0");
        expectedResult.add(sourceCode);
        expectedResult.add("Concrete");

        cmpe = new ClassAndMethodPropertyExtraction();
        cp = ClassProperty.getInstance();
        mp = MethodProperty.getInstance();

        try{
            CompilationUnit cu = JavaParser.parse(sourceCode);
            cmpe.visit(cu, null);
        } catch (Exception e){
        }

        assertEquals(1, cp.get().size());
        System.out.println("Hasil Pengujian Jalur 5:");
        cp.get().entrySet().forEach(classProperty -> {
            assertEquals(expectedResult.get(0), classProperty.getValue().get(0));
            assertEquals(expectedResult.get(1), classProperty.getValue().get(1));
            assertEquals(expectedResult.get(2), classProperty.getValue().get(2));
            //assertEquals(expectedResult.get(3), classProperty.getValue().get(3));
            assertEquals(expectedResult.get(4), classProperty.getValue().get(4));

            System.out.println("Data Class " + classProperty.getValue().get(0) +
                    " dengan tipe " + classProperty.getValue().get(4) +" berhasil disimpan");
        });

        mp.get().entrySet().forEach(methodProperty -> {
            assertEquals("Files", methodProperty.getValue().get(0));
            assertEquals("String getFile_name()", methodProperty.getValue().get(1));
            assertEquals("1", methodProperty.getValue().get(2));
            assertEquals("0", methodProperty.getValue().get(3));
            System.out.println("Data Method " + methodProperty.getValue().get(1) + " berhasil disimpan");
        });
        System.out.println();

        cp.clear();
        expectedResult.clear();
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

        cmpe = new ClassAndMethodPropertyExtraction();
        cp = ClassProperty.getInstance();
        mp = MethodProperty.getInstance();

        try{
            CompilationUnit cu = JavaParser.parse(sourceCode);
            cmpe.visit(cu, null);
        } catch (Exception e){
        }

        assertEquals(1, cp.get().size());
        System.out.println("Hasil Pengujian Jalur 6:");
        cp.get().entrySet().forEach(classProperty -> {
            assertEquals("Files", classProperty.getValue().get(0));
            assertEquals("5", classProperty.getValue().get(1));
            assertEquals("0", classProperty.getValue().get(2));
            //assertEquals(expectedResult.get(3), classProperty.getValue().get(3));
            assertEquals("Concrete", classProperty.getValue().get(4));

            System.out.println("Data Class " + classProperty.getValue().get(0) +
                    " dengan tipe " + classProperty.getValue().get(4) +" berhasil disimpan");
        });

        mp.get().entrySet().forEach(methodProperty -> {
            assertEquals("Files", methodProperty.getValue().get(0));
            assertEquals("Files(String, String, String)", methodProperty.getValue().get(1));
            assertEquals("3", methodProperty.getValue().get(2));
            assertEquals("0", methodProperty.getValue().get(3));
            System.out.println("Data Contructor " + methodProperty.getValue().get(1) + " berhasil disimpan");
        });
        System.out.println();

        cp.clear();
        expectedResult.clear();
    }
}
