package shapeTools;

import java.awt.Shape;
import java.awt.geom.Line2D;

import main.GConstants.EDrawingStyle;

public class GPen extends GShapeTool {
	private static final long serialVersionUID = 1L;

	public GPen() {
		super(EDrawingStyle.e2PointDrawing);
		this.shape = new Line2D.Double();
	}

	public Object clone() {
		GShapeTool cloned = null;
		cloned = (GShapeTool) super.clone();
		cloned.shape = (Shape) (((Line2D) this.shape)).clone();
		return cloned;
	}

	@Override
	public GShapeTool newInstance() {
		return new GLine();
	}

	// methods
	@Override
	public void setInitPoint(int x, int y) {
		this.penDraw = true;
		Line2D line = (Line2D) this.shape;
		this.vX.add(x);
		this.vY.add(y);
		line.setLine(x, y, x, y);
		
	}

	@Override
	public void setFinalPoint(int x, int y) {
		this.penDraw = false;
	}

	@Override
	public void movePoint(int x, int y) {
		this.penDraw = true;
		Line2D line = (Line2D) this.shape;
		this.vX.add(x);
		this.vY.add(y);

//		for (int i = 1; i < vX.size(); i++) {
//			line.setLine(vX.get(i - 1), vY.get(i - 1), vX.get(i), vY.get(i));
//		}
	}
}
