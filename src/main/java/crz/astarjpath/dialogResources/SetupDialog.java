
package crz.astarjpath.dialogResources;

import crz.astarjpath.AStarJPath;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 *
 * @author LeothEcRz
 */
public final class SetupDialog extends JDialog{
    
    public static float MAX_GRID_WIDTH_PERCENT = 0.9604f; 
    public static float MAX_GRID_HEIGHT_PERCENT = 0.9212f;
   
    private final JButton okButton;
    private final ActionListener listener;
    private SetupSizes setupSizes;
    
    private final JTextField verticalCellCountField;
    private final JTextField horizontalCellCountField;
    private final JTextField pixelPerCellField;
    
    private boolean overrideLimit;
    
    /**
     * @param AL - Parent Controlled ActionListener For OK-button
     */
    public SetupDialog(ActionListener AL){
        super();
        
        // Variables
        listener = AL;
        
        // Set Dialog Size
        Dimension screenSize = AStarJPath.getScreenSize();
        Dimension size = new Dimension();
        size.setSize( (screenSize.width / 4),
                screenSize.height/ 4);
        setSize(size);
        
        // Properties
        setModal(true);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        // ALL
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
        
        //OKButton
        c.ipadx = 0;
        c.gridx = 3;
        c.gridy = 4;        
        okButton = new JButton();
        //okButton.addActionListener(listener);
        okButton.setAction(setOkButtonPreAction());
        //okButton.setActionCommand("DialogOK");
        okButton.setText("OK");
        add(okButton, c);
        
        //MaxButton
        c.gridx = 1;
        c.gridy = 4;        
        JButton maxButton = new JButton();
        maxButton.setAction(setMaxButtonAction(this.getContentPane()));
        maxButton.setText("Max With Current PPC");
        add(maxButton, c);
        
        //OverrideButton
        c.gridx = 5;
        c.gridy = 4;        
        JToggleButton overrideButton = new JToggleButton();
        overrideButton.setText("Override OFF");
        overrideButton.addItemListener(setOverideButtonActionListener());
        overrideButton.setSelected(true);
        add(overrideButton, c);
        
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
                
                if(!fieldsAreAllNumbers()){
                    JOptionPane.showMessageDialog( ((JButton)e.getSource()).getParent(), "The Input Is Not Valid", "Bad Input", JOptionPane.ERROR_MESSAGE);

                   return;
                }
                
                setupSizes = new SetupSizes(Integer.parseInt(pixelPerCellField.getText()), // Pixel Per Cell
                        Integer.parseInt(horizontalCellCountField.getText()), // H Count
                        Integer.parseInt(verticalCellCountField.getText())); // V Count
                System.out.println(setupSizes.toString());

                Dimension screenSize = AStarJPath.getScreenSize();
                String tooBigMessage = "The input values will create a window that is larger than the screen allows";
                String tooBigTittle = "Inputs Result In Huge Window";
                
                // MAX SIZES LIMITS
                if(!overrideLimit){
                    
                    int gridMaxWidth = Math.round( MAX_GRID_WIDTH_PERCENT * (float)screenSize.getWidth() );
                    System.out.println("H: " + setupSizes.getHorizontalLength());
                        System.out.print(" PossibleMAX:" + gridMaxWidth);
                    
                    int gridMaxHeight = Math.round( MAX_GRID_HEIGHT_PERCENT *  (float)screenSize.getHeight() );
                    System.out.println("V: " + setupSizes.getVerticalLength());
                        System.out.print(" PossibleMAX:" + gridMaxHeight);
                        
                    if( setupSizes.getHorizontalLength() >= gridMaxWidth){ //Horizontal
                        JOptionPane.showMessageDialog(((JButton)e.getSource()).getParent(), tooBigMessage + " .H.", tooBigTittle, JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if( setupSizes.getVerticalLength() >=  gridMaxHeight){ //Vertical
                        JOptionPane.showMessageDialog(((JButton)e.getSource()).getParent(), tooBigMessage + " .V.", tooBigTittle, JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "DialogOK", 0);
                listener.actionPerformed(event);
        } }; 
    }
    
    /**
     * 
     * @param comp
     * @return 
     */
    private AbstractAction setMaxButtonAction(Container comp){
        
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
                
                int vCount = (int) Math.floor((float)(screenSize.getHeight()) * 0.8910f) / ppc;
                int hCount = (int) Math.floor((float)(screenSize.getWidth()) * 0.9603f) / ppc;
                                
                System.out.println( vCount + " :V | C: " + hCount + "\n");
                
                horizontalCellCountField.setText(String.valueOf(hCount));
                verticalCellCountField.setText(String.valueOf(vCount));
                
            }
        };
        
    }
    
    /**
     * 
     * @return 
     */
    private ItemListener setOverideButtonActionListener(){
        return (ItemEvent e) -> {
            
            if( ((JToggleButton) e.getItem()).isSelected() ){
                ((JToggleButton) e.getItem()).setText("Override OFF");
                overrideLimit = false;
            }else{
                ((JToggleButton) e.getItem()).setText("Override ON ");
                overrideLimit = true;
            }
            System.out.println(overrideLimit);

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
    
    /**
     * 
     * @return True - if limit is set to override.
     */
    public boolean getOverride(){
        return this.overrideLimit;
    }
    
    
}
