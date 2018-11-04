package app.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodProperty {
    private static MethodProperty instance;
    private MethodProperty(){};

    Map<Integer, List<String>> lifOfMethodProperty = new HashMap<>();

    /**
     * lifOfMethodProperty
     * int key
     * [0] class name
     * [1] method name
     * [2] LOC
     * [3] comment
     * [4] source code
     * [5] body method block statement
     * [6] simple name
     * [7] type
     * [8] parameter type
     * [9] parameter name
     */

    public static synchronized MethodProperty getInstance() {
        if (instance == null) {
            instance = new MethodProperty();
        }
        return instance;
    }

    public void set(String className, String methodName, int lineOfCode, int lineOfComment, String sourceCode, String bodyMethod, String simpleName, String type, String parameterTye, String parameterName){
        List<String> methodProperty = new ArrayList<>();
        methodProperty.add(className);
        methodProperty.add(methodName);
        methodProperty.add( String.valueOf(lineOfCode) );
        methodProperty.add( String.valueOf(lineOfComment) );
        methodProperty.add(sourceCode);
        methodProperty.add(bodyMethod);
        methodProperty.add(simpleName);
        methodProperty.add(type);
        methodProperty.add(parameterTye);
        methodProperty.add(parameterName);
        lifOfMethodProperty.put(lifOfMethodProperty.size(), methodProperty);
    }

    public Map<Integer, List<String>> get(){
        return lifOfMethodProperty;
    }

    public void clear(){
        lifOfMethodProperty.clear();
    }
}
