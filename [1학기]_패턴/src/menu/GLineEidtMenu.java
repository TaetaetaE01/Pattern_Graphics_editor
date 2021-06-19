package menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import frame.GPanel;
import main.GConstants.EColorMenuItem;
import main.GConstants.ELineEditMenuItem;

public class GLineEidtMenu extends JMenu {

	private GPanel panel;
	private int lineWidth;

	public GLineEidtMenu(String text) {
		super(text);

		ActionHandler actionHandler = new ActionHandler();

		for (ELineEditMenuItem eLineEditMenuItem : ELineEditMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eLineEditMenuItem.getText());
			menuItem.setActionCommand(eLineEditMenuItem.name());
			menuItem.addActionListener(actionHandler);
			this.add(menuItem);
		}
	}

	public void setAssociation(GPanel panel) {
		this.panel = panel;
	}

	public void setLineWidth() {
		JOptionPane a = new JOptionPane();
		@SuppressWarnings("static-access")
		String value = a.showInputDialog(this, "선 두께를 입력해주세요!");

		if (value != null) {
			int temp = Integer.parseInt(value);
			this.lineWidth = temp;
		}
		panel.setLineWidth(lineWidth);
	}
	
	public void setLineShape() {
		
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ELineEditMenuItem eLineEditMenuItem = ELineEditMenuItem.valueOf(e.getActionCommand());
			switch (eLineEditMenuItem) {
			case eLineWidth:
				setLineWidth();
				break;
			case eFullLineShape:
				panel.setLineShape(false);
				break;
			case eDottedLineShape:
				panel.setLineShape(true);
				break;
			default:
				break;
			}
		}
	}
}
