package shapeTools;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Line2D.Double;

import main.GConstants.EDrawingStyle;

public class GRoundRectangle extends GShapeTool {

	public GRoundRectangle() {
		super(EDrawingStyle.e2PointDrawing);
		this.shape = new RoundRectangle2D.Double();
	}
	
	public Object clone() {
		GShapeTool cloned = null;
		cloned = (GShapeTool) super.clone();
		cloned.shape = (Shape) (((RoundRectangle2D) this.shape)).clone();
		return cloned;
	}

	@Override
	public GShapeTool newInstance() {
		return new GRoundRectangle();
	}

	@Override
	public void setInitPoint(int x, int y) {
		RoundRectangle2D roundRectangle = (RoundRectangle2D.Double) this.shape;
		roundRectangle.setRoundRect(x, y, 0, 0, 50, 50);
	}

	@Override
	public void setFinalPoint(int x, int y) {
	}

	@Override
	public void movePoint(int x, int y) {
		RoundRectangle2D roundRectangle = (RoundRectangle2D.Double) this.shape;
		roundRectangle.setRoundRect(roundRectangle.getX(), roundRectangle.getY(), x - roundRectangle.getX(),
				y - roundRectangle.getY(), 50, 50);
	}

}
