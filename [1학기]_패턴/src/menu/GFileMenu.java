package menu;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import frame.GPanel;
import main.GConstants.EFileMenuItem;
import shapeTools.GShapeTool;

public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	// components
	private File file;
	private File filePath;
	private JMenuItem saveItem;
	private File directory;

	// associations
	private GPanel panel;

	public GFileMenu(String text) {
		super(text);

		ActionHandler actionHandler = new ActionHandler();

		for (EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFileMenuItem.getText());
			menuItem.setActionCommand(eFileMenuItem.name());
			menuItem.addActionListener(actionHandler);
			this.add(menuItem);

			if (eFileMenuItem.getText().equals("열기") || eFileMenuItem.getText().equals("다른이름으로")) {
				this.addSeparator();
			}
		}
		this.file = null;
	}

	public void setAssociation(GPanel panel) {
		this.panel = panel;
	}

	private void openFile() {

		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(file)));

			Vector<GShapeTool> shapes = (Vector<GShapeTool>) objectInputStream.readObject();
			this.panel.setShapes(shapes);
			objectInputStream.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void saveFile() {

		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(this.file)));
			objectOutputStream.writeObject(this.panel.getShapes());
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile() {

		try {
//			System.out.println(file + "호에엥");

			if (file != null) {
				// file에 null일때 조건
				ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream(file)));

				objectOutputStream1.writeObject(panel.getShapes());
				objectOutputStream1.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkSaveOrNot() {
		boolean bCancel = true;
		if (this.panel.isModified()) {
			int reply = JOptionPane.showConfirmDialog(this.panel, "변경내용을 저장 할까요?");
			if (reply == JOptionPane.OK_OPTION) {
				this.save();
				bCancel = false;
			} else if (reply == JOptionPane.NO_OPTION) {
				this.panel.setModified(false);
				bCancel = false;
			} else if (reply == JOptionPane.CANCEL_OPTION) {
				// 암모것도 안함
				bCancel = true;
			} else {
				bCancel = true;
			}
		} else {
			bCancel = false;
		}
		return bCancel;
	}

	// 여기부터
	private void nnew() {
		if (!checkSaveOrNot()) {
			this.panel.clearScreen();
			this.file = null;
		}
	}

	private void open() {
		// if not cancel
		if (!checkSaveOrNot()) {
			JFileChooser chooser = new JFileChooser(this.directory);

			FileNameExtensionFilter filter = new FileNameExtensionFilter("file", "gvs", "file");
			chooser.setFileFilter(filter);

			int returnVal = chooser.showSaveDialog(this.panel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.directory = chooser.getCurrentDirectory();
				this.file = chooser.getSelectedFile();
				this.openFile();
			}
		}
		this.panel.setModified(false);
	}

	private void save() {
		if (this.panel.isModified()) {
			if (this.file == null) {
				this.saveAs();
			} else {
				this.saveFile();
			}
		}
	}

	private void saveAs() {
		// save
		JFileChooser chooser = new JFileChooser(this.directory);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("file", "gvs", "file");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(this.panel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.directory = chooser.getCurrentDirectory();
			this.file = chooser.getSelectedFile();
			this.saveFile();
		}
	}

	public void exitProgram() {
		if (!checkSaveOrNot()) {
			System.exit(0);
		}
	}

	private void capture() {
		try {
			Robot robot = new Robot();
			Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage image = robot.createScreenCapture(rectangle);

			try {
				JFileChooser chooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter("image File", "png", "png");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showSaveDialog(this.panel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					ImageIO.write(image, "png", file);
					JOptionPane.showMessageDialog(null, "수강 리스트를 캡처했습니다", "화면 캡쳐", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (HeadlessException e1) {
			e1.printStackTrace();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EFileMenuItem eMenuItem = EFileMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
			case eNew:
				nnew();
				break;
			case eOpen:
				open();
				break;
			case eSave:
				save();
				break;
			case eSaveAs:
				saveAs();
				break;
			case ePrint:
				panel.print();
				break;
			case eScreanShot:
				capture();
				break;
			case eExit:
				exitProgram();
				break;
			default:
				break;
			}
		}
	}

}
