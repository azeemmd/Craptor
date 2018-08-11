/*
 * Created on Jun 4, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package craptor;

import craptor.swing.table.JHeaderTable;
import craptor.swing.table.model.PageTableModel;

import craptor.swing.table.model.RandomTableModel;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * @author azeem
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
public class TestTableHeader extends JFrame{
	
	JTable table;
	PageTableModel tableModel;
	
	TestTableHeader(String title)
	{
		super(title);
		tableModel = new PageTableModel((new RandomTableModel(15, 10)).getData(), 10);
		table = new JHeaderTable();
                table.setModel(tableModel);
	
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);
	}

	public static void main(String[] args) {
		JFrame frame = new TestTableHeader("TableExample");
		frame.setSize(800, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
