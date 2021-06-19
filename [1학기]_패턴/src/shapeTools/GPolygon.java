package shapeTools;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import main.GConstants.EDrawingStyle;

public class GPolygon extends GShapeTool {
	private static final long serialVersionUID = 1L;

	public GPolygon() {
		super(EDrawingStyle.eNPointDrawing);
		this.shape = new Polygon();
	}

	@Override
	public GShapeTool newInstance() {
		return new GPolygon();
	}

	public Object clone() {
		GShapeTool cloned = (GShapeTool) super.clone();
		Polygon polygon = new Polygon();
		// 루핑돌려서 좌표를 다 옮기기
		cloned.shape = polygon;
//		cloned.shape = (Shape) ((Polygon) (this.shape)).clone();
		return cloned;
	}

	@Override
	public void setInitPoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.addPoint(x, y);
		polygon.addPoint(x, y);
	}

	public void setIntermediatePoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.addPoint(x, y);
	}

	@Override
	public void setFinalPoint(int x, int y) {
	}

	@Override
	public void movePoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.xpoints[polygon.npoints - 1] = x;
		polygon.ypoints[polygon.npoints - 1] = y;
	}

}
