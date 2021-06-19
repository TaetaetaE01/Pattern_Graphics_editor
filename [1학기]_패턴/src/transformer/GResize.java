package transformer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import shapeTools.GShapeTool;

public class GResize extends GTransformer {
	private Point2D resizeOrigin;
	private double cx, cy;

	public GResize(GShapeTool selectedShape) {
		super(selectedShape);
	}

	public Point2D computeResizeMethod() {
		double deltaW = 0;
		double deltaH = 0;

		double width = this.selectedShape.getWidth();
		double height = this.selectedShape.getHeight();

		switch (this.selectedShape.getEAnchors()) {
		// NW
		case x0y0:
			deltaW = -(cx - px);
			deltaH = -(cy - py);
			break;
		// N
		case x1y0:
			deltaW = 0;
			deltaH = -(cy - py);
			break;
		// NE
		case x2y0:
			deltaW = cx - px;
			deltaH = -(cy - py);
			break;
		// W
		case x0y1:
			deltaW = -(cx - px);
			deltaH = 0;
			break;
		// E
		case x2y1:
			deltaW = cx - px;
			deltaH = 0;
			break;
		// SW
		case x0y2:
			deltaW = -(cx - px);
			deltaH = cy - py;
			break;
		// S
		case x1y2:
			deltaW = 0;
			deltaH = cy - py;
			break;
		// SE
		case x2y2:
			deltaW = cx - px;
			deltaH = cy - py;
			break;
		default:
			break;
		}

		double xRatio = 1.0;
		double yRatio = 1.0;

		if (width > 0.0)
			xRatio = (xRatio + (deltaW / width));

		if (height > 0.0)
			yRatio = (yRatio + (deltaH / height));

		return new Point.Double(xRatio, yRatio);
	}

	public void transform(Graphics2D graphics2d, int x, int y) {
		this.cx = x;
		this.cy = y;

		this.resizeOrigin = this.selectedShape.getResizerOrigin();
		Point2D resizeRtio = computeResizeMethod();
		this.selectedShape.resize(resizeRtio, resizeOrigin, graphics2d, x, y);
	}

	@Override
	public void initTransforming(Graphics2D graphics2d, int x, int y) {
		this.px = x;
		this.py = y;
	}

	@Override
	public void keepTransforming(Graphics2D graphics2d, int x, int y) {
		// 이자리에 셀렉티드 ShapeTool
		this.transform(graphics2d, x, y);
		this.px = x;
		this.py = y;
	}
}
