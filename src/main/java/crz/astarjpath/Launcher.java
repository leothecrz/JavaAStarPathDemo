
package crz.astarjpath;

import crz.astarjpath.dialogResources.SetupDialog;
import crz.astarjpath.dialogResources.SetupSizes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Launcher {
    
    private final ActionListener listener;
    private final SetupDialog setupDialog;
    
    SetupSizes setupSizes;
    
    public Launcher(){
        
        listener = setUpListner();
        setupDialog = new SetupDialog(listener);
        setupDialog.setVisible(true);
        
    }
    
    /**
     * OK Button Container Parent Actions 
     * @return 
     */
    private ActionListener setUpListner(){
        
        ActionListener AL = (ActionEvent evt) -> {
            if(!evt.getActionCommand().equals("DialogOK")){
                return;
                //throw new AssertionError();
            }
            setupSizes = setupDialog.getSetupSizes();
            setupDialog.dispose();
            new MainFrame(setupSizes).setVisible(true);
            
        };
        return AL;
    }
    
}
