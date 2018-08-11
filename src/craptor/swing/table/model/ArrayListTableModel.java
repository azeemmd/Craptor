package craptor.swing.table.model;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ArrayListTableModel implements TableModel{
    
    private ArrayList data;
    private Object[] columnNames;
    
    public ArrayListTableModel(ArrayList data, Object[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex].toString();
    }

    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Object[])data.get(rowIndex))[columnIndex];
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ((Object[])data.get(rowIndex))[columnIndex] = aValue;
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}
