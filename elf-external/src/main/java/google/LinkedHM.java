/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 *
 * @author bnevins
 */
public class LinkedHM {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashMap<String,String> lhm = new LinkedHashMap<>();
        lhm.put("a", "a");
        lhm.put("d", "a");
        lhm.put("e", "a");
        lhm.put("c", "a");
        lhm.put("b", "a");


        Iterator itr = lhm.keySet().iterator();
        while(itr.hasNext()) {
            System.out.println(itr.next());
        }
    }
}
