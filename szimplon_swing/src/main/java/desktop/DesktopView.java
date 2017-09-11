package desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class DesktopView extends JFrame implements Observer {
	private DesktopController controller;
	private DesktopModel model;
	private JDesktopPane desktop;
	private JTextField searchFd;
	public JButton undoBtn, redoBtn;
	public JMenuItem saveProjectMi;
	public JButton closeBtn, saveBtn;
	
	public DesktopView (DesktopModel model) {
		this.model = model;
		controller = null;
		setWindowsLookAndFeel();
		setTitle("sZimPLon Swing Beta");
		setLayout(new BorderLayout());
		makeDesktop();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void setWindowsLookAndFeel() {
		// Windowsos kin�zet be�ll�t�sa
		try {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void makeDesktop() {
		setBounds(10, 10, 1200, 600);
		desktop = new JDesktopPane();
		desktop.setDesktopManager(new DefaultDesktopManager());
		desktop.setBackground(new Color(130,160,210));
		this.add(desktop);
		makeMenuBar();
		makeToolBar();
	}
	
	public void makeMenuBar () {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu projectMenu = new JMenu("Projekt");

		JMenuItem exitMi = new JMenuItem("Kilépés");
		JMenuItem newProjectMi = new JMenuItem("Új");
		JMenuItem openProjectMi = new JMenuItem("Megnyitás");
		saveProjectMi = new JMenuItem("Mentés");
		JMenuItem saveAsProjectMi = new JMenuItem("Mentés másként");
		JMenuItem closeProjectMi = new JMenuItem("Bezár");
				
		fileMenu.add(exitMi);
		projectMenu.add(newProjectMi);
		projectMenu.add(openProjectMi);
		projectMenu.add(saveProjectMi);
		projectMenu.add(saveAsProjectMi);
		projectMenu.add(closeProjectMi);
		
		menuBar.add(fileMenu);
		menuBar.add(projectMenu);
		
		setJMenuBar(menuBar);
	}
	
	public void makeToolBar () {
		JToolBar toolBar = new JToolBar();
		JLabel searchLb = new JLabel("Keresés: ");
		searchFd = new JTextField();		
		
		JButton newProjectBtn = new JButton(new ImageIcon("src/img/new-project-icon-24.png"));
		newProjectBtn.setToolTipText("Új projekt létrehozása");
		
		JButton newFileBtn = new JButton(new ImageIcon("src/img/new-document-icon.png"));
		newFileBtn.setToolTipText("Új LML fájl létrehozása");
		
		saveBtn = new JButton(new ImageIcon("src/img/save-icon-24.png"));
		saveBtn.setToolTipText("Mentés");
		
		JButton makeOdtBtn = new JButton(new ImageIcon("src/img/make-odt-icon-24.png"));
		makeOdtBtn.setToolTipText("Formázott ODT dokumentum elkészítése");
		
		closeBtn = new JButton(new ImageIcon("src/img/close-icon-24.png"));
		closeBtn.setToolTipText("Bezárás");
		
		undoBtn = new JButton(new ImageIcon("src/img/undo-icon-24.png"));
		undoBtn.setToolTipText("Visszavonás");
		
		redoBtn = new JButton(new ImageIcon("src/img/redo-icon-24.png"));
		redoBtn.setToolTipText("Ismét");
		
		toolBar.add(newProjectBtn);
		toolBar.add(newFileBtn);
		toolBar.add(saveBtn);
		toolBar.add(makeOdtBtn);
		toolBar.add(closeBtn);
		toolBar.add(undoBtn);
		toolBar.add(redoBtn);
				
		toolBar.add(searchLb);
		toolBar.add(searchFd);
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	public DesktopController getController() {
		return controller;
	}

	public void setController(DesktopController control) {
		this.controller = control;
		undoBtn.addActionListener(controller.undoBtnListener);		
		redoBtn.addActionListener(controller.redoBtnListener);
		closeBtn.addActionListener(controller.closeBtnListener);
		saveBtn.addActionListener(controller.saveBtnListener);
		desktop.addKeyListener(controller.desktopKeyListener);
	}

	public JDesktopPane getDesktop() {
		return desktop;
	}

	public void setDesktop(JDesktopPane desktop) {
		this.desktop = desktop;
	}

	public JButton getUndoBtn() {
		return undoBtn;
	}

	public void setUndoBtn(JButton undoBtn) {
		this.undoBtn = undoBtn;
	}

	public JButton getRedoBtn() {
		return redoBtn;
	}

	public void setRedoBtn(JButton redoBtn) {
		this.redoBtn = redoBtn;
	}
}