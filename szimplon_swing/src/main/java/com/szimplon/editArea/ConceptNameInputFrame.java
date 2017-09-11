package com.szimplon.editArea;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class ConceptNameInputFrame extends JFrame {

	JTextField textField;

	public ConceptNameInputFrame () {
		setBounds(100, 100, 200, 70);
		textField = new JTextField(30);
		add(textField);
		setVisible(true);
		
		
		textField.addKeyListener(new KeyListener() {
	
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					dispose();
					conceptNameCacher(textField.getText());
				}
			}
		});
	}
	
	public void conceptNameCacher(String text) {
	}
}
