package crz.astarjpath.GridModelResources;

import java.util.Comparator;

public class CellFCostComparator implements Comparator<Cell>{
    @Override
    public int compare(Cell o1, Cell o2) {
        if(o1.getF_Cost() > o2.getF_Cost())
                return 1;
            return 0; 
    }
}
