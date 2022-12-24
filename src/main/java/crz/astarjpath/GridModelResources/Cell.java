

package crz.astarjpath.GridModelResources;

/**
 * 
 * @author LeothEcRz
 */
public final class Cell {
    
    /**
     * Possible States of the Cell
     */
    public static enum CellTypes{
        EMPTY, START, END, GRIDWALL, UNCHECKED, CHECKED, PATH
    };
    
    private boolean inOpenSet;
    private CellTypes type;
    private final int x;
    private final int y;

    private double G_Cost = Double.POSITIVE_INFINITY;
    private double H_Cost = Double.POSITIVE_INFINITY;
    private double F_Cost = Double.POSITIVE_INFINITY;
    
    private Integer cameFromIndex;
    
    /**
     * Default Constructor
     * @param x
     * @param y 
     */
    public Cell(int x, int y){
        this.x = x;
        this.y = y;
        type = CellTypes.EMPTY;
        inOpenSet = false;
        cameFromIndex = null;
    }

    public Integer getCameFromIndex(){
        if(this.cameFromIndex == null)
            return -1;
        return this.cameFromIndex;
    }    
    
    public void setCameFromIndex(Integer camefrom){
        this.cameFromIndex = camefrom;
    }
    
    /**
     * Update F Cost. Because of cell change to G or H cost.
     */
    public void updateFCost(){
        F_Cost = (G_Cost + H_Cost);
    }

    public void setG_Cost(double G_Cost) {
        this.G_Cost = G_Cost;
    }
    
    public void setF_Cost(double F_Cost) {
        this.F_Cost = F_Cost;
    }

    public void setH_Cost(double H_Cost) {
        this.H_Cost = H_Cost;
    }
    
    public double getF_Cost() {
        return F_Cost;
    }

    public double getG_Cost() {
        return G_Cost;
    }

    public double getH_Cost() {
        return H_Cost;
    }

    public CellTypes getType() {
        return type;
    }

    public void setType(CellTypes type) {
        this.type = type;
    }

    public boolean isInOpenSet() {
        return inOpenSet;
    }

    public void setInOpenSet(boolean inOpenSet) {
        this.inOpenSet = inOpenSet;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
