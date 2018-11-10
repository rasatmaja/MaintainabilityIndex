package app.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorLog {
    private static ErrorLog instance;
    private ErrorLog(){}

    /**
     * listOfErrors
     * int key
     * [0] name
     * [1] locations
     * [2] message
     */
    Map<Integer, List<String>> listOfErrors = new HashMap<>();

    public static synchronized ErrorLog getInstance() {
        if (instance == null) {
            instance = new ErrorLog();
        }
        return instance;
    }

    public void set(List<String> errors){
        listOfErrors.put(listOfErrors.size(), errors);
    }
    public Map<Integer, List<String>> get(){
        return listOfErrors;
    }

    public void clear(){
        listOfErrors.clear();
    }

    public void debug(){
        listOfErrors.entrySet().forEach(error -> {
            System.out.println("Name : " + error.getValue().get(0));
            System.out.println("loc  : " + error.getValue().get(1));
            System.out.println("message : " + error.getValue().get(2));
        });
    }
}
