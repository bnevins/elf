
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bnevins
 */
public class Quickie {

    /**
     * @param args the command line arguments
     */
    public Quickie() {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("data/tinyCG.txt");
            byte[] bytes = in.readAllBytes();
            String s = new String(bytes);
            System.out.println(s);
        }
        catch (IOException ex) {
            Logger.getLogger(Quickie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        new Quickie();
    }
    
}
