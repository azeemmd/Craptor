package craptor.swing;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class JTabbedPaneWithCloseIcons extends JTabbedPane implements MouseListener {

    public JTabbedPaneWithCloseIcons() {
        super();
        addMouseListener(this);
    }

    public void addTab(String title, Component component) {
        this.addTab(title, component, null);
    }

    public void addTab(String title, Component component, Icon extraIcon) {
        super.addTab(title, new CloseTabIcon(extraIcon), component);
    }

    public void mouseClicked(MouseEvent e) {
        int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());
        if (tabNumber < 0)
            return;
        Rectangle rect = ((CloseTabIcon)getIconAt(tabNumber)).getBounds();
        if (rect.contains(e.getX(), e.getY())) {
            //the tab is being closed            
            //Close the connection
            ((QueryPanel)getComponentAt(tabNumber)).closeConnection();
            this.removeTabAt(tabNumber);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}

