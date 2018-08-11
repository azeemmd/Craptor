package craptor.swing.table.model;

import java.sql.SQLException;

import javax.swing.table.TableModel;

public interface IPageTableModel extends TableModel {
    
    public int getPageCount();

    public int getOriginalRowCount();

    public int getPageSize();

    public int getCurrentPage();

    public void setPageSize(int size)  throws SQLException;

    public void nextPage() throws SQLException;

    public void prevPage();

    public void firstPage();

    public void lastPage();

    public void goToPage(int page);
}
