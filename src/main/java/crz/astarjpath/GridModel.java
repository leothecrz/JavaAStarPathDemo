/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crz.astarjpath;

import crz.astarjpath.GridModelResources.Cell;
import crz.astarjpath.GridModelResources.Cell.CellTypes;
import crz.astarjpath.GridModelResources.XYMemory;
import java.util.ArrayList;
import java.util.PriorityQueue;


/**
 *
 * @author LeothEcRz
 */
public class GridModel {
    
    public int width;
    public int height;
    
    public int StartingX;
    public int StartingY;
    
    public int GoalX;
    public int GoalY;
    
    public boolean pathFound;
    public boolean pathDrawn;
    
    public boolean startSet;
    public boolean endSet;
    
    public int stepsTaken;
    
    
    /**
     * Grid of Cells
     */
    public ArrayList<Cell> cellGrid;
    
    /**
     * Path Memory
     */
    ArrayList<Integer> cameFrom;
    
    /**
     * The Open Set
     */
    final PriorityQueue<Cell> cellsPriorityQueue;
    
    /**
     * A Step's Memory of its neighbors.
     */
    ArrayList<XYMemory> neighbors = new ArrayList<>();

    /**
     * DrawingPath Pointer
     */
    XYMemory currentDrawPath;
    
    /**
     * 
     * @param width
     * @param height 
     */
    public GridModel(int width, int height){
        
        pathFound = false;
        pathDrawn = false;
        startSet = false;
        endSet = false;
        stepsTaken = 0;
        currentDrawPath = new XYMemory();
        
        cellGrid = new ArrayList<>(width*height);
        
        cameFrom = new ArrayList<>(); //
        
        cellsPriorityQueue = new PriorityQueue<>((Cell o1, Cell o2) -> {
            if(o1.getF_Cost() > o2.getF_Cost())
                return 1;
            return 0; });
        
        setNewSize(width, height);
        StartingX = StartingY = -1;
    }
    
    /**
     * Set the size of the model's grid. Resets the <b>cellGrid</b>.
     * Resets the <b>cameFrom</b> Memory.
     * @param w
     * @param h 
     */
    public final void setNewSize(int w, int h){
        
        this.width = w;
        this.height = h;
        
        while(!cellGrid.isEmpty()){
            cellGrid.remove(cellGrid.size()-1);
        }
        
        while(!cameFrom.isEmpty()){
            cameFrom.remove(cameFrom.size()-1);
        }
        
        for(int i=0; i<this.height; i++){
            for(int j=0; j <this.width; j++){
                cellGrid.add(new Cell(j, i));
                cameFrom.add(-1);
            }
        }
        
    }
    
    public int findIndex(int x, int y){
        return ((y*this.width) + x);
    }
    
    public void setTypeTo(int x, int y, CellTypes type){
        cellGrid.get(findIndex(x, y)).setType(type);
        
        if(type == CellTypes.END){
            this.GoalX = x;
            this.GoalY = y;
        }
        
    }
    
    public void setTypeTo(int index, CellTypes type){
        cellGrid.get(index).setType(type);
        
    }
    
    public CellTypes getCellTypes(int x, int y){
        return cellGrid.get(findIndex(x, y)).getType();
    }
    
    public CellTypes getCellTypes(int index){
        return cellGrid.get(index).getType();
    }
    
    private double updateHcost(int x, int y){
        
        int deltaX =  Math.abs( (GoalX - x) );
        int deltaY =  Math.abs( (GoalY - y ) );
        
        Cell activeCell = cellGrid.get(findIndex(x, y));
        int totalDelta;

        if(deltaX > deltaY){
            totalDelta = (14*deltaY) + 10*(deltaX-deltaY);
            activeCell.setH_Cost(totalDelta);
            activeCell.updateFCost();
            return totalDelta;
        }
        totalDelta = (14*deltaX) + 10 *(deltaY - deltaX);
        activeCell.setH_Cost(totalDelta);
        activeCell.updateFCost();
        return totalDelta;
        
        /**
        deltaX = deltaX * deltaX;
        deltaY = deltaY * deltaY;
        
        double totalDelta = Math.sqrt( deltaX + deltaY );
        
        Cell activeCell = cellGrid.get(findIndex(x, y));
        activeCell.setH_Cost(totalDelta);
        activeCell.updateFCost();
        
        return activeCell.getH_Cost();
        */
        
    }
    
    private boolean notWall(int x, int y){
     return ! (cellGrid.get(findIndex(x, y)).getType() == Cell.CellTypes.GRIDWALL) ;
    }
    
    public void drawGridWall(int x, int y){
        setTypeTo(x, y, CellTypes.GRIDWALL);
    }
    
    public void resetBoard(){
        setNewSize(this.width, this.height);
        pathFound = false;
        pathDrawn = false;
        
        if(startSet)
            clearStart();
        
        if(endSet)
            clearGoal();
        
    }
    
    public void resetGoal(int x, int y){
        if(endSet)
            clearGoal();
        endSet = true;
        this.GoalX = x;
        this.GoalY = y;
        setTypeTo(GoalX, GoalY, CellTypes.END);
    }
    
    private void clearGoal(){
        setTypeTo(GoalX, GoalY, CellTypes.EMPTY);
    }
    
    public void resetStart(int x, int y){
        
        //Reset previous start if one has already been set
        if(startSet)
            clearStart();
        
        if(!cellsPriorityQueue.isEmpty())
            emptyCellQueue();
            
        this.StartingX = x;
        this.StartingY = y;
        
        Cell activeCell = cellGrid.get(findIndex(x, y));
        activeCell.setInOpenSet(true);
        updateHcost(x, y);
        activeCell.setG_Cost(0);
        activeCell.setType(CellTypes.START);
        cellsPriorityQueue.add(activeCell);
        
        startSet = true;
        pathFound = false;
    }
    
    private void clearStart(){
        setTypeTo(StartingX, StartingX, CellTypes.EMPTY);
    }
    
    private void emptyCellQueue(){
        Cell activeCell;
        while(!cellsPriorityQueue.isEmpty()){
            activeCell = cellGrid.get( 
                    findIndex( cellsPriorityQueue.peek().getX(), 
                            cellsPriorityQueue.peek().getY() ));
            activeCell.setInOpenSet(false);
            activeCell.setType(CellTypes.EMPTY);
            cellsPriorityQueue.remove();
        }
    }
    
    public void takeStep(){
        stepsTaken++;
        if(pathFound){
            
            if(currentDrawPath.x == StartingX && currentDrawPath.y == StartingY){
                pathDrawn = true;
                return;
            }
        
            //int cameFromIndex = cameFrom.get(findIndex(currentDrawPath.x, currentDrawPath.y));
            Integer cameFromIndex = cellGrid.get(findIndex(currentDrawPath.x, currentDrawPath.y)).getCameFromIndex();
            
            Cell nextPathCell = cellGrid.get(cameFromIndex);
            currentDrawPath.x = nextPathCell.getX();
            currentDrawPath.y = nextPathCell.getY();
            nextPathCell.setType(CellTypes.PATH);
            //cellGrid.get(cameFromIndex).setType(CellTypes.PATH);

            System.out.println("\nPATH: X: " + String.valueOf(currentDrawPath.x));
            System.out.println(" Y: " + String.valueOf(currentDrawPath.y));
            
            return;
        }
        
        if(cellsPriorityQueue.isEmpty())
            return;
        
        
        

        Cell activeCell = cellsPriorityQueue.peek();
        int activeIndex = findIndex(activeCell.getX(), activeCell.getY());
        
        if(activeCell.getX() == GoalX && activeCell.getY() == GoalY){

            currentDrawPath.x = activeCell.getX();
            currentDrawPath.y = activeCell.getY();
            pathFound = true;
            return;

        }

        cellGrid.get(activeIndex).setType(CellTypes.CHECKED);
        
        neighbors = new ArrayList<>();

        //Surrounding Cordinate Variables Set
        int LeftX = activeCell.getX() - 1;
        if(LeftX < 0)
           LeftX = this.width - 1;

        int RightX = activeCell.getX() + 1;
        if (RightX == this.width)
            RightX = 0;

        int TopY = activeCell.getY()- 1;
        if(TopY < 0)
            TopY = this.height - 1;

        int BottomY = activeCell.getY() + 1;
        if (BottomY == this.height)
            BottomY = 0;

        { // Top Y
        stepModuleNeightborsUpdate(LeftX, TopY, 14);
        stepModuleNeightborsUpdate(activeCell.getX(), TopY, 10);
        stepModuleNeightborsUpdate(RightX, TopY, 14);
        } { // Active Cell Y
        stepModuleNeightborsUpdate(LeftX, activeCell.getY(), 10);
        stepModuleNeightborsUpdate(RightX, activeCell.getY(), 10);
        } { // Bottom Y
        stepModuleNeightborsUpdate(LeftX, BottomY, 14);
        stepModuleNeightborsUpdate(activeCell.getX(), BottomY, 10);
        stepModuleNeightborsUpdate(RightX, BottomY, 14);
        }

        cellGrid.get(activeIndex).setInOpenSet(false);
        
        cellsPriorityQueue.poll();

        System.out.println("\n X: " + String.valueOf(activeCell.getX()));
        System.out.println(" Y: " + String.valueOf(activeCell.getY()));
        System.out.println(" HCost: " + String.valueOf(activeCell.getH_Cost()));
        System.out.println(" GCost: " + String.valueOf(activeCell.getG_Cost()));
        System.out.println(" (1) OpenSet Size: " + String.valueOf(cellsPriorityQueue.size()));
        
        while(!neighbors.isEmpty()){
            XYMemory xActive = neighbors.get(neighbors.size() - 1);

            double newGCostValue = activeCell.getG_Cost() + xActive.distance;
            if(newGCostValue < cellGrid.get(findIndex(xActive.x, xActive.y)).getG_Cost()){
                
                Cell neighborCell = cellGrid.get(findIndex(xActive.x, xActive.y));
                //cameFrom.set(findIndex(xActive.x, xActive.y), activeIndex);
                neighborCell.setCameFromIndex(activeIndex);
                //cellGrid.get(findIndex(xActive.x, xActive.y)).setCameFromIndex(activeIndex);
                neighborCell.setG_Cost(newGCostValue);
                //cellGrid.get(findIndex(xActive.x, xActive.y)).setG_Cost(newGCostValue);
                updateHcost(xActive.x, xActive.y);
                //cellGrid.get(findIndex(xActive.x, xActive.y)).setF_Cost(newGCostValue + updateHcost(xActive.x, xActive.y));
                
                //if(!cellGrid.get(findIndex(xActive.x, xActive.y)).isInOpenSet()){
                if(!neighborCell.isInOpenSet()){
                    //cellGrid.get(findIndex(xActive.x, xActive.y)).setInOpenSet(true);
                    neighborCell.setInOpenSet(true);
                    //if(cellGrid.get(findIndex(xActive.x, xActive.y)).getType() != CellTypes.END )
                    if(neighborCell.getType() != CellTypes.END)
                        //cellGrid.get(findIndex(xActive.x, xActive.y)).setType(CellTypes.UNCHECKED);
                        neighborCell.setType(CellTypes.UNCHECKED);

                    cellsPriorityQueue.add(cellGrid.get(findIndex(xActive.x, xActive.y)));
                    cellsPriorityQueue.add(neighborCell);
                }
            } 
            neighbors.remove(neighbors.size() - 1);
        }
    }
    
    private void stepModuleNeightborsUpdate(int x, int y, int dist){
        updateHcost(x, y);
        if(notWall(x, y))
            neighbors.add(new XYMemory(x, y, dist));
    }
    
    public void drawBorder(){
        for(int i=0;i<this.width;i++){
            this.drawGridWall(i, 0);
        }
        for(int i=0;i<this.height;i++){
            this.drawGridWall(0, i);
        }
    }
    
    
    /*
    public static void main(String[] args){
        
        GridModel gModel = new GridModel(7, 7);
        gModel.pathFound = false;
       
        gModel.StartingX = 4;
        gModel.StartingY = 0;
        gModel.resetStart(4, 0);
        gModel.GoalX = 4;
        gModel.GoalY = 4;
        
        gModel.cellGrid.get( gModel.findIndex(4,0) ).setType(CellTypes.START);
        gModel.cellGrid.get( gModel.findIndex(4,4) ).setType(CellTypes.END);
        
        while(! ( (gModel.currentDrawPath.x == gModel.StartingX) && (gModel.currentDrawPath.y == gModel.StartingY) ) ){
            gModel.takeStep();
        }
        
        
    }
    */
    
}
