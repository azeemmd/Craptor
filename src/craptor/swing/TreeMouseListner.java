package craptor.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.tree.DefaultMutableTreeNode;

import craptor.core.EnvConnection;

public class TreeMouseListner implements MouseListener {
    private MainFrame mainFrame;

    public TreeMouseListner(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void mouseClicked(MouseEvent e) {
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)mainFrame.getConnectionTree().getLastSelectedPathComponent();
        if (e.getButton() == MouseEvent.BUTTON3) {            
            if(node == null) {
                return;
            }
            Object userObject = node.getUserObject();
            //if clicked on connection
            if(userObject instanceof  EnvConnection){
                mainFrame.getTreePopupMenu().show(e.getComponent(), e.getX(), e.getY());
            }
            // if clicked on root
            else if("Connections".equals(userObject.toString())) {
                mainFrame.getTreePopupMenu1().show(e.getComponent(), e.getX(), e.getY());
            }
        }
        else if (e.getClickCount() >= 2 && e.getButton() == MouseEvent.BUTTON1) {
            Object userObject = node.getUserObject();
            if(userObject instanceof  EnvConnection){
                mainFrame.treeNodeClicked();
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        mainFrame.treePopupMenuClicked(e.getComponent().getName());        
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
