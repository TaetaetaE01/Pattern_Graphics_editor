package shapeTools;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import main.GConstants.EDrawingStyle;

public class GArc extends GShapeTool {

	public GArc() {
		super(EDrawingStyle.e2PointDrawing);
		this.shape = new Arc2D.Double();
	}

	public Object clone() {
		GShapeTool cloned = null;
		cloned = (GShapeTool) super.clone();
		cloned.shape = (Shape) (((Arc2D) this.shape)).clone();
		return cloned;
	}

	@Override
	public GShapeTool newInstance() {
		return new GArc();
	}

	@Override
	public void setInitPoint(int x, int y) {
		Arc2D arc = (Arc2D) this.shape;
		arc.setArc(x, y, 0, 0, 0, 0, Arc2D.OPEN);
	}

	@Override
	public void setFinalPoint(int x, int y) {
	}

	@Override
	public void movePoint(int x, int y) {
		Arc2D arc = (Arc2D) this.shape;
		arc.setArc(arc.getX(), arc.getY(), x - arc.getX(), y - arc.getY(), 90, 210, Arc2D.PIE);
	}

}
