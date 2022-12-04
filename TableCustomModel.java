package net.codejava;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TableCustomModel extends AbstractTableModel {
	private String[] columnNames;
	private Object[][] data;

	public TableCustomModel(String[] tableTitle, Object[][] tableData) {
		columnNames = tableTitle;
	    data = tableData;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public String getValueAt(int rowIndex, int columnIndex) {
    	Object[] row = data[rowIndex];
    	return (String) row[columnIndex];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    public boolean isCellEditable(int row, int col) {
    	return true;
    }
    
    public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
    	Object[] row = data[rowIndex];
    	row[columnIndex] = newValue;
    }
};
