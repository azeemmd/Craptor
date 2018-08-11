/*
 * Created on Jun 4, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package craptor.swing.table.model;

import javax.swing.table.AbstractTableModel;

/**
 * @author azeem
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
public class PageTableModel extends AbstractTableModel implements IPageTableModel {
	
	private int pageSize;
	private int pageOffSet;
	private Object[][] data;
	
	public PageTableModel(Object[][] data,int pageSize)
	{
		this.pageSize = pageSize;
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		int originalRow = ( pageSize * pageOffSet) + row; 
                return data[originalRow][col];
        }
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return data[0].length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getRowCount()
	 */
	public int getRowCount() {
            
            // If last page
            if(pageOffSet == getPageCount() - 1) {
                return getOriginalRowCount() - ((pageOffSet) * pageSize);                
            }
            else{
		return pageSize;
            }
	}
        
        public String getColumnName(int arg0) {
            
                return "Column" + arg0;
        }
	
	public int getPageCount()
	{
		return (int)Math.ceil((double)data.length / pageSize);
	}
	
	public int getOriginalRowCount()
	{
		return data.length;
	}
	
	public int getPageSize()
	{
		return pageSize;
	}
	
	public int getCurrentPage()
	{
		return pageOffSet;
	}
	
	public void setPageSize(int size)
	{
		if(pageSize == size)
		{
			return;
		}
		
		int oldPageSize = pageSize;
		pageSize = size;
		if(pageSize < oldPageSize)
		{
			fireTableRowsInserted(pageSize, oldPageSize - 1);
		}
		else
		{
			fireTableRowsInserted(oldPageSize, pageSize - 1);
		}
	}
	
	public void nextPage()
	{
		if(pageOffSet < getPageCount() - 1)
		{
			pageOffSet++;
			fireTableDataChanged();
		}
	}
	
	public void prevPage()
	{
		if(pageOffSet > 0)
		{
			pageOffSet--;
			fireTableDataChanged();
		}
	}
	
	public void firstPage()
	{
		pageOffSet = 0;
		fireTableDataChanged();
	}
	
	public void lastPage()
	{
		pageOffSet = getPageCount() - 1;
		fireTableDataChanged();
	}

	/**
	 * @param page
	 */
	public void goToPage(int page) {
		if(pageOffSet == page)
		{
			return;
		}
		pageOffSet = page;
		fireTableDataChanged();		
	}

}
