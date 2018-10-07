package app.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassProperty {
    private static ClassProperty instance;
    private ClassProperty(){};

    Map<Integer, List<String>> listOfClassProperty = new HashMap<>();
    /**
     * listOfClassProperty
     * int key
     * [0] name class
     * [1] LOC
     * [2] line of comment
     * [3] source code
     * [4] class type
     */

    public static synchronized ClassProperty getInstance() {
        if (instance == null) {
            instance = new ClassProperty();
        }
        return instance;
    }

    public void set(String className, int lineOfCode, int lineOfComment, String sourceCode, String classType){
        List<String> classAttribute = new ArrayList<>();
        classAttribute.add(className);
        classAttribute.add( String.valueOf(lineOfCode) );
        classAttribute.add( String.valueOf(lineOfComment) );
        classAttribute.add(sourceCode);
        classAttribute.add(classType);
        listOfClassProperty.put(listOfClassProperty.size(), classAttribute);
    }

    public Map<Integer, List<String>> get(){
        return listOfClassProperty;
    }

    public void clear(){
        listOfClassProperty.clear();
    }
}
