package menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import frame.GPanel;
import main.GConstants.EColorMenuItem;

public class GColorMenu extends JMenu {

	private GPanel panel;

	public GColorMenu(String text) {
		super(text);

		ActionHandler actionHandler = new ActionHandler();

		for (EColorMenuItem eColorMenuItem : EColorMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eColorMenuItem.getText());
			menuItem.setActionCommand(eColorMenuItem.name());
			menuItem.setToolTipText(eColorMenuItem.getShortcut());
			menuItem.addActionListener(actionHandler);
			this.add(menuItem);
			
			switch (eColorMenuItem.getText()) {
			case "색 채우기":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
				break;
				
			case "선 색 바꾸기":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
				break;
				
			case "배경색 바꾸기":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
				break;
			}
		}
	}

	public void setAssociation(GPanel panel) {
		this.panel = panel;
	}

	public void fillColorShape() {
		Color color = JColorChooser.showDialog(this.panel, "Fill Color", this.getForeground());
		this.panel.setFillColor(color, "Shape");
	}

	public void fillColorLine() {
		Color color = JColorChooser.showDialog(this.panel, "Fill Color", this.getForeground());
		this.panel.setFillColor(color, "Line");
	}

	public void fillColorPanel() {
		Color color = JColorChooser.showDialog(this.panel, "Fill Color", this.getForeground());
		this.panel.setFillColor(color, "Panel");
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EColorMenuItem eColorMenuItem = EColorMenuItem.valueOf(e.getActionCommand());
			switch (eColorMenuItem) {
			case eFillColorShapeItem:
				System.out.println("필쉐입");
				fillColorShape();
				break;
			case eFillColorLineItem:
				System.out.println("필라인");
				fillColorLine();
				break;
			case eFillColorPanelItem:
				System.out.println("필패널");
				fillColorPanel();
				break;
			default:
				break;
			}
		}
	}
}
