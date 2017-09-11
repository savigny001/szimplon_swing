package com.szimplon.editArea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import com.szimplon.model.concept.Concept;
import com.szimplon.model.concept.Field;
import com.szimplon.model.lmlfile.LmlFile;
import com.szimplon.model.concept.ConceptBehaviourType;

public class WorkspaceTreeCellRenderer extends JLabel implements TreeCellRenderer {
	
	  public static ImageIcon ICON_FOLDER = new ImageIcon("src/img/folder-icon-16.png");
	  public static ImageIcon ICON_LMLFILE = new ImageIcon("src/img/lmlfile-icon-16.png");
	  public static ImageIcon ICON_FIELD = new ImageIcon("src/img/field-icon_3.png");
	  	  
	  protected Color m_textSelectionColor;
	  protected Color m_textNonSelectionColor;
	  protected Color m_bkSelectionColor;
	  protected Color m_bkNonSelectionColor;
	  protected Color m_borderSelectionColor;
	  protected boolean m_selected;
	
	  public WorkspaceTreeCellRenderer() {
	    super();
	    m_textSelectionColor = UIManager.getColor(
	      "Tree.selectionForeground");
	    m_textNonSelectionColor = UIManager.getColor(
	      "Tree.textForeground");
//	    m_bkSelectionColor = UIManager.getColor(
//	      "Tree.selectionBackground");
	    m_bkSelectionColor = new Color (80, 100, 130);
	    m_bkNonSelectionColor = UIManager.getColor(
	      "Tree.textBackground");
//	    m_bkNonSelectionColor = new Color(204,229,255);
	    m_borderSelectionColor = UIManager.getColor(
	      "Tree.selectionBorderColor");
	    
	  }
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		Object obj = node.getUserObject();
		setText(obj.toString());
				 
			    
	   if (LmlFile.class.isInstance(obj)) {
		   
		   LmlFile file = (LmlFile)obj;
		   
		   if (file.isDirectory) setIcon(ICON_FOLDER); else
		   if (file.fileName.endsWith(".lml")) setIcon(ICON_LMLFILE); else setIcon(null); 
		     
	   }  else setIcon(null); 
	   

	    setFont(tree.getFont());
	    setForeground(sel ? m_textSelectionColor : 
	      m_textNonSelectionColor);
	    setBackground(sel ? m_bkSelectionColor : 
	      m_bkNonSelectionColor);
	    m_selected = sel;
	    return this;
	}
	
	public void paint(Graphics g) {
		
	    Color bColor = getBackground();
	    Icon icon = getIcon();

	    g.setColor(bColor);
	    int offset = 0;
	    if(icon != null && getText() != null) 
	      offset = (icon.getIconWidth() + getIconTextGap());
	    g.fillRect(offset, 0, getWidth() - 1 - offset,
	      getHeight() - 1);
	    
	    if (m_selected) 
	    {
	      g.setColor(m_borderSelectionColor);
	      g.drawRect(offset, 0, getWidth()-1-offset, getHeight()-1);
	    }

	    super.paint(g);
	    }

}
