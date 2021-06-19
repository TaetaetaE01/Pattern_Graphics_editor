package shapeTools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Vector;

import main.GConstants;
import main.GConstants.EAction;
import main.GConstants.EDrawingStyle;
import shapeTools.GShapeTool.EAnchors;

abstract public class GShapeTool implements Serializable, Cloneable {
	// attributes
	private static final long serialVersionUID = 1L;

	public enum EAnchors {
		x0y0, x0y1, x0y2, x1y0, x1y2, x2y0, x2y1, x2y2, RR
	}

	private EDrawingStyle eDrawingStyle;
	protected Shape shape;
	protected Vector<Integer> vX;
	protected Vector<Integer> vY;
	protected boolean penDraw;
	private Color color;
	private Ellipse2D[] anchors;
	private boolean isSelected;
	private EAnchors selectedAnchor;
	private EAction eAction;
	private AffineTransform affineTransform;
	private boolean colorCheck;
	private int lineWidth;
	private boolean dottedLineCheck;
	private float dash5[] = { 1, 5f };

	// working variables

	// constructors
	public GShapeTool(EDrawingStyle eDrawingState) {
		this.anchors = new Ellipse2D.Double[EAnchors.values().length];
		for (EAnchors eAnchor : EAnchors.values()) {
			this.anchors[eAnchor.ordinal()] = new Ellipse2D.Double();
		}
		this.isSelected = false;
		this.eDrawingStyle = eDrawingState;
		this.selectedAnchor = null;

		this.affineTransform = new AffineTransform();
		this.affineTransform.setToIdentity();

		this.vX = new Vector<Integer>();
		this.vY = new Vector<Integer>();

	}

	public Object clone() {
		GShapeTool cloned = null;
		try {
			cloned = (GShapeTool) super.clone();
			for (EAnchors eAnchor : EAnchors.values()) {
				cloned.anchors[eAnchor.ordinal()] = (Ellipse2D) this.anchors[eAnchor.ordinal()].clone();
			}
			cloned.affineTransform = (AffineTransform) this.affineTransform.clone();

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return cloned;
	}

	// getters & setters
	public EDrawingStyle getDrawingStyle() {
		return this.eDrawingStyle;
	}

	public EAction getAction() {
		return this.eAction;
	}

	// 어떤 앵커를 선택했는지
	public EAnchors getEAnchors() {
		return this.selectedAnchor;
	}

	// 넓이
	public double getWidth() {
		return this.shape.getBounds2D().getWidth();
	}

	// 높이
	public double getHeight() {
		return this.shape.getBounds2D().getHeight();
	}

	// 앵커
	public Ellipse2D[] getAnchors() {
		return anchors;
	}

	// 도형 set
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	// 도형 get
	public Shape getShape() {
		return this.shape;
	}

	// 색 set
	public void setColor(Color color) {
		this.color = color;
	}

	// 색 check
	public void setCheckColor(boolean colorCheck) {
		this.colorCheck = colorCheck;
	}

	// 선 두께
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	// 선 모양 설정
	public void setLineShape(boolean dottedLineCheck) {
		this.dottedLineCheck = dottedLineCheck;
	}

	// 도형 중앙값 x
	public double getCenterX() {
		return this.shape.getBounds2D().getCenterX();
	}

	// 도형 중앙값 y
	public double getCenterY() {
		return this.shape.getBounds2D().getCenterY();
	}

	public Point2D getResizerOrigin() {
		Point2D p = new Point2D.Double();

		// 다시
		Ellipse2D[] anchorss = this.getAnchors();
//		System.out.println(selectedAnchor + "너냥");

		switch (selectedAnchor) {
		// NW
		case x0y0:
			p.setLocation(anchorss[EAnchors.x2y2.ordinal()].getCenterX(),
					anchorss[EAnchors.x2y2.ordinal()].getCenterY());
			break;
		// N
		case x1y0:
			p.setLocation(0, anchorss[EAnchors.x1y2.ordinal()].getCenterY());
			break;
		// NE
		case x2y0:
			p.setLocation(anchorss[EAnchors.x0y2.ordinal()].getCenterX(),
					anchorss[EAnchors.x0y2.ordinal()].getCenterY());
			break;
		// W
		case x0y1:
			p.setLocation(anchorss[EAnchors.x2y1.ordinal()].getCenterX(), 0);
			break;
		// E
		case x2y1:
			p.setLocation(anchorss[EAnchors.x0y1.ordinal()].getCenterX(), 0);
			break;
		// SW
		case x0y2:
			p.setLocation(anchorss[EAnchors.x2y0.ordinal()].getCenterX(),
					anchorss[EAnchors.x2y0.ordinal()].getCenterY());
			break;
		// S
		case x1y2:
			p.setLocation(0, anchorss[EAnchors.x1y0.ordinal()].getCenterY());
			break;
		// SE
		case x2y2:
			p.setLocation(anchorss[EAnchors.x0y0.ordinal()].getCenterX(),
					anchorss[EAnchors.x0y0.ordinal()].getCenterY());
			break;

		default:
			break;
		}
		return p;
	}

	// methods
	public EAction containes(int x, int y) {
		this.eAction = null;
		if (this.isSelected) {
			for (int i = 0; i < this.anchors.length - 1; i++) {
				if (this.affineTransform.createTransformedShape(anchors[i]).contains(x, y)) {
					this.selectedAnchor = EAnchors.values()[i];
					this.eAction = EAction.eResize;
				}
			}
			if (this.affineTransform.createTransformedShape(this.anchors[EAnchors.RR.ordinal()]).contains(x, y)) {
				this.eAction = EAction.eRotate;
			}
		}
		if (this.affineTransform.createTransformedShape(this.shape).contains(x, y)) {
			this.eAction = EAction.eMove;
		}
		return this.eAction;
	}

//	public boolean lineContains(int x, int y) {
//		Rectangle rectangle = this.shape.getBounds();
//		if (this.isSelected) {
//			if (x > rectangle.getX() + 5 && x < (rectangle.getX() + rectangle.getWidth() - 5)
//					&& y > rectangle.getY() + 5 && y < (rectangle.getY() + rectangle.getHeight()) - 5) {
//				System.out.println("호잉");
//				return true;
//			}
//		}
//		return false;
//	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void move(Graphics2D graphics2d, int dx, int dy) {
		int tempX = dx;
		int tempY = dy;

		this.draw(graphics2d);
		this.affineTransform.translate(tempX, tempY);
		this.draw(graphics2d);
	}

	// resize
	public void resize(Point2D resizeRatio, Point2D resizeOrigin, Graphics2D graphics2d, int x, int y) {

		this.draw(graphics2d);

		this.affineTransform.setToTranslation(resizeOrigin.getX(), resizeOrigin.getY());
		this.affineTransform.scale(resizeRatio.getX(), resizeRatio.getY());
		this.affineTransform.translate(-resizeOrigin.getX(), -resizeOrigin.getY());

		this.setShape(this.affineTransform.createTransformedShape(this.shape));
//		this.shape = affineTransform.createTransformedShape(this.shape);

		this.draw(graphics2d);
	}

	// rotate
	public void rotate(Graphics2D graphics2d, Point pStart, Point pEnd) {

		this.draw(graphics2d);
		double centerX = this.shape.getBounds().getCenterX();
		double centerY = this.shape.getBounds().getCenterY();

		double statAngle = Math.toDegrees(Math.atan2(centerX - pStart.x, centerY - pStart.y));
		double endAngle = Math.toDegrees(Math.atan2(centerX - pEnd.x, centerY - pEnd.y));

		double rotationAngle = statAngle - endAngle;

		if (rotationAngle < 0) {
			rotationAngle += 360;
		}

		this.affineTransform.rotate(Math.toRadians(rotationAngle), centerX, centerY);
		this.draw(graphics2d);
	}

	public void drawAnchors(Graphics2D graphics) {
		int wAnchor = GConstants.wAnchor;
		int hAnchor = GConstants.hAnchor;

		Rectangle rectangle = this.shape.getBounds();
		int x0 = rectangle.x - wAnchor / 2;
		int x1 = rectangle.x - wAnchor / 2 + (rectangle.width) / 2;
		int x2 = rectangle.x - wAnchor / 2 + rectangle.width;
		int y0 = rectangle.y - hAnchor / 2;
		int y1 = rectangle.y - hAnchor / 2 + (rectangle.height) / 2;
		int y2 = rectangle.y - hAnchor / 2 + rectangle.height;

		this.anchors[EAnchors.x0y0.ordinal()].setFrame(x0, y0, wAnchor, hAnchor);
		this.anchors[EAnchors.x0y1.ordinal()].setFrame(x0, y1, wAnchor, hAnchor);
		this.anchors[EAnchors.x0y2.ordinal()].setFrame(x0, y2, wAnchor, hAnchor);
		this.anchors[EAnchors.x1y0.ordinal()].setFrame(x1, y0, wAnchor, hAnchor);
		this.anchors[EAnchors.x1y2.ordinal()].setFrame(x1, y2, wAnchor, hAnchor);
		this.anchors[EAnchors.x2y0.ordinal()].setFrame(x2, y0, wAnchor, hAnchor);
		this.anchors[EAnchors.x2y1.ordinal()].setFrame(x2, y1, wAnchor, hAnchor);
		this.anchors[EAnchors.x2y2.ordinal()].setFrame(x2, y2, wAnchor, hAnchor);
		this.anchors[EAnchors.RR.ordinal()].setFrame(x1, y0 - 40, wAnchor, hAnchor);

		for (EAnchors eAnchor : EAnchors.values()) {
			Color color = graphics.getColor();
			graphics.setColor(Color.WHITE);
			graphics.fill(this.affineTransform.createTransformedShape(this.anchors[eAnchor.ordinal()]));
			graphics.setColor(color);
			graphics.draw(this.affineTransform.createTransformedShape(this.anchors[eAnchor.ordinal()]));
		}
	}

	public void draw(Graphics2D graphics2d) {
		Color temp = graphics2d.getColor();
		if (this.colorCheck == false) {
			// 도형 안에 색칠하기
			if (this.color != null) {
				graphics2d.setColor(color);

				graphics2d.fill(this.affineTransform.createTransformedShape(this.shape));
				graphics2d.setColor(temp);
			}
			// 앵커 그리기
			if (isSelected) {
				this.drawAnchors(graphics2d);
			}

			// 도형그리는건데 -> 검은색으로 아예 막아 버림
			graphics2d.setColor(Color.black);
			// 선 두께, 선 모양 관리
			if (dottedLineCheck == true) {
				graphics2d.setStroke(
						new BasicStroke(this.lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, dash5, 0));
			} else {
				graphics2d.setStroke(new BasicStroke(this.lineWidth, BasicStroke.CAP_BUTT, 0));
			}
			graphics2d.draw(this.affineTransform.createTransformedShape(this.shape));
		} else {
			// 앵커 그리기
			if (isSelected) {
				this.drawAnchors(graphics2d);
			}
			graphics2d.setColor(color);
			// 선 두께,선 모양 관리
			if (dottedLineCheck == true) {
				graphics2d.setStroke(
						new BasicStroke(this.lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, dash5, 0));
			} else {
				graphics2d.setStroke(new BasicStroke(this.lineWidth, BasicStroke.CAP_BUTT, 0));
			}
			graphics2d.draw(this.affineTransform.createTransformedShape(this.shape));
		}
	}

	public void penDraw(Graphics2D graphics2d) {
		Color temp = graphics2d.getColor();
		if (this.colorCheck == false) {
			if (this.color != null) {
				graphics2d.setColor(color);
				graphics2d.fill(this.affineTransform.createTransformedShape(this.shape));
				graphics2d.setColor(temp);
			}
			graphics2d.setColor(Color.black);
			if (dottedLineCheck == true) {
				graphics2d.setStroke(
						new BasicStroke(this.lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, dash5, 0));
			} else {
				graphics2d.setStroke(new BasicStroke(this.lineWidth, BasicStroke.CAP_BUTT, 0));
			}
			for (int i = 1; i < vX.size(); i++) {
				graphics2d.drawLine(vX.get(i - 1), vY.get(i - 1), vX.get(i), vY.get(i));
			}
		} else {

		}
	}

	public void animate(Graphics2D graphics2d, int x, int y) {
		if (penDraw) {
			this.penDraw(graphics2d);
			this.movePoint(x, y);
			this.penDraw(graphics2d);
		}
		this.draw(graphics2d);
		this.movePoint(x, y);
		this.draw(graphics2d);
	}

	// interface
	public abstract GShapeTool newInstance();

	public abstract void setInitPoint(int x, int y);

	public void setIntermediatePoint(int x, int y) {
	}

	public abstract void setFinalPoint(int x, int y);

	public abstract void movePoint(int x, int y);

}
