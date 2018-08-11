/*
 * Created on Jun 4, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package craptor.swing.table;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;


/**
 * @author azeem
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
public class TableHeaderPanel extends JPanel implements ActionListener{

	public static int NAVIGATION_FIRST = 1;
	public static int NAVIGATION_PREV = 2;
	public static int NAVIGATION_NEXT = 3;
	public static int NAVIGATION_LAST = 4;

	private JToolBar navigationToolBar;
	private JHeaderTable table;
	
	private JButton first;
	private JButton prev;
	private JButton next;
	private JButton last;
	private JTextField pageSizeTextField;
	private JComboBox goToPageComboBox;
	private JLabel status;
	
	/**
	 * 
	 */
	public TableHeaderPanel(final JHeaderTable table) {
		super();
		this.table = table;
                this.setLayout(new FlowLayout(FlowLayout.RIGHT));
                this.setOpaque(false);
			
		navigationToolBar = new JToolBar();
                //navigationToolBar.setFloatable(false);
		first = new JButton("<<");
		prev = new JButton("<");
		next = new JButton(">");
		last = new JButton(">>");		
		
		pageSizeTextField = new JTextField("" + table.getPageSize());
		pageSizeTextField.setColumns(3);
		goToPageComboBox = new JComboBox();
		status = new JLabel();
		
		refreshPanel();
		
		navigationToolBar.add(new JLabel("PageSize:"));
		navigationToolBar.add(pageSizeTextField);
		navigationToolBar.addSeparator();
		navigationToolBar.add(new JLabel(" Go to page:"));
		navigationToolBar.add(goToPageComboBox);
		navigationToolBar.add(status);
		navigationToolBar.addSeparator();
		navigationToolBar.add(first);
		navigationToolBar.add(prev);
		navigationToolBar.add(next);
		navigationToolBar.add(last);
		
		this.add(navigationToolBar);
		
		first.addActionListener(this);
		prev.addActionListener(this);
		next.addActionListener(this);
		last.addActionListener(this);
		
		pageSizeTextField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				int pageSize = Integer.parseInt(pageSizeTextField.getText());
				table.setPageSize(pageSize);
				refreshPanel();				
			}
		});
		
		goToPageComboBox.addActionListener(this);
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent){
           
		if(actionEvent.getSource().equals(first))
		{
			table.handleNavigation(NAVIGATION_FIRST);
		}
		else if(actionEvent.getSource().equals(prev))
		{
			table.handleNavigation(NAVIGATION_PREV);
		}
		else if(actionEvent.getSource().equals(next))
		{
			table.handleNavigation(NAVIGATION_NEXT);
		}
		else if(actionEvent.getSource().equals(last))
		{
			table.handleNavigation(NAVIGATION_LAST);
		}
		else if(actionEvent.getSource().equals(goToPageComboBox))
		{
			int page = goToPageComboBox.getSelectedIndex();
			table.goToPage(page);
		}
                if(goToPageComboBox.getModel().getSize() ==  table.getCurrentPage()) {
                    DefaultComboBoxModel model = (DefaultComboBoxModel) goToPageComboBox.getModel();
                    model.addElement("" + (table.getCurrentPage()+1));
                }
		goToPageComboBox.setSelectedIndex(table.getCurrentPage());
                status.setText("of " + table.getPageCount());
	}
	
	private DefaultComboBoxModel getDefaultComboBoxModel(int size)
	{
		DefaultComboBoxModel model = (DefaultComboBoxModel) goToPageComboBox.getModel();
		model.removeAllElements();
		for(int i = 1; i <= size; ++i)
		{
			model.addElement("" + i);	
		}
		
		return model;
	}
	
	public void refreshPanel() {
		goToPageComboBox.setModel(getDefaultComboBoxModel(table.getPageCount()));		
		status.setText("of " + table.getPageCount());				
	}
}
