package craptor.swing.table.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class QueryTableModel extends AbstractTableModel implements IPageTableModel{
    
    private ResultSet rs;
    private int columnCount;
    private String[] columnNames;
    private int pageSize;
    private int pageOffSet;
    private ArrayList data;
    
    private int fetchedRowCount;
    private boolean fetchedAllRows = false;
    
    public QueryTableModel(ResultSet rs, int pageSize) throws SQLException {
        this.rs = rs;
        this.pageSize = pageSize;
        ResultSetMetaData metaData = rs.getMetaData();
        columnCount = metaData.getColumnCount();
        columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; ++i) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }
        
        //data = new ArrayList();
        //Fetch atleast pageSize records to display
        long before =  System.currentTimeMillis();
        data = fetchRows(pageSize);
        long after =  System.currentTimeMillis();
        //System.out.println("time taken :" + (after - before));
    }
    
    private ArrayList fetchRows(int n) throws SQLException{
        ArrayList rows = new ArrayList();
        int i;
        for(i = 0 ; i < n && rs.next(); ++i) {
            Object[] row = new Object[columnCount];
            for (int j = 1; j <= columnCount; ++j) {
                row[j-1] = rs.getString(j);
            }
            rows.add(row);
            fetchedRowCount++;
        }
        if(i < n) {
            fetchedAllRows = true;
        }
        return rows;
    }

    public int getPageCount() {
        return (int)Math.ceil((double)fetchedRowCount / pageSize);
    }

    public int getOriginalRowCount() {
        return fetchedRowCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return pageOffSet;
    }

    public void setPageSize(int size) throws SQLException {
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
        else if(!fetchedAllRows)
        {       data.addAll(fetchRows(pageSize - oldPageSize));
                fireTableRowsInserted(oldPageSize, pageSize - 1);
        }
    }

    public void nextPage() throws SQLException {
            if(pageOffSet < getPageCount() - 1)
            {
                    pageOffSet++;
                    fireTableDataChanged();
            }
            else if(!fetchedAllRows) {
                data.addAll(fetchRows(pageSize));
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


    public int getRowCount() {
        
        if(data.isEmpty()) {
            //If no records
            return 0;
        }
        else if(pageOffSet == getPageCount() - 1) {
            // If last page
            return getOriginalRowCount() - ((pageOffSet) * pageSize);                
        }
        else{
            return pageSize;
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        int originalRow = ( pageSize * pageOffSet) + rowIndex; 
        return ((Object[])data.get(originalRow))[columnIndex];
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        int originalRow = ( pageSize * pageOffSet) + rowIndex; 
       ((Object[])data.get(originalRow))[columnIndex] = aValue;
    }
}
