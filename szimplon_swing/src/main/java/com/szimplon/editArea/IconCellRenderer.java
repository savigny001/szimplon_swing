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
import com.szimplon.model.concept.Tag;
import com.szimplon.model.concept.ConceptBehaviourType;

public class IconCellRenderer extends JLabel implements TreeCellRenderer {
	
	  public static ImageIcon ICON_FIELD = new ImageIcon("src/img/field-icon_5.png");
	  public static ImageIcon ICON_TAG = new ImageIcon("src/img/tag-icon_3.png");
	  public static ImageIcon ICON_YELLOW = new ImageIcon("src/img/yellow-point-icon.png");
	  public static ImageIcon ICON_EXCLAMATION = new ImageIcon("src/img/exclamation-icon.png");
	  public static ImageIcon ICON_EARTH = new ImageIcon("src/img/earth-icon-16.png");
	  public static ImageIcon ICON_LIST = new ImageIcon("src/img/list-icon-16.png");
	  public static ImageIcon ICON_GREEN = new ImageIcon("src/img/green-tag2-icon-16.png");
	  
	  protected Color m_textSelectionColor;
	  protected Color m_textNonSelectionColor;
	  protected Color m_bkSelectionColor;
	  protected Color m_bkNonSelectionColor;
	  protected Color m_borderSelectionColor;
	  protected boolean m_selected;
	
	  public IconCellRenderer() {
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
			    
	   if (obj instanceof Field) setIcon(ICON_FIELD); 
	   		else if (obj instanceof Tag) setIcon(ICON_TAG); else {
		
		   if (Concept.class.isInstance(obj)) {
			   
			   Concept concept = (Concept)obj;
			   if (concept.getBehaviourType() == ConceptBehaviourType.PROPERTY_CONCEPT_MARKER) setIcon(ICON_LIST);
			   if (concept.getBehaviourType() == ConceptBehaviourType.PROPERTY_CONCEPT) setIcon(ICON_GREEN);
			   if (concept.getBehaviourType() == ConceptBehaviourType.REQUIRED_CONCEPT) {
				   
				   if (concept.isAlternative()) setIcon(ICON_YELLOW); else setIcon(ICON_EXCLAMATION);
			   }
//			   if (concept.getBehaviourType() == ConceptBehaviourType.EFFECT_CONCEPT) setIcon(ICON_EARTH);			   
			   if (concept.getBehaviourType() == ConceptBehaviourType.EFFECT_CONCEPT_MARKER) setIcon(ICON_EARTH);
			   
		   } else setIcon(null);
		   
	   } 
	   

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
