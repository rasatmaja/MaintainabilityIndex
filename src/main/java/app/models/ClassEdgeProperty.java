package app.models;

import java.util.*;

public class ClassEdgeProperty {

    private static ClassEdgeProperty instance;
    private ClassProperty classProperty = ClassProperty.getInstance();
    Map<Integer, List<String>> listOfClassEdgeProperty = new HashMap<>();

    private ClassEdgeProperty(){};
    public static synchronized ClassEdgeProperty getInstance() {
        if (instance == null) {
            instance = new ClassEdgeProperty();
        }
        return instance;
    }

    public void set(String source, String target){
        if (!source.equalsIgnoreCase(target) && source != null && target != null){
            classProperty.get().entrySet().forEach(classData -> {
                if (target.equalsIgnoreCase(classData.getValue().get(0))){
                    System.out.println("Size : " + listOfClassEdgeProperty.size());
                    if(listOfClassEdgeProperty.size() > 0){
                        if(!isExist(source, target)){
                            listOfClassEdgeProperty.put(listOfClassEdgeProperty.size(), new ArrayList<>(Arrays.asList(source, target)));
                        }
                    } else {
                        listOfClassEdgeProperty.put(listOfClassEdgeProperty.size(), new ArrayList<>(Arrays.asList(source, target)));
                    }
                }
            });
        }
    }

    private boolean isExist (String source, String target){
        boolean isExist = false;
        for (Map.Entry<Integer, List<String>> link : listOfClassEdgeProperty.entrySet()) {
            String tempSource = link.getValue().get(0);
            String tempTarget = link.getValue().get(1);
            if (tempSource.equalsIgnoreCase(source) && tempTarget.equalsIgnoreCase(target))  {
                isExist = true;
            }
        }
        return isExist;
    }

    public Map<Integer, List<String>> get(){
        return this.listOfClassEdgeProperty;
    }

    public void clear(){
        this.listOfClassEdgeProperty.clear();
    }

    public void debug(){
        listOfClassEdgeProperty.entrySet().forEach(link -> {
            System.out.println("================= DEBUG LINK =================");
            System.out.println("key    : " + link.getKey());
            System.out.println("source : " + link.getValue().get(0));
            System.out.println("target : " + link.getValue().get(1));
            System.out.println("================= DEBUG LINK =================");
        });
    }
}
