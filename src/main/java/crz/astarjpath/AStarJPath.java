

package crz.astarjpath;

//Static
import java.awt.Dimension;
import java.awt.Toolkit;

//Main
import javax.swing.SwingUtilities;


/**
 * Entry Point and Runs Projecr in the event dispactch thread. 
 */
public class AStarJPath {

    public static Dimension getScreenSize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Launcher::new);
    }

}
