package frame;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import main.GConstants.CFrame;

public class GFrame extends JFrame {
	// attributes
	private static final long serialVersionUID = 1L;

	// components
	private GPanel panel;
	private GToolBar toolBar;
	private GMenuBar menuBar;
	private WinEvent winEvent;

	public GFrame() {
		// initialize attributes
		this.setLocation(CFrame.point);
		this.setSize(CFrame.dimesion);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setTitle("태현이의 그림판");

		// initialize components
		this.winEvent = new WinEvent();
		this.addWindowListener(winEvent);

		this.menuBar = new GMenuBar();
		this.setJMenuBar(this.menuBar);

		BorderLayout layoutManager = new BorderLayout();
		this.getContentPane().setLayout(layoutManager);

		this.toolBar = new GToolBar();
		this.getContentPane().add(this.toolBar, BorderLayout.NORTH);

		this.panel = new GPanel();
		this.getContentPane().add(this.panel, BorderLayout.CENTER);

		// set associations
		this.menuBar.setAssociation(this.panel);
		this.toolBar.setAssociation(this.panel);
	}

	public void initialize() {
		this.toolBar.initialize();
		this.panel.initialize();
	}

	private class WinEvent implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {

		}

		@Override
		public void windowClosing(WindowEvent e) {
			menuBar.getFileMenu().exitProgram();
		}

		@Override
		public void windowClosed(WindowEvent e) {

		}

		@Override
		public void windowIconified(WindowEvent e) {

		}

		@Override
		public void windowDeiconified(WindowEvent e) {

		}

		@Override
		public void windowActivated(WindowEvent e) {

		}

		@Override
		public void windowDeactivated(WindowEvent e) {

		}

	}
}
