package frame;

import javax.swing.JMenuBar;

import main.GConstants.EMenu;
import menu.GColorMenu;
import menu.GEditMenu;
import menu.GFileMenu;
import menu.GLineEidtMenu;

public class GMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private GFileMenu fileMenu;
	private GEditMenu editMenu;
	private GColorMenu colorMenu;
	private GLineEidtMenu lineEidtMenu;

	public GMenuBar() {

		for (EMenu eMenu : EMenu.values()) {
			if (eMenu.getText().equals("파일")) {
				this.fileMenu = new GFileMenu(eMenu.getText());
				this.add(this.fileMenu);
			} else if (eMenu.getText().equals("편집")) {
				this.editMenu = new GEditMenu(eMenu.getText());
				this.add(this.editMenu);
			} else if (eMenu.getText().equals("색깔")) {
				this.colorMenu = new GColorMenu(eMenu.getText());
				this.add(this.colorMenu);
			} else if (eMenu.getText().equals("선 관리")) {
				this.lineEidtMenu = new GLineEidtMenu(eMenu.getText());
				this.add(this.lineEidtMenu);
			}
		}
	}

	public void setAssociation(GPanel panel) {
		this.fileMenu.setAssociation(panel);
		this.editMenu.setAssociation(panel);
		this.colorMenu.setAssociation(panel);
		this.lineEidtMenu.setAssociation(panel);
	}

	public GFileMenu getFileMenu() {
		return this.fileMenu;
	}
}
