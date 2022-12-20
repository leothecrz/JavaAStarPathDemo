
package crz.astarjpath.dialogResources;

import crz.astarjpath.AStarJPath;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author LeothEcRz
 */
public class SetupDialog extends JDialog{
    
    public static enum states{
        UNKOWN,
        VALID_INPUT,
        INVALID_INPUT
    }
    
    private final JButton okButton;
    private final ActionListener listener;
    private SetupSizes setupSizes;
    
    JTextField verticalCellCountField;
    JTextField horizontalCellCountField;
    JTextField pixelPerCellField;
    
    private states currentState; 
    
    /**
     * @param AL - Parent Controlled ActionListener For OK-button
     */
    public SetupDialog(ActionListener AL){
        super();
        
        listener = AL;
        currentState = states.UNKOWN;
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        //Set Dialog Size
        Dimension screenSize = AStarJPath.getScreenSize();
        
        Dimension size = new Dimension();
        size.setSize( (screenSize.width / 4),
                screenSize.height/ 4);
        setSize(size);
        
        setModal(true);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        //ALL
        c.ipadx = getSize().width / 9;
        c.weightx = 1;
        c.weighty = 1;
        
        // Pixel Per
        c.gridx = 1;
        c.gridy = 2;
        pixelPerCellField = new JTextField();
        pixelPerCellField.setText("");
        pixelPerCellField.setHorizontalAlignment(JTextField.CENTER);
        add(pixelPerCellField, c);
        
        // Horizontal
        c.gridx = 3;
        c.gridy = 2;
        horizontalCellCountField = new JTextField();
        horizontalCellCountField.setText("");
        horizontalCellCountField.setHorizontalAlignment(JTextField.CENTER);

        add(horizontalCellCountField, c);
        
        // Vertical
        c.gridx = 5;
        c.gridy = 2;
        verticalCellCountField = new JTextField();
        verticalCellCountField.setText("");
        verticalCellCountField.setHorizontalAlignment(JTextField.CENTER);

        add(verticalCellCountField, c);
        
        //Buttons
        c.ipadx = 0;
        c.gridx = 3;
        c.gridy = 4;        
        okButton = new JButton();
        okButton.addActionListener(listener);
        okButton.setAction(setOkButtonPreAction());
        okButton.setActionCommand("DialogOK");
        okButton.setText("OK");
        add(okButton, c);
        
        c.gridx = 1;
        c.gridy = 4;        
        JButton maxButton = new JButton();
        maxButton.setAction(setMaxButtonAction(this.getContentPane()));
        maxButton.setText("Max With Current PPC");
        add(maxButton, c);
        
        //Labels
        JLabel vCountLabel = new JLabel("Vertical Count: ");
        vCountLabel.setHorizontalTextPosition(JLabel.CENTER);
        c.gridx = 5;
        c.gridy = 1;
        add(vCountLabel, c);
        
        JLabel hCountLabel = new JLabel("Horizontal Count: ");
        hCountLabel.setHorizontalTextPosition(JLabel.CENTER);
        c.gridx = 3;
        c.gridy = 1;
        add(hCountLabel, c);
        
        JLabel pixelPerLabel = new JLabel("Pixels Per Cell: ");
        pixelPerLabel.setHorizontalTextPosition(JLabel.CENTER);
        c.gridx = 1;
        c.gridy = 1;
        add(pixelPerLabel, c);
    }
    
    /**
     * 
     * @return The Action Performed by the setupDialog's OK button before The ActionListener
     */
    private AbstractAction setOkButtonPreAction(){
        return new AbstractAction() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //Check Status of Text Fields
                if(!fieldsAreAllNumbers()){
                    currentState = states.INVALID_INPUT;
                   return;
                }
                
                currentState = states.VALID_INPUT;
                
                setupSizes = new SetupSizes(Integer.parseInt(pixelPerCellField.getText()), // Pixel Per Cell
                        Integer.parseInt(horizontalCellCountField.getText()), // H Count
                        Integer.parseInt(verticalCellCountField.getText())); // V Count
                
        } }; 
    }
    
    private AbstractAction setMaxButtonAction(Container comp){
        final int buffer = 150;
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(pixelPerCellField.getText().isBlank()){
                    JOptionPane.showMessageDialog(comp, "Pixel Per Cell Field is EMPTY", "Empty Required Field", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int ppc;
                try{
                    ppc = Integer.parseInt(pixelPerCellField.getText());
                 }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(comp, "Pixel Per Cell Field is NOT VALID", "INVALID ENTRY in Required Field", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if(!horizontalCellCountField.getText().isBlank())
                    horizontalCellCountField.setText("");
                
                if(!verticalCellCountField.getText().isBlank())
                    verticalCellCountField.setText("");
                
                
                Dimension screenSize = AStarJPath.getScreenSize();
                screenSize.setSize(screenSize.width-buffer, screenSize.height-buffer);
                int vCount = screenSize.height / ppc;
                int hCount = screenSize.width / ppc;
                horizontalCellCountField.setText(String.valueOf(hCount));
                verticalCellCountField.setText(String.valueOf(vCount));
                
            }
        };
    }
    
    
    /**
     * @return True - if all fields are valid entries.
     */
    private boolean fieldsAreAllNumbers(){
        
        String s1 = pixelPerCellField.getText();
        try{
            Integer.parseInt(s1);
        } catch (NumberFormatException ex){
            return false;
        }
        
        String s2 = horizontalCellCountField.getText();
        try{
            Integer.parseInt(s2);
        } catch (NumberFormatException ex){
            return false;
        }
        
        String s3 = verticalCellCountField.getText();
        try{
            Integer.parseInt(s3);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }
    
    /**
     * @return data type that holds sizing information. 
     */
    public SetupSizes getSetupSizes(){
        return setupSizes;
    }
    
    public states getState(){
        return this.currentState;
    }
    
    public void setState(states state){
        this.currentState = state;
    }
    
    
}
