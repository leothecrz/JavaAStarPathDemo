/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crz.astarjpath;

import crz.astarjpath.GridModelResources.Cell;
import crz.astarjpath.GridModelResources.XYMemory;
import crz.astarjpath.dialogResources.SetupSizes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.Timer;

/**
 *
 * @author LeothEcRz
 */
public final class MainFrame extends JFrame {
    
    private final static String FRAME_TITTLE = "A Star Path Finding Demo";
    private final static int TIMER_INTERVAL_LENGTH = 0;
    private static enum drawModes{
        Start, End, Wall, Erase
    }
    
    private final JMenuBar menubar;
    private final JMenu menu;
    private final JMenuItem MIdrawmode1, MIdrawmode2, MIdrawmode3, MIdrawmode4, MIclear, MIstart, MIpause, MIdrawBorder;  
    private final JPanel gridPanel;
    private final JPanel facePanel;
    
    private final ActionListener mainListener, timerListener;
    private final MouseListener mouseListenr;
    
    private final ArrayList<JPanel> thePanelArray;
    private final GridModel gModel; 
    
    private final SetupSizes launcherData;
    private final Timer modelTimer;
    
    private boolean running;
    private drawModes drawingMode;
    
    public MouseListener debugPointerListener;
    
    
    public MainFrame(SetupSizes setupSizes){
        
        super();
        
        launcherData = setupSizes;
        drawingMode = drawModes.Wall;
        running = false;
        gModel = new GridModel(launcherData.horizontalCellCount, launcherData.verticalCellCount);
        
        
        
        int width = Math.round( (float)launcherData.getHorizontalLength() * 1.020408f );
        int height = Math.round( (float)launcherData.getVerticalLength() * 1.115789f ); // 7% more needed that caclculated used 107/95 insted of 100/95. Tool Bar Effects Maybe
        
        System.out.println("\n CALC-WIDTH = " + width);
        System.out.println(" CALC-Height = " + height);    
        this.setPreferredSize(new Dimension(width, height));
        this.setSize(this.getPreferredSize());
        System.out.println(this.getSize());
        
        //Properties
        setTitle(FRAME_TITTLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        setResizable(true);
        
        //FacePanel
        facePanel = new JPanel(null);
        facePanel.setPreferredSize(this.getSize());
        facePanel.setSize(facePanel.getPreferredSize());
        facePanel.setBackground(Color.BLACK);
        
        //ToolBar
        JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setBounds(0, 0, this.getSize().width, (int)(Math.floor( (float)this.getSize().height * 0.03f)) );
        facePanel.add(toolbar);
        
        //Timer
        timerListener = setupTimerListener();
        modelTimer = new Timer(TIMER_INTERVAL_LENGTH, timerListener);
        modelTimer.setRepeats(true);
        
        //Listener
        mouseListenr = setupMouseListener();
        mainListener = setupActionListener();
        
        //MenuBar Setup
        menubar = new JMenuBar();
        menu = new JMenu(" Draw Modes ");
        
        ButtonGroup menuRadioButtonGroup = new ButtonGroup();
        MIdrawmode1 = new JRadioButtonMenuItem(" D M 1 - Start ");
        MIdrawmode1.addActionListener(mainListener);
        MIdrawmode1.setActionCommand("DM1");

        MIdrawmode2 = new JRadioButtonMenuItem(" D M 2 - End ");
        MIdrawmode2.addActionListener(mainListener);
        MIdrawmode2.setActionCommand("DM2");

        MIdrawmode3 = new JRadioButtonMenuItem(" D M 3 - Wall ");
        MIdrawmode3.addActionListener(mainListener);
        MIdrawmode3.setActionCommand("DM3");
        MIdrawmode3.setSelected(true); // default drawmode
        
        MIdrawmode4 = new JRadioButtonMenuItem(" D M 4 - Erase ");
        MIdrawmode4.addActionListener(mainListener);
        MIdrawmode4.setActionCommand("DM4");

        menuRadioButtonGroup.add(MIdrawmode1);
        menuRadioButtonGroup.add(MIdrawmode2);
        menuRadioButtonGroup.add(MIdrawmode3);
        menuRadioButtonGroup.add(MIdrawmode4);

        MIdrawBorder = new JMenuItem(" Draw Border ");
        MIdrawBorder.addActionListener(mainListener);
        MIdrawBorder.setActionCommand("MenuDrawBorder");

        MIclear = new JMenuItem(" Reset ");
        MIclear.setActionCommand("MenuReset");
        MIclear.addActionListener(mainListener);
        
        MIstart = new JMenuItem(" Start ");
        MIstart.setActionCommand("MenuStart");
        MIstart.addActionListener(mainListener);

        MIpause = new JMenuItem(" Pause ");
        MIpause.setActionCommand("MenuPause");
        MIpause.addActionListener(mainListener);

        toolbar.add(MIstart);
        toolbar.add(MIpause);
        toolbar.add(MIclear);
        toolbar.add(MIdrawBorder);
        
        menu.add(MIdrawmode1);
        menu.add(MIdrawmode2);
        menu.add(MIdrawmode3);
        menu.add(MIdrawmode4);
        //menu.add(MIdrawBorder);
        //menu.add(MIclear);
        //menu.add(MIstart);
        //menu.add(MIpause);

        menubar.add(menu);
        setJMenuBar(menubar);
        
        //gridPanel
        gridPanel = new JPanel(null);
        int x = (int) Math.floor(( (double)this.getSize().width * 0.01));
        int y = (int) Math.floor(( (double)this.getSize().height * 0.04));
        System.out.println(x + " :X | Y: " + y);
        thePanelArray = setUpthePanelArray();
        gridPanel.setBounds(x, y, launcherData.getHorizontalLength(), launcherData.getVerticalLength());
        gridPanel.setBackground(Color.BLACK);
        
        facePanel.add(gridPanel);
        this.add(facePanel);
        
        //System.out.println(this.getSize());
        System.out.println(gridPanel.getBounds());
        //System.out.println(toolbar.getSize());
        
        debugPointerListener = debugMouseListener();
        this.addMouseListener(debugPointerListener);
        
        
    }
    
    private MouseListener debugMouseListener(){
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
                System.err.println("\n" + e.getButton());
                System.err.println(" Point:");
                System.err.println(e.getPoint());
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
    }
    
    
    private JButton makeButton(String imageName, String actionCommand, String toolTipText ,String altText, ActionListener listener) {
    //Look for the image.
        String imgLocation = "images/"
                             + imageName
                             + ".gif";
        URL imageURL = AStarJPath.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(listener);

        if (imageURL != null) {                      //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {                                     //no image found
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
    }
    
    /**
     * GAME LOOP THAT RUNS WHEN START IS PRESSED
     * @return 
     */
    private ActionListener setupTimerListener(){
        
        ActionListener AL = evt -> {
            for(int i=0; i<30; i++)
                gModel.takeStep();
            
            paintAllPanels();
            
            if(gModel.pathDrawn){
                this.modelTimer.stop();
                System.out.println(" Path Found - END | Steps Taken: " + String.valueOf(gModel.stepsTaken));
            }
        };
        return AL;
        
    }
    
    /**
     * ToolBar ActionListener
     * @return 
     */
    private ActionListener setupActionListener(){
        
        ActionListener AL = evt -> {
            switch (evt.getActionCommand()) {
                case "MenuStart" -> {
                    if(running)
                        return;
                    
                    running = true;
                    modelTimer.start();
                }
                case "MenuPause" -> {
                    if(!running)
                        return;
                    
                    running = false;
                    modelTimer.stop();
                }
                case "MenuReset" -> {
                    if(running){
                        modelTimer.stop();
                        running = false;
                    }
                    gModel.resetBoard();
                    paintAllPanels();
                }
                case "DM1" ->{
                    drawingMode = drawModes.Start;
                }
                case "DM2" ->{
                    drawingMode = drawModes.End;
                }
                case "DM3" ->{
                    drawingMode = drawModes.Wall;
                }
                case "MenuDrawBorder" ->{
                    drawBorder();
                }
                case "DM4" -> {
                    drawingMode = drawModes.Erase;
                }
                
                default -> throw new AssertionError();
            };
        };
        
        return AL;
        
    }
    
    /**
     * JPANEL GRID LISTENER
     * @return 
     */
    private MouseListener setupMouseListener(){
        
        MouseListener mListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                if(running)
                    return;
                
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;
                
                XYMemory index = paneltoModel( (JPanel) e.getComponent() );
                switch (drawingMode) {
                    case Start -> {
                        if(gModel.startSet){
                            
                            gModel.setTypeTo(gModel.findIndex(gModel.GoalX, gModel.GoalY), Cell.CellTypes.EMPTY);
                            setPanelColor(pointToPanel(gModel.StartingX, gModel.StartingY));
                            
                        } 
                        gModel.resetStart(index.x, index.y);
                        setPanelColor( (JPanel) e.getComponent() );
                        //paintAllPanels();
                    }
                    case End -> {
                        if(gModel.endSet){
                            
                            gModel.setTypeTo(gModel.findIndex(gModel.GoalX, gModel.GoalY), Cell.CellTypes.EMPTY);
                            setPanelColor(pointToPanel(gModel.GoalX, gModel.GoalY));
                            
                        }
                        gModel.resetGoal(index.x, index.y);
                        setPanelColor( (JPanel) e.getComponent() );
                        //paintAllPanels();
                    }
                    case Wall -> {
                        gModel.drawGridWall(index.x, index.y);
                        setPanelColor( (JPanel) e.getComponent() );
                        //paintAllPanels();
                    }
                    case Erase ->{
                        gModel.setTypeTo(index.x, index.y, Cell.CellTypes.EMPTY);
                        setPanelColor( (JPanel) e.getComponent() );
                        //paintAllPanels();
                    }
                    default -> throw new AssertionError();
                }
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(running)
                    return;
                
                Color bgColor = e.getComponent().getBackground();
                int red = bgColor.getRed() + 10;
                int blue = bgColor.getBlue() + 10;
                int green = bgColor.getGreen() + 10;
                if(red>255) red=255;
                if(blue>255) blue=255;
                if(green>255) green=255;
                
                e.getComponent().setBackground(new Color(red,blue,green));
                    
                
                //e.getComponent().setBackground(e.getComponent().getBackground().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(running)
                    return;
                
                Color bgColor = e.getComponent().getBackground();
                int red = bgColor.getRed() - 10;
                int blue = bgColor.getBlue() - 10;
                int green = bgColor.getGreen() - 10;
                
                if(red<0) red=0;
                if(blue<0) blue=0;
                if(green<0) green=0;
                
                
                e.getComponent().setBackground(new Color(red,blue,green));

                
                //e.getComponent().setBackground(e.getComponent().getBackground().darker());

            }
        };
                
        return mListener;
        
    }
    
    private ArrayList<JPanel> setUpthePanelArray(){
        JPanel loopJPanel;
        ArrayList<JPanel> TPA = new ArrayList<>(launcherData.horizontalCellCount * launcherData.verticalCellCount);
        
        for(int i=0; i < launcherData.verticalCellCount;i++){
            for(int j=0; j < launcherData.horizontalCellCount; j++){
                
                loopJPanel = new JPanel();
                loopJPanel.setBounds( j*launcherData.pixelPerCell+0, i*launcherData.pixelPerCell+0, launcherData.pixelPerCell-2, launcherData.pixelPerCell-2);
                loopJPanel.addMouseListener(mouseListenr);
                
                //activeGPanel.add(new JLabel());
                
                loopJPanel.setName( String.valueOf(j) + "_" +String.valueOf(i) ); // "i_j" BAD WORK AROUND
                loopJPanel.setBackground(Color.LIGHT_GRAY);

                //paneltoModel(activeGPanel);
                gridPanel.add(loopJPanel);
                TPA.add(loopJPanel);
            }
        }
        
        return TPA;
    }
    
    private JPanel pointToPanel(int x, int y){
        int index = gModel.findIndex(x, y);
        return thePanelArray.get(index);
    }
    
    
    /**
     * Converts A Given Panel To Grid Model XYMEMORY information.
     * @param panel
     * @return 
     */
    private XYMemory paneltoModel(JPanel panel){

        XYMemory mem = new XYMemory();
        String[] split = panel.getName().split("_");
        
        try{
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            int index = (( y * launcherData.horizontalCellCount) + x);
            //System.out.println(index);
            
            mem.x = x;
            mem.y = y;
            mem.distance = index;
            
        } catch (NumberFormatException NFE){
            System.err.println(" Object: ");
            System.err.println(panel);
            System.err.println(" Not Set Up Correctly \n\n");
            return new XYMemory();
        }
        
        return mem;
    }
    
    /**
     * Set The Given Panel To Its Model CounterPart
     * @param panel 
     */
    private void setPanelColor(JPanel panel){
        XYMemory mem = paneltoModel(panel);
        Cell c = gModel.cellGrid.get(mem.distance);
        switch (c.getType()) {
            case CHECKED -> {
                panel.setBackground(Color.ORANGE);
            }
            case EMPTY -> {
                panel.setBackground(Color.LIGHT_GRAY);
            }
            case END -> {
                panel.setBackground(Color.RED);
            }
            case GRIDWALL -> {
                panel.setBackground(Color.DARK_GRAY);
            }
            case PATH -> {
                panel.setBackground(Color.CYAN);
            }
            case START -> {
                panel.setBackground(Color.GREEN);
            }
            case UNCHECKED -> {
                panel.setBackground(Color.GRAY);
            }     
            default -> throw new AssertionError();
        }
    }
    
    private void paintAllPanels(){
        for(int i=0; i<thePanelArray.size(); i++){
            setPanelColor(thePanelArray.get(i));
        }
    }
    
    private void drawBorder(){
        gModel.drawBorder();
        paintAllPanels();
    }
    
}
