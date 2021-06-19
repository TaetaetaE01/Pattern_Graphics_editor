package menu;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import frame.GPanel;
import main.GConstants.EEditMenuItem;
import main.GConstants.EFileMenuItem;

public class GEditMenu extends JMenu {

	// component

	// association
	private GPanel panel;

	public GEditMenu(String text) {
		super(text);

		ActionHandler actionHandler = new ActionHandler();

		for (EEditMenuItem eEditMenuItem : EEditMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eEditMenuItem.getText());
			menuItem.setActionCommand(eEditMenuItem.name());
			menuItem.setToolTipText(eEditMenuItem.getShortcut());
			menuItem.addActionListener(actionHandler);
			this.add(menuItem);

			switch (eEditMenuItem.getText()) {
			case "Undo":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
				break;

			case "Redo":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
				break;

			case "Cut":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
				break;

			case "Delete":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK));
				break;

			case "Copy":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
				break;

			case "Paste":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
				break;

			case "Group":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
				break;

			case "UnGroup":
				menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
				break;
			}

			if (eEditMenuItem.getText().equals("Redo") || eEditMenuItem.getText().equals("Paste")) {
				this.addSeparator();
			}
		}

	}

	public void setAssociation(GPanel panel) {
		this.panel = panel;
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EEditMenuItem eEditMenuItem = EEditMenuItem.valueOf(e.getActionCommand());
			switch (eEditMenuItem) {
			case eUndo:
				panel.unDo();
				break;
			case eRedo:
				panel.reDo();
				break;
			case eCut:
				panel.cut();
				break;
			case eDelete:
				panel.delete();
				break;
			case eCopy:
				panel.copy();
				break;
			case ePaste:
				panel.paste();
				break;
			case eGroup:
				
				break;
			case eUngroupe:
				
				break;
			default:
				break;
			}
		}
	}
}
