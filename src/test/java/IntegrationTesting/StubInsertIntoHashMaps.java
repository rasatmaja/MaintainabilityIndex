/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegrationTesting;

import java.util.ArrayList;

/**
 *
 * @author rasio
 */
public class StubInsertIntoHashMaps {
    private static StubInsertIntoHashMaps instance;
    private ArrayList<String> node = new ArrayList<>();

    private StubInsertIntoHashMaps(){};
    public static synchronized StubInsertIntoHashMaps getInstance() {
        if (instance == null) {
            instance = new StubInsertIntoHashMaps();
        }
        return instance;
    }
    
    public void stub_insertIntoHashMaps(String node){
        this.node.add(node);
    }
    
    public ArrayList<String> getList(){
        return this.node;
    }
}
