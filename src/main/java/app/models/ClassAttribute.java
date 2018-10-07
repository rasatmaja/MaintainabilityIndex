package app.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassAttribute {
    private static ClassAttribute instance;
    private ClassAttribute(){};

    Map<Integer, List<String>> listOfClassAttribute = new HashMap<>();
    /**
     * listOfClassAttribute
     * int key
     * [0] name class
     * [1] LOC
     * [2] line of comment
     * [3] source code
     */

    public static synchronized ClassAttribute getInstance() {
        if (instance == null) {
            instance = new ClassAttribute();
        }
        return instance;
    }

    public void set(String className, int lineOfCode, int lineOfComment, String sourceCode){
        List<String> classAttribute = new ArrayList<>();
        classAttribute.add(className);
        classAttribute.add( String.valueOf(lineOfCode) );
        classAttribute.add( String.valueOf(lineOfComment) );
        classAttribute.add(sourceCode);
        listOfClassAttribute.put(listOfClassAttribute.size(), classAttribute);
    }

    public Map<Integer, List<String>> get(){
        return listOfClassAttribute;
    }

    public void clear(){
        listOfClassAttribute.clear();
    }
}
