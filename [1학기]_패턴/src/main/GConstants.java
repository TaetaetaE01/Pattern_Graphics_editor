package main;

import java.awt.Dimension;
import java.awt.Point;

import shapeTools.GOval;
import shapeTools.GRectangle;
import shapeTools.GLine;
import shapeTools.GPolygon;
import shapeTools.GShapeTool;
import shapeTools.GRoundRectangle;
import shapeTools.GArc;
import shapeTools.GPen;

public class GConstants {
	public static class CFrame {
		public final static Point point = new Point(350, 150);
		public final static Dimension dimesion = new Dimension(1200, 800);
	}
	
	public enum EDrawingStyle {
		e2PointDrawing,
		eNPointDrawing
	};
	
	public final static int wAnchor = 10;	
	public final static int hAnchor = 10;
	
	public enum EAction {
		eDraw,
		eMove,
		eResize,
		eRotate,
		eShear
	}
	
	public enum EShapeTool {
		
		eRectangle(new GRectangle(), "Rectangle" , "image/사각형.png", "image/선택사각형.png"), 
		eOval(new GOval(), "Oval", "image/원.png", "image/선택원.png"),
		eLine(new GLine(), "Line" , "image/선.png", "image/선택선.png"),
		ePolygon(new GPolygon(), "Polygon" , "image/다각형.png", "image/선택다각형.png"),
		eRoundRectangle(new GRoundRectangle(), "RoundRectangle" , "image/둥근사각형.png", "image/선택둥근사각형.png"),
		eArc(new GArc(), "Arc" , "image/호.png", "image/선택호.png"),
		ePen(new GPen(), "Pen" , "image/펜.png", "image/선택펜.png");
		
		private GShapeTool shapeTool;
		private String text;
		private String icon;
		private String selectedIcon;

		private EShapeTool(GShapeTool shapeTool, String text, String icon, String selectedIcon) {
			this.shapeTool = shapeTool;
			this.text = text;
			this.icon = icon;
			this.selectedIcon = selectedIcon;
		}
		public GShapeTool getShapeTool() {
			return this.shapeTool;
		}
		public String getText() {
			return this.text;
		}
		public String getIcon() {
			return this.icon;
		}
		public String getSelectedIcon() {
			return this.selectedIcon;
		}
	}
	
	public enum EMenu {
		eFile("파일"),
		eEdit("편집"),
		eColor("색깔"),
		eLineEdit("선 관리"),
		eHelp("도움말");		
		private String text;
		private EMenu(String text) {
			this.text = text;
		}
		public String getText() {
			return this.text;
		}
	}
	
	public enum EFileMenuItem {
		eNew("새로만들기"),
		eOpen("열기"),
		eSave("저장"),
		eSaveAs("다른이름으로"),
		ePrint("프린트"),
		eScreanShot("스크린샷"),
		eExit("나가기");
		
		private String text;
		private EFileMenuItem(String text) {
			this.text = text;
		}
		public String getText() {
			return this.text;
		}
	}
	
	public enum EEditMenuItem {
		eUndo("Undo", "ctrl + z", "Z" , "Event.CTRL_MASK"),
		eRedo("Redo", "ctrl + y" , "Y", "Event.CTRL_MASK"),
		eCut("Cut", "ctrl + x", "X", "Event.CTRL_MASK"),
		eDelete("Delete", "ctrl + delete", "DELETE", "Event.CTRL_MASK"),
		eCopy("Copy", "ctrl + c", "C", "Event.CTRL_MASK"),
		ePaste("Paste", "ctrl + v", "V", "Event.CTRL_MASK"),
		eGroup("Group","ctrl + g", "G", "Event.CTRL_MASK"),
		eUngroupe("UnGroup", "ctrl + u", "U", "Event.CTRL_MASK");
		
		
		private String text;
		private String shortcut;
		private String Keyboard;
		private String control;
		
		private EEditMenuItem(String text, String shortcut, String Keyboard, String control) {
			this.text = text;
			this.shortcut = shortcut;
			this.Keyboard = Keyboard;
			this.control = control;
		}
		public String getText() {
			return this.text;
		}
		
		public String getShortcut() {
			return this.shortcut;
		}
		
		public String getKeboardKey() {
			return this.Keyboard;
		}
		public String getcontrol() {
			return this.control;
		}
	}
	
	public enum EColorMenuItem {
		eFillColorShapeItem("색 채우기", "ctrl + f", "F" , "Event.CTRL_MASK"),
		eFillColorLineItem("선 색 바꾸기", "ctrl + l", "L" , "Event.CTRL_MASK"),
		eFillColorPanelItem("배경색 바꾸기", "ctrl + p", "P" , "Event.CTRL_MASK");
		
		private String text;
		private String shortcut;
		private String Keyboard;
		private String control;
		
		private EColorMenuItem(String text, String shortcut, String Keyboard, String control) {
			this.text = text;
			this.shortcut = shortcut;
		}
		public String getText() {
			return this.text;
		}
		public String getShortcut() {
			return this.shortcut;
		}
		public String getKeboardKey() {
			return this.Keyboard;
		}
		public String getcontrol() {
			return this.control;
		}
	}
	
	public enum ELineEditMenuItem {
		eLineWidth("선 굵기"),
		eFullLineShape("실선"),
		eDottedLineShape("점선");
	
		private String text;
		private ELineEditMenuItem(String text) {
			this.text = text;
		}
		public String getText() {
			return this.text;
		}
	}
	
}
