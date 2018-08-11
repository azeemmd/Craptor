package craptor.swing.table.model;


import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class WrapperPageTableModel extends AbstractTableModel implements IPageTableModel {
	
	private int pageSize;
	private int pageOffSet;
	private TableModel model;
	
        public WrapperPageTableModel(TableModel model, int pageSize) {
            this.pageSize = pageSize;
            this.model = model;
        }

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		int originalRow = ( pageSize * pageOffSet) + row; 
		return model.getValueAt(originalRow, col);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return model.getColumnCount();
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
            
                return model.getColumnName(arg0);
        }
	
	public int getPageCount()
	{
		return (int)Math.ceil((double)model.getRowCount() / pageSize);
	}
	
	public int getOriginalRowCount()
	{
		return model.getRowCount();
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

