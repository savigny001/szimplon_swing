package desktop;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.szimplon.editArea.EditAreaPanel;
import com.szimplon.model.MainModel;
import com.szimplon.model.lmlfile.LmlFile;

import src.EditAreaStart;

public class DesktopController {
	private DesktopModel model;
	private DesktopView view;
	public ActionListener undoBtnListener, redoBtnListener, closeBtnListener,
		saveBtnListener;
	public KeyListener desktopKeyListener;
	
	public DesktopController (DesktopModel model, DesktopView view) {
		this.model = model;
		this.view = view;
			
		undoBtnListener = new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		
		redoBtnListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		};
		
		closeBtnListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeActualTab();
			}
		};
		
		saveBtnListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveActualTab();			
			}
		};
		
		desktopKeyListener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.isControlDown() && (arg0.getKeyCode() == KeyEvent.VK_S)) saveActualTab();
				if (arg0.isControlDown() && (arg0.getKeyCode() == KeyEvent.VK_Z)) undo();
				if (arg0.isControlDown() && (arg0.getKeyCode() == KeyEvent.VK_Y)) redo();
			}
		};
	}
	
	public void closeActualTab() {
		Component tabbedPaneSelectedComponent = EditAreaStart.frame.editAreaTabbedPn.getSelectedComponent();
		EditAreaPanel tabbedPaneSelectedPn = (EditAreaPanel)tabbedPaneSelectedComponent;
		LmlFile tabbedPaneSelectedFile = tabbedPaneSelectedPn.getFile();
		EditAreaStart.frame.editAreaTabbedPn.remove(tabbedPaneSelectedComponent);
		EditAreaStart.model.getOpenedFiles().remove(tabbedPaneSelectedFile);
	}
	
	public void saveActualTab() {
		Component tabbedPaneSelectedComponent = EditAreaStart.frame.editAreaTabbedPn.getSelectedComponent();
		EditAreaPanel tabbedPaneSelectedPn = (EditAreaPanel)tabbedPaneSelectedComponent;
		String tabbedPaneSelectedPnText = tabbedPaneSelectedPn.editArea.getText();
		LmlFile tabbedPaneSelectedFile = tabbedPaneSelectedPn.getFile();
		MainModel.writeTextFile(tabbedPaneSelectedFile.filePath, tabbedPaneSelectedPnText);
		System.out.println("save");
	}
	
	public void undo() {
		try {
			EditAreaStart.frame.manager.undo();
		} catch (CannotUndoException e1) {
			System.out.println("nem lehet visszavonni");
		}
	}
	
	public void redo() {
		try {
			EditAreaStart.frame.manager.redo();
		} catch (CannotRedoException e1) {
			System.out.println("nem lehet ismét");
		}
	}
}