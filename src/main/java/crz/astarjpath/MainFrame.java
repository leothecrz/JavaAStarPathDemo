/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crz.astarjpath;

import crz.astarjpath.GridModelResources.Cell;
import crz.astarjpath.GridModelResources.XYMemory;
import crz.astarjpath.MainFrameResources.GridPanel;
import crz.astarjpath.dialogResources.SetupSizes;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author LeothEcRz
 */
public final class MainFrame extends JFrame {

    private final static int TIMER_INTERVAL_LENGTH = 0;
    private final static int SIM_STEPS_PER_UPDATE= 1;

    public enum drawModes{
        Start, End, Wall, Erase
    }

    public enum brushModes{
        Point, H_Line, V_Line, Square, Circle,
    }

    private final JToolBar toolbar;
    private final JPopupMenu menu, brushMenu;
    private final JMenuItem MIdrawmode1, MIdrawmode2, MIdrawmode3, MIdrawmode4, MIBrushPoint, MIBrushHLine, MIBrushVLine, MIBrushSquare, MIBrushCircle;
    private final JButton MIclear, MIstart, MIpause, MIdrawBorder, MImenu, MIbrush;
    private final JPanel gridPanel;
    private final JSpinner brushSizeSpinner;
    private final JLabel statusLabel;

    private final MouseListener panelGridMouseListener;
    
    private final ArrayList<GridPanel> thePanelArray;
    
    private final AStartModel gModel; 
    
    private final SetupSizes launcherData;
    private final Timer modelTimer;
    
    private boolean running;
    private drawModes drawingMode;
    private brushModes brushMode;
    private int brushSize;

    public MainFrame(SetupSizes setupSizes){
        super();

        //Variables
        statusLabel = new JLabel("Status: Paused");
        launcherData = setupSizes;
        drawingMode = drawModes.Wall; //Default
        brushMode = brushModes.Point; //Default
        running = false;
        brushSize = 1;

        gModel = new AStartModel(launcherData.horizontalCellCount, launcherData.verticalCellCount);

        //Properties
        {
            String FRAME_TITTLE = "A Star Path Finding Demo";
            this.setTitle(FRAME_TITTLE);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            this.setResizable(false);
            int frameWidth = Math.round((float) launcherData.getHorizontalLength() * 1.020408f);
            int frameHeight = Math.round((float) launcherData.getVerticalLength() * 1.105263f);
            this.setPreferredSize(new Dimension(frameWidth, frameHeight));
            this.setSize(this.getPreferredSize());
            this.setLocationRelativeTo(null);
        }

        //FacePanel
        JPanel facePanel = new JPanel(null);
        facePanel.setPreferredSize(this.getSize());
        facePanel.setSize(facePanel.getPreferredSize());
        facePanel.setBackground(Color.BLACK);

        //Timer
        ActionListener timerListener = createTheTimerActionListener();
        modelTimer = new Timer(TIMER_INTERVAL_LENGTH, timerListener);
        modelTimer.setRepeats(true);
        
        //Listener
        panelGridMouseListener = setupMouseListener();
        ActionListener mainListener = createTheToolBarListener();

        //ToolBar
        toolbar = new JToolBar(JToolBar.HORIZONTAL);
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setBackground(Color.lightGray);
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        toolbar.setBounds(0, 0, this.getSize().width, (int) (Math.floor( ((float)this.getSize().height * 4f))/100f) ); //

        //Tool Bar Brush Menu
        {
            brushMenu = new JPopupMenu();
            ButtonGroup brushButtonGroup = new ButtonGroup();

            MIBrushPoint = new JRadioButtonMenuItem("Pixel/Point");
            MIBrushPoint.addActionListener(mainListener);
            MIBrushPoint.setActionCommand("PixelBrush");
            MIBrushPoint.setSelected(true);

            MIBrushHLine = new JRadioButtonMenuItem("Horizontal Line");
            MIBrushHLine.addActionListener(mainListener);
            MIBrushHLine.setActionCommand("HLineBrush");

            MIBrushVLine = new JRadioButtonMenuItem("Vertical Line");
            MIBrushVLine.addActionListener(mainListener);
            MIBrushVLine.setActionCommand("VLineBrush");

            MIBrushSquare = new JRadioButtonMenuItem("Square");
            MIBrushSquare.addActionListener(mainListener);
            MIBrushSquare.setActionCommand("SquareBrush");

            MIBrushCircle = new JRadioButtonMenuItem("Circle");
            MIBrushCircle.addActionListener(mainListener);
            MIBrushCircle.setActionCommand("CircleBrush");

            brushButtonGroup.add(MIBrushCircle);
            brushButtonGroup.add(MIBrushPoint);
            brushButtonGroup.add(MIBrushHLine);
            brushButtonGroup.add(MIBrushVLine);
            brushButtonGroup.add(MIBrushSquare);

            brushMenu.add(MIBrushPoint);
            brushMenu.add(MIBrushHLine);
            brushMenu.add(MIBrushVLine);
            brushMenu.add(MIBrushSquare);
            brushMenu.add(MIBrushCircle);

        }
        //Tool Bar Draw Type
        {
            menu = new JPopupMenu();
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

            menu.add(MIdrawmode1);
            menu.add(MIdrawmode2);
            menu.add(MIdrawmode3);
            menu.add(MIdrawmode4);
        }
        //Tool Bar Buttons
        {
            MIdrawBorder = new JButton(" Draw Border ");
            MIdrawBorder.addActionListener(mainListener);
            MIdrawBorder.setActionCommand("MenuDrawBorder");

            MIclear = new JButton(" Reset ");
            MIclear.setActionCommand("MenuReset");
            MIclear.addActionListener(mainListener);

            MIstart = new JButton(" Start ");
            MIstart.setActionCommand("MenuStart");
            MIstart.addActionListener(mainListener);

            MIpause = new JButton(" Pause ");
            MIpause.setActionCommand("MenuPause");
            MIpause.addActionListener(mainListener);

            MImenu = new JButton(" Draw-Type ");
            MImenu.setActionCommand("MenuMenu");
            MImenu.addActionListener(mainListener);

            MIbrush = new JButton(" Brush-Type ");
            MIbrush.setActionCommand("MenuBrush");
            MIbrush.addActionListener(mainListener);
        }
        // Brush Size Spinner
        brushSizeSpinner = new JSpinner();
        brushSizeSpinner.setPreferredSize(new Dimension(toolbar.getSize().width/40, toolbar.getHeight()-4 ));
        brushSizeSpinner.addChangeListener(createSpinnerChangeListener());
        brushSizeSpinner.setValue(1);

        toolbar.add(statusLabel);
        toolbar.add(MIstart);
        toolbar.add(MIpause);
        toolbar.add(MIclear);
        toolbar.add(MIdrawBorder);
        toolbar.add(MImenu);
        toolbar.add(MIbrush);
        toolbar.add(brushSizeSpinner);
        
        //gridPanel
        gridPanel = new JPanel(null);
        int x = (int) Math.floor(( (double)this.getSize().width * 0.01));
        int y = (int) Math.floor(( (double)this.getSize().height * 0.05));
        gridPanel.setBounds(x, y, launcherData.getHorizontalLength(), launcherData.getVerticalLength());
        gridPanel.setBackground(Color.BLACK);

        thePanelArray = setUpthePanelArray();

        facePanel.add(toolbar);
        facePanel.add(gridPanel);


        this.add(facePanel);
    }

    /**
     * @return ArrayList holding all the Panels in the grid. Adds the Panels To Frame.
     */
    private ArrayList<GridPanel> setUpthePanelArray(){

        GridPanel loopJPanel;
        ArrayList<GridPanel> TPA = new ArrayList<>(launcherData.horizontalCellCount * launcherData.verticalCellCount);
        
        for(int i=0; i < launcherData.verticalCellCount;i++){
            for(int j=0; j < launcherData.horizontalCellCount; j++){

                loopJPanel = new GridPanel(launcherData, i, j, panelGridMouseListener);
                {
                    //loopJPanel.setBounds( j*launcherData.pixelPerCell, i*launcherData.pixelPerCell, launcherData.pixelPerCell-2, launcherData.pixelPerCell-2);
                    //loopJPanel.addMouseListener(panelGridMouseListener);

                    //loopJPanel.add(new JLabel( "X: " + String.valueOf(j) ) );
                    //loopJPanel.add(new JLabel( "Y: " + String.valueOf(i) ) );

                    //loopJPanel.setName( String.valueOf(j) + "_" +String.valueOf(i) ); // "i_j" BAD WORK AROUND
                    //loopJPanel.setBackground(Color.LIGHT_GRAY);
                }
                gridPanel.add(loopJPanel);
                TPA.add(loopJPanel);

            }
        }
        
        return TPA;
    }
    

    /**
     * Handles State Changes For Brush Size Spinner
     * @return Listener For Spinner
     */
    private ChangeListener createSpinnerChangeListener(){
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                var eSource = (JSpinner)(e.getSource());
                int value = (int) eSource.getValue();

                // Brush Size Limits For MODE:START, MODE:END, and BRUSH:PIXEL
                if(drawingMode == drawModes.Start || drawingMode == drawModes.End || brushMode == brushModes.Point){
                    eSource.setValue(1);
                    brushSize = 1;
                    return;
                }

                // Set Min Value
                if(value < 1){ 
                    eSource.setValue(brushSize);
                    return;
                }

                // No Even Values FOR HORIZONTAL BRUSH
                if( (brushMode == brushModes.H_Line) && value%2==0){ 
                    if((int) eSource.getValue() > brushSize){
                        value++;
                    } else {
                        value--;
                    }
                    eSource.setValue(value);
                }

                brushSize = value;
                System.out.println("BrushSize: " + brushSize);

            }
        };
    }

    /**
     * GAME LOOP THAT RUNS WHEN START IS PRESSED
     * @return GAME LOOP ActionListener
     */
    private ActionListener createTheTimerActionListener(){

        return evt -> {
            for(int i=0; i<SIM_STEPS_PER_UPDATE; i++)
                gModel.takeStep();

            paintAllPanels();

            if(gModel.pathDrawn){
                this.modelTimer.stop();
                System.out.println(" Path Found - END | Steps Taken: " + String.valueOf(gModel.simStepsTaken));
                statusLabel.setText("Status: Paused");
            }
        };
        
    }
    
    /**
     * ToolBar ActionListener
     * @return Listener Responsible For Handling ToolBar Button Presses.
     */
    private ActionListener createTheToolBarListener(){

        return evt -> {
            switch (evt.getActionCommand()) {
                case "MenuMenu" -> menu.show( this.toolbar, MImenu.getX(), MImenu.getY()+MImenu.getHeight());
                case "MenuBrush" -> brushMenu.show( this.toolbar, MIbrush.getX(), MIbrush.getY()+MIbrush.getHeight());
                case "MenuStart" -> {
                    if(running)
                        return;

                    running = true;
                    statusLabel.setText("Status: Running");
                    modelTimer.start();
                }
                case "MenuPause" -> {
                    if(!running)
                        return;

                    running = false;
                    statusLabel.setText("Status: Paused");
                    modelTimer.stop();
                }
                case "MenuReset" -> {
                    if(running){
                        modelTimer.stop();
                        running = false;
                    }
                    statusLabel.setText("Status: Paused");
                    gModel.resetBoard();
                    paintAllPanels();
                }
                case "DM1" -> {
                    drawingMode = drawModes.Start;
                    MIBrushPoint.setSelected(true);
                    brushMode = brushModes.Point;
                    brushSize = 1;
                    brushSizeSpinner.setValue(1);
                }
                case "DM2" -> {
                    drawingMode = drawModes.End;
                    MIBrushPoint.setSelected(true);
                    brushMode = brushModes.Point;
                    brushSize = 1;
                    brushSizeSpinner.setValue(1);
                }
                case "DM3" -> drawingMode = drawModes.Wall;
                case "MenuDrawBorder" -> drawBorder();
                case "DM4" -> drawingMode = drawModes.Erase;

                case "PixelBrush" ->
                    brushMode = brushModes.Point;
                case "HLineBrush"->
                    brushMode = brushModes.H_Line;
                case "VLineBrush"->
                    brushMode = brushModes.V_Line;
                case "SquareBrush" ->
                    brushMode = brushModes.Square;
                case "CircleBrush" ->
                    brushMode = brushModes.Circle;


                default -> throw new AssertionError();
            };
        };
        
    }
    
    /**
     * PANEL GRID LISTENER
     * @return MouseListener That handles mouse events on the grid panels.
     */
    private MouseListener setupMouseListener(){

        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(running)
                    return; // Cant Click While Running

                if(e.getButton() == MouseEvent.BUTTON1){
                    m1ButtonClick(e);
                    return;
                }

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if(running) // Only Run When Status = Not Running
                    return;

                GridPanel activePanel = (GridPanel) e.getSource();
                activePanel.setBackground(activePanel.getBackground().brighter());

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(running) // Only Run When Status = Not Running
                    return;

                GridPanel activePanel = (GridPanel) e.getSource();
                setPanelColor(activePanel);

            }

            @Override
            public void mousePressed(MouseEvent e) {
                return;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                return;
            }

        };
        
    }

    /**
     * MI CLick On Panel Grids
     * @param e
     */
    private void m1ButtonClick(MouseEvent e){
        XYMemory index = panelToModel( (GridPanel) e.getComponent() );
        switch (drawingMode) {
            case Start -> {

                if(gModel.isStartSet)
                    frameAllignedSetType(gModel.StartingX,gModel.StartingY, Cell.CellTypes.EMPTY);
                gModel.resetStart(index.x, index.y);
                frameAllignedSetType(index.x, index.y, Cell.CellTypes.START);
            }
            case End -> {

                if(gModel.isEndSet)
                    frameAllignedSetType(gModel.GoalX,gModel.GoalY, Cell.CellTypes.EMPTY);
                gModel.resetGoal(index.x, index.y);
                frameAllignedSetType(index.x,index.y, Cell.CellTypes.END);

            }
            case Wall ->
                clickDrawWall(e , Cell.CellTypes.GRIDWALL);

            case Erase ->
                clickDrawWall(e, Cell.CellTypes.EMPTY);

            default -> throw new AssertionError();
        }
    }

    private void clickDrawWall(MouseEvent e, Cell.CellTypes c){
        XYMemory index = panelToModel( (GridPanel) e.getComponent() );
        switch(brushMode){
            case Point -> {
                frameAllignedSetType(index.x,index.y, c);
            }
            case Circle -> {
            }
            case H_Line -> {
                Integer[] drawIndexes = new Integer[brushSize];
                int centerIndex = (brushSize/2);

                drawIndexes[centerIndex] = gModel.findIndex(index.x, index.y); // Middle Index Set

                System.out.println(centerIndex);
                System.out.println(centerIndex);


                for (int i = 0; i < centerIndex; i++) {

                    //Check Index i Bound
                    if((index.x - (i+1)) >= 0)
                        drawIndexes[i] = gModel.findIndex(index.x - (i+1), index.y);
                    System.out.println(i);

                }
                for(int i = centerIndex+1; i<brushSize; i++){

                    int fixedI = i-centerIndex; // starts at zero
                    if(index.x + fixedI < gModel.width)
                        drawIndexes[i] = gModel.findIndex(index.x + (fixedI), index.y);
                    System.out.println(i);

                }
                for(Integer j : drawIndexes){
                    if(j == null)
                        continue;
                    XYMemory activeMem = indexToModel(j);
                    frameAllignedSetType(activeMem.x, activeMem.y, c);
                    System.out.println(j);
                }
            }
            case V_Line -> {


            }
            case Square -> {

            }
            default -> {
                System.out.println("NO BRUSH ERROR");
                return;
            }
        };

    }

    private void frameAllignedSetType(int x, int y, Cell.CellTypes type){
        gModel.setTypeTo(x,y,type);
        setPanelColor(pointToPanel(x,y));
    }

    private GridPanel pointToPanel(int x, int y){
        int index = gModel.findIndex(x, y);
        return thePanelArray.get(index);
    }

    private GridPanel indexToPanel(int index){
        return thePanelArray.get(index);
    }

    /**
     * Converts A Given Panel To Grid Model XYMEMORY information.
     * @param
     * @return 
     */
    private XYMemory indexToModel(int index){
        return panelToModel(indexToPanel(index));
    }
    private XYMemory panelToModel(GridPanel panel){

        XYMemory mem = new XYMemory();

        int x = panel.getJPoint();
        int y = panel.getIPoint();
        int index = (( y * launcherData.horizontalCellCount) + x);

        mem.x = x;
        mem.y = y;
        mem.distance = index;

        return mem;
    }

    /**
     * Update All Panel Colors
     */
    private void paintAllPanels(){
        for (GridPanel jPanel : thePanelArray) {
            setPanelColor(jPanel);
        }
    }

    /**
     * Set The Given Panel To Its Model CounterPart
     * @param panel - Panel That Will Have Its Color Changed
     */
    private void setPanelColor(GridPanel panel){
        XYMemory mem = panelToModel(panel);
        Cell c = gModel.cellGrid.get(mem.distance);
        switch (c.getType()) {
            case CHECKED ->
                panel.setBackground(Color.ORANGE);

            case EMPTY ->
                panel.setBackground(Color.LIGHT_GRAY);

            case END ->
                panel.setBackground(Color.RED);

            case GRIDWALL ->
                panel.setBackground(Color.DARK_GRAY);

            case PATH ->
                panel.setBackground(Color.CYAN);

            case START ->
                panel.setBackground(Color.GREEN);

            case UNCHECKED ->
                panel.setBackground(Color.GRAY);

            default -> throw new AssertionError();
        }
    }

    /**
     * Run GModel method To Change Border Panels To Walls
     * Update all panels to reflect the change.
     */
    private void drawBorder(){
        gModel.drawBorder();
        paintAllPanels();
    }

}
