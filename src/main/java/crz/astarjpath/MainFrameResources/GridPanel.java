
package crz.astarjpath.MainFrameResources;

import javax.swing.JPanel;

import crz.astarjpath.dialogResources.SetupSizes;

import java.awt.Color;
import java.awt.event.MouseListener;

public final class GridPanel extends JPanel{

    private final static int PANEL_BORDER = 2;
    private final static Color defColor = Color.LIGHT_GRAY;

    private final int i_point;
    private final int j_point;

    public GridPanel( SetupSizes setupSizes, int i, int j,  MouseListener  mListener ){
        super();
        this.setBackground(defColor);
        this.setBounds( j*setupSizes.pixelPerCell + 1, i*setupSizes.pixelPerCell + 1, setupSizes.pixelPerCell-PANEL_BORDER, setupSizes.pixelPerCell-PANEL_BORDER);
        this.i_point = i;
        this.j_point = j;
        this.addMouseListener(mListener);

    }

    public int getIPoint(){
        return this.i_point;
    }

    public int getJPoint(){
        return this.j_point;
    }

}