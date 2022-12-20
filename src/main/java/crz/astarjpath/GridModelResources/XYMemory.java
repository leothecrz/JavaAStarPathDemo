
package crz.astarjpath.GridModelResources;

/**
 *
 * @author LeothEcRz
 */
public class XYMemory {
    
    public int x;
    public int y;
    public int distance;
    
    public XYMemory(int x, int y, int d){
        this.x = x;
        this.y = y;
        this.distance = d;
    }
    
    public XYMemory(){
        this(0, 0, 0);
    }
    
}
