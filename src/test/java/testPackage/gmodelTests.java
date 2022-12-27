
package testPackage;

import crz.astarjpath.AStartModel;
import crz.astarjpath.GridModelResources.Cell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 *
 * @author LeothEcRz
 */
public class gmodelTests {
    
    private static AStartModel gModel;
    
    public gmodelTests() {
    }

    @BeforeAll
    public static void setUpClass(){
        gModel = new AStartModel(5, 5);
    }

    @AfterAll
    public static void tearDownClass(){
        gModel = null;
    }

    @BeforeEach
    public void setUp(){
    }

    @AfterEach
    public void tearDown(){
    }

    @Test
    public void gModelStartCheck(){
        AStartModel GM = new AStartModel(10,5);
        boolean setupOk = !GM.isEndSet;
        if(GM.isStartSet){
            setupOk = false;
        }
        if(GM.pathFound){
            setupOk = false;
        }
        if(GM.pathDrawn){
            setupOk = false;
        }
        assertTrue(setupOk);
    }

    @Test
    public void gModelCorrectWidthCheck(){
        assertEquals(5, gModel.width);
    }
    
    @Test
    public void gModelCorrectHeightCheck(){
       assertEquals(5, gModel.height);
    }
    
    @Test
    public void gModelCorrectCellArrayCheck(){
        assertEquals(25, (gModel.cellGrid).size());
    }

    @Test
    public void gModelFinalIndexMethodCheck(){
        int x = 4;
        int y = 4;
        int index = gModel.findIndex(x,y);
        int correct = (gModel.width * y) + x;
        assertEquals(correct, index);
    }

    @Test
    public void gModelSetTypeToCheck(){
        int x = 4;
        int y = 4;
        gModel.setTypeTo(x,y, Cell.CellTypes.GRIDWALL);
        boolean test = gModel.cellGrid.get(gModel.findIndex(x,y)).getType().equals(Cell.CellTypes.GRIDWALL);
        assertTrue(test);
    }

    @Test
    public void gModelSetTypeToCheck_TWO(){
        int x = 4;
        int y = 4;
        gModel.setTypeTo(gModel.findIndex(x,y), Cell.CellTypes.END);
        boolean test = gModel.cellGrid.get(gModel.findIndex(x,y)).getType().equals(Cell.CellTypes.END);
        assertTrue(test);
    }

    @Test
    public void gModelResetStartCheck(){
        gModel.resetStart(1,2);
        boolean setupOK = gModel.isStartSet;
         if(1 != gModel.StartingX)
            setupOK=false;
        if(2 != gModel.StartingY)
            setupOK=false;

        assertTrue(setupOK);

    }

}
