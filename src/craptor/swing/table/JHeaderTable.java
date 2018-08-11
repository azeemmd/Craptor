/*
 * Created on Jun 4, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package craptor.swing.table;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import craptor.swing.table.model.IPageTableModel;

/**
 * @author azeem
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
public class JHeaderTable extends JTable{

	private TableHeaderPanel headerPanel;
        private boolean headerAdded = false;
	
	/**
	 * 
	 */
	public JHeaderTable() {
                super();
		JTableHeader tableHeader = this.getTableHeader();
		tableHeader.setLayout(new BorderLayout());
		tableHeader.setDefaultRenderer(new TabelHeaderRenderer());	
	}
        
        public void setModel(TableModel model) {
            super.setModel(model);
            if(model instanceof IPageTableModel && (!headerAdded)) {
                headerPanel = new TableHeaderPanel(this);
                tableHeader.add(headerPanel, BorderLayout.NORTH);
                headerAdded = true;
            }
        }
	
	public void handleNavigation(int navigation)
	{
            try{
		if(navigation == TableHeaderPanel.NAVIGATION_NEXT)
		{
			((IPageTableModel)getModel()).nextPage();
		}
		else if(navigation == TableHeaderPanel.NAVIGATION_PREV)
		{
			((IPageTableModel)getModel()).prevPage();
		}
		else if(navigation == TableHeaderPanel.NAVIGATION_FIRST)
		{
			((IPageTableModel)getModel()).firstPage();
		}
		else if(navigation == TableHeaderPanel.NAVIGATION_LAST)
		{
			((IPageTableModel)getModel()).lastPage();
		}
            }
            catch(Exception e) {
                e.printStackTrace();
            }
	}
	
	public void setPageSize(int pageSize)
	{
            try{
		((IPageTableModel)getModel()).setPageSize(pageSize);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
	}
	
	public int getPageSize()
	{
            return ((IPageTableModel)getModel()).getPageSize();

	}
	
	public void goToPage(int page)
	{
		((IPageTableModel)getModel()).goToPage(page);
	}
	
	public int getPageCount()
	{
            return ((IPageTableModel)getModel()).getPageCount();
	}
	
	public int getCurrentPage()
	{
		return ((IPageTableModel)getModel()).getCurrentPage();
	}
}
