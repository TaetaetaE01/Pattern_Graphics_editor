package frame;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import main.GConstants.EShapeTool;

public class GToolBar extends JToolBar {
	// attributes
	private static final long serialVersionUID = 1L;

	// associations
	private GPanel panel;

	public GToolBar() {
		// initialize components
		ActionHandler actionHandler = new ActionHandler();
		ButtonGroup group = new ButtonGroup();

		for (EShapeTool eButton : EShapeTool.values()) {
			JRadioButton button = new JRadioButton();
			
			ImageIcon icon = new ImageIcon(eButton.getIcon());
			Image im = icon.getImage();
			Image im2 = im.getScaledInstance(30, 25, Image.SCALE_DEFAULT);
			ImageIcon icon2 = new ImageIcon(im2);

			ImageIcon icon3 = new ImageIcon(eButton.getSelectedIcon());
			Image im3 = icon3.getImage();
			Image im4 = im3.getScaledInstance(30, 25, Image.SCALE_DEFAULT);
			ImageIcon icon4 = new ImageIcon(im4);

			button.setActionCommand(eButton.toString());
			button.setIcon(icon2);
			button.setSelectedIcon(icon4);
			group.add(button);
			this.add(button);
			button.addActionListener(actionHandler);

			this.add(button);
		}
	}

	public void initialize() {
		((JRadioButton) (this.getComponent(EShapeTool.eRectangle.ordinal()))).doClick();
	}

	public void setAssociation(GPanel panel) {
		this.panel = panel;
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			EShapeTool eShapeTool = EShapeTool.valueOf(event.getActionCommand());
			panel.setSelection(eShapeTool.getShapeTool());
		}
	}

}
