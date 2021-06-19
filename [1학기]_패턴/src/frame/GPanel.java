package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import main.GConstants.EAction;
import main.GConstants.EDrawingStyle;
import menu.GUndoStack;
import shapeTools.GShapeTool;
import transformer.GMover;
import transformer.GResize;
import transformer.GRotator;
import transformer.GTransformer;

public class GPanel extends JPanel implements Printable {
	// attributes
	private static final long serialVersionUID = 1L;
	private int UnDoCount;
	private int ReDoCount;
	private int tempIndex;
	private int lineWidth;
	private boolean dottedLineCheck;

	// components
	private Vector<Vector<GShapeTool>> unDoSnapShots;
	private Vector<Vector<GShapeTool>> reDoSnapShots;
	private Vector<GShapeTool> shapes;
	private Vector<GShapeTool> editShapes;
	private GMouseHandler mouseHandler;

	private GUndoStack undoStack;
	// associations

	// working objects
	private GShapeTool shapeTool;
	private GShapeTool selectedShape;
	private GShapeTool clonedShape;
	private GTransformer transformer;
	private boolean bModified;
	private JPopupMenu popupMenu;
	private JMenuItem frontBtn;
	private JMenuItem backBtn;

	///////////////////////////////////////////////////////
	// getters and setters
	public Vector<GShapeTool> getShapes() {
		return this.shapes;
	}

	public void setShapes(Vector<GShapeTool> shapes) {
		this.shapes = shapes;
		this.repaint();
	}

	public void setSelection(GShapeTool shapeTool) {
		this.shapeTool = shapeTool;
	}

	public boolean isModified() {
		return this.bModified;
	}

	public boolean setModified(boolean bModified) {
		return this.bModified = bModified;
	}

	public void setFillColor(Color color, String what) {
		Graphics2D graphics2D = (Graphics2D) getGraphics();
		if (what.equals("Shape")) {
			if (this.selectedShape != null) {
				this.selectedShape.setCheckColor(false);
				this.selectedShape.draw(graphics2D);
				this.selectedShape.setColor(color);
				this.selectedShape.draw(graphics2D);
			}
		} else if (what.equals("Line")) {
			if (this.selectedShape != null) {
				this.selectedShape.setCheckColor(true);
				this.selectedShape.draw(graphics2D);
				this.selectedShape.setColor(color);
				this.selectedShape.draw(graphics2D);
			}
		} else if (what.equals("Panel")) {
			this.setBackground(color);
		}
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public void setLineShape(boolean dottedLineCheck) {
		this.dottedLineCheck = dottedLineCheck;
	}

	// constructors
	public GPanel() {
		this.shapes = new Vector<GShapeTool>();
		this.editShapes = new Vector<GShapeTool>();
		this.unDoSnapShots = new Vector<Vector<GShapeTool>>();
		this.reDoSnapShots = new Vector<Vector<GShapeTool>>();

		this.mouseHandler = new GMouseHandler();
		this.addMouseListener(this.mouseHandler);
		this.addMouseMotionListener(this.mouseHandler);
		this.addMouseWheelListener(this.mouseHandler);

		this.undoStack = new GUndoStack();

		this.bModified = false;

		this.popupMenu = new JPopupMenu();
		this.frontBtn = new JMenuItem("맨 앞으로");
		this.backBtn = new JMenuItem("맨 뒤로");

		this.popupMenu.add(frontBtn);
		this.popupMenu.add(backBtn);

	}

	public void initialize() {
		this.setBackground(Color.WHITE);
	}

	public void clearScreen() {
		this.shapes.clear();
		this.repaint();
	}

	// methods

	public Vector<GShapeTool> deepCopy(Vector<GShapeTool> original) {
		Vector<GShapeTool> clonedShapes = (Vector<GShapeTool>) this.shapes.clone();

		for (int i = 0; i < this.shapes.size(); i++) {
			clonedShapes.set(i, (GShapeTool) this.shapes.get(i).clone());
		}

		return clonedShapes;
	}

//	public void undo() {
//		this.shapes = this.deepCopy(this.undoStack.undo());
//		this.repaint();
//	}
//
//	public void redo() {
//		this.shapes = this.deepCopy(this.undoStack.redo());
//		this.repaint();
//	}

	public void paint(Graphics graphics) {
		super.paint(graphics);

		for (GShapeTool shape : this.shapes) {
			shape.draw((Graphics2D) graphics);
		}
	}

	private void setSelected(GShapeTool selectedShape) {
		for (GShapeTool shape : this.shapes) {
			shape.setSelected(false);
		}

		this.selectedShape = selectedShape;
		this.selectedShape.setSelected(true);
		this.repaint();
	}

	private GShapeTool onShape(int x, int y) {
		for (GShapeTool shape : this.shapes) {
			EAction eAction = shape.containes(x, y);

			if (eAction != null) {
				return shape;
			}
		}
		return null;
	}

	private void initDrawing(int x, int y) {
		this.selectedShape = (GShapeTool) this.shapeTool.clone();
		this.selectedShape.setInitPoint(x, y);

		this.selectedShape.setLineWidth(this.lineWidth);
		this.selectedShape.setLineShape(this.dottedLineCheck);
	}

	private void setIntermediatePoint(int x, int y) {
		this.selectedShape.setIntermediatePoint(x, y);
	}

	private void keepDrawing(int x, int y) {
		// exclusive or mode
		Graphics2D graphics2D = (Graphics2D) getGraphics();
		graphics2D.setXORMode(getBackground());
		// erase
		selectedShape.animate(graphics2D, x, y);
	}

	private void finishDrawing(int x, int y) {
		this.selectedShape.setFinalPoint(x, y);
		this.shapes.add(this.selectedShape);
		this.bModified = true;

		this.UnDoCount++;
		this.unDoSnapShots.add(this.shapes);
		this.tempIndex = unDoSnapShots.size();
	}

	private void initTransforming(GShapeTool selectedShape, int x, int y) {

		this.selectedShape = (GShapeTool) this.shapeTool.clone();
		this.selectedShape = selectedShape;
		EAction eAction = this.selectedShape.getAction();
		switch (eAction) {
		case eMove:
			this.transformer = new GMover(this.selectedShape);
			break;
		case eResize:
			this.transformer = new GResize(this.selectedShape);
			break;
		case eRotate:
			this.transformer = new GRotator(this.selectedShape);
			break;
		default:
			break;
		}
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(this.getBackground());
		this.transformer.initTransforming(graphics2d, x, y);

	}

	private void keepTransforming(int x, int y) {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(this.getBackground());
		this.transformer.keepTransforming(graphics2d, x, y);
		repaint();
	}

	private void finishTransforming(int x, int y) {

		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(this.getBackground());
		this.transformer.finishTransforming(graphics2d, x, y);

		this.setSelected(this.selectedShape);
		this.bModified = true;

		this.unDoSnapShots.add(this.shapes);
	}

	// 뒤로가기
	public void unDo() {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();

//			// 벡터에 담긴 그림 다시 paint
		Vector<GShapeTool> temp = new Vector<GShapeTool>();
		for (GShapeTool shape : this.shapes) {
			temp.add(shape);
		}
		if (UnDoCount == 0) {
			return;
		} else {
			this.paint(graphics2d);
			this.reDoSnapShots.add(this.shapes);

			temp.remove(temp.lastElement());
			this.shapes = temp;
			this.paint(graphics2d);
			UnDoCount--;
			ReDoCount++;
		}
	}

	// 앞으로 가기
	public void reDo() {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();

		Vector<GShapeTool> temp = new Vector<GShapeTool>();
		for (GShapeTool shape : shapes) {
			temp.add(shape);
		}

		if (ReDoCount == 0) {
			return;
		} else {

			this.paint(graphics2d);
			Vector<GShapeTool> reDoVector = new Vector<GShapeTool>();

			if (reDoSnapShots.size() == 0) {
				return;
			} else {
				reDoVector = this.reDoSnapShots.lastElement();
				this.reDoSnapShots.remove(this.reDoSnapShots.lastElement());

				this.shapes = reDoVector;
				this.paint(graphics2d);
			}
		}
	}

	// 컨트롤 씨
	public void copy() {
		if (selectedShape != null) {
			this.clonedShape = (GShapeTool) this.selectedShape.clone();
		}
	}

	public void delete() {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		GShapeTool currentShape = this.selectedShape;

		this.shapes.remove(currentShape);
		this.editShapes = this.shapes;
		this.paint(graphics2d);
	}

	public void cut() {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		this.clonedShape = this.selectedShape;
		this.shapes.remove(this.clonedShape);

		this.paint(graphics2d);
	}

	public void paste() {
		if (this.clonedShape != null) {
			Graphics2D graphics2d = (Graphics2D) this.getGraphics();
			this.shapes.add(this.clonedShape);

			this.paint(graphics2d);
		}
	}

	public void print() {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean isPrinted = job.printDialog();
		if (isPrinted) {
			try {
				job.print();
			} catch (PrinterException e) {
				JOptionPane.showMessageDialog(this, "프린트할 수 없음!");
			}
		}
	}

	public void popup() {
		System.out.println("하이룽");

		ActionHandler actionHandler = new ActionHandler();
		this.frontBtn.addActionListener(actionHandler);
		this.backBtn.addActionListener(actionHandler);
	}

	public void front() {

		if (this.selectedShape != null) {
			GShapeTool currentShape = this.selectedShape;

			this.shapes.remove(this.selectedShape);
			this.shapes.add(currentShape);
			repaint();
		}
	}

	public void back() {
		if (this.selectedShape != null) {
			GShapeTool currentShape = this.selectedShape;
			this.shapes.removeElement(this.selectedShape);

			Vector<GShapeTool> temp = new Vector<GShapeTool>();
			for (GShapeTool shape : shapes) {
				temp.add(shape);
			}
			this.shapes.removeAllElements();
			this.shapes.add(currentShape);

			for (GShapeTool shape : temp) {
				this.shapes.add(shape);
			}
			repaint();
		}
	}

	///////////////////////////////////////////////////////
	// inner classes
	private class GMouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

		private boolean isDrawing;
		private boolean isTransforming;

		public GMouseHandler() {
			this.isDrawing = false;
			this.isTransforming = false;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (!isDrawing) {
				GShapeTool selectedShape = onShape(e.getX(), e.getY());
				if (selectedShape == null) {
					if (shapeTool.getDrawingStyle() == EDrawingStyle.e2PointDrawing) {
						initDrawing(e.getX(), e.getY());
						this.isDrawing = true;
					}
				} else {
					initTransforming(selectedShape, e.getX(), e.getY());
					this.isTransforming = true;
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (isDrawing) {
				if (shapeTool.getDrawingStyle() == EDrawingStyle.e2PointDrawing) {
					keepDrawing(e.getX(), e.getY());
				}
			} else if (this.isTransforming) {
				keepTransforming(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isDrawing) {
				if (shapeTool.getDrawingStyle() == EDrawingStyle.e2PointDrawing) {
					finishDrawing(e.getX(), e.getY());
					this.isDrawing = false;
				}
			} else if (this.isTransforming) {
				finishTransforming(e.getX(), e.getY());
				this.isTransforming = false;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (isDrawing) {
				if (shapeTool.getDrawingStyle() == EDrawingStyle.eNPointDrawing) {
					keepDrawing(e.getX(), e.getY());
				}
			}
		}

		private void mouseLButton1Clicked(MouseEvent e) {
			if (!isDrawing) {
				GShapeTool selectedShape = onShape(e.getX(), e.getY());
				if (selectedShape == null) {
					if (shapeTool.getDrawingStyle() == EDrawingStyle.eNPointDrawing) {
						initDrawing(e.getX(), e.getY());
						this.isDrawing = true;
					}
				} else {
					setSelected(selectedShape);
				}
			} else {
				if (shapeTool.getDrawingStyle() == EDrawingStyle.eNPointDrawing) {
					setIntermediatePoint(e.getX(), e.getY());
				}
			}
		}

		private void mouseLButton2Clicked(MouseEvent e) {
			if (isDrawing) {
				if (shapeTool.getDrawingStyle() == EDrawingStyle.eNPointDrawing) {
					finishDrawing(e.getX(), e.getY());
					this.isDrawing = false;
				}
			}
		}

		private void mouseRButton1Clicked(MouseEvent e) {
			GShapeTool selectedShape = onShape(e.getX(), e.getY());
			if (selectedShape != null) {
				popup();
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1) {
					this.mouseLButton1Clicked(e);
				} else if (e.getClickCount() == 2) {
					this.mouseLButton2Clicked(e);
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				if (e.getClickCount() == 1) {
					this.mouseRButton1Clicked(e);
				}
			}
		}
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "맨 앞으로") {
				front();
			} else if (e.getActionCommand() == "맨 뒤로") {
				back();
			}
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		for (GShapeTool shape : this.shapes) {
			shape.draw(graphics2d);
		}
		return PAGE_EXISTS;
	}

}
