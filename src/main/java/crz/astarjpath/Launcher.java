
package crz.astarjpath;

import crz.astarjpath.dialogResources.SetupDialog;
import crz.astarjpath.dialogResources.SetupSizes;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Launcher {
    
    private final ActionListener listener;
    private SetupDialog setupDialog;
    
    SetupSizes setupSizes;
    
    public Launcher(){
        
        listener = setUpListner();
        setupDialog = new SetupDialog(listener);
        setupDialog.setVisible(true);
        
    }
    
    private ActionListener setUpListner(){
        
        ActionListener AL = (ActionEvent evt) -> {
        
            switch (evt.getActionCommand()) {
                
                case "DialogOK" -> {
                    
                    if(setupDialog.getState() != SetupDialog.states.VALID_INPUT){
                        JOptionPane.showMessageDialog(setupDialog, "The Input Is Not Valid", "Bad Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    setupSizes = setupDialog.getSetupSizes();
                    
                   
                    System.out.println(setupSizes.toString());
                   
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    
                    String tooBigMessage = "The input values will create a window that is larger than the screen allows";
                    String tooBigTittle = "Inputs Result In Huge Window";
                    int border = 50;
                    
                    if( setupSizes.pixelPerCell * setupSizes.horizontalCellCount + border > screenSize.width){
                        JOptionPane.showMessageDialog(setupDialog, tooBigMessage, tooBigTittle, JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                        
                    if(setupSizes.pixelPerCell * setupSizes.verticalCellCount + border > screenSize.height){
                        JOptionPane.showMessageDialog(setupDialog, tooBigMessage, tooBigTittle, JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    setupDialog.dispose();
                    new MainFrame(setupSizes).setVisible(true);
                    return;
                    
                }
                
                default -> throw new AssertionError();
            }
        };
        return AL;
    }
    
}
