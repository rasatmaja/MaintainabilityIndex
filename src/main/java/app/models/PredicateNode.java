package app.models;

public class PredicateNode {
    private static PredicateNode instance;
    private int predicateNode;

    public static PredicateNode getInstance() {
        if (instance == null) {
            instance = new PredicateNode();
        }
        return instance;
    }

    private PredicateNode() {
    }

    public void setPredicateNode(int numberOfPredicateNode){
        this.predicateNode = numberOfPredicateNode;
    }

    public int getPredicateNode(){
        return this.predicateNode;
    }
}
