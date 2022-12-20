
package crz.astarjpath.dialogResources;

public class SetupSizes {
    public int pixelPerCell;
    public int horizontalCellCount;
    public int verticalCellCount;
    
    public SetupSizes(int PPC, int HCC, int VCC){
        pixelPerCell = PPC;
        horizontalCellCount = HCC;
        verticalCellCount = VCC;
    }

    @Override
    public String toString() {
 
        StringBuilder SB = new StringBuilder();
        SB.append("\n Pixel Per Cell: ");
        SB.append(String.valueOf(pixelPerCell));
        
        SB.append("\n H Count: ");
        SB.append(String.valueOf(horizontalCellCount));
        
        SB.append("\n V Count: ");
        SB.append(String.valueOf(verticalCellCount));
        
        return SB.toString();
        
    }
    
}
