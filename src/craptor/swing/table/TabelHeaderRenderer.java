/*
 * Created on Jun 4, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package craptor.swing.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

/**
 * @author azeem
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
public class TabelHeaderRenderer extends JLabel implements TableCellRenderer {


	TabelHeaderRenderer()
	{		
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		setText(arg1.toString());
		// Set all sorts of interesting alignment options
		setVerticalAlignment( SwingConstants.CENTER );
		setHorizontalAlignment( SwingConstants.CENTER );
		setHorizontalTextPosition( SwingConstants.CENTER );
		setVerticalTextPosition( SwingConstants.BOTTOM );

                setBorder(new CompoundBorder(new EmptyBorder(40,0,0,0), new LineBorder(Color.black)));
		return this;
	}

}
