package craptor.swing;


import craptor.core.IEnvConnection;
import craptor.core.QueryHelper;

import craptor.swing.editor.SQLContext;
import craptor.swing.editor.SQLDocument;
import craptor.swing.editor.SQLEditorKit;
import craptor.swing.table.JHeaderTable;

import craptor.swing.table.model.PageTableModel;
import craptor.swing.table.model.QueryTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import java.sql.SQLException;

import java.util.StringTokenizer;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;


@SuppressWarnings("serial")
public class QueryPanel extends JPanel implements KeyListener {

    private QueryHelper queryHelper;   
    private MainFrame mainFrame;
    private TableModel tableModel;

    private JSplitPane jSplitPane1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JEditorPane queryText;
    private JTextArea console;
    private JTabbedPaneWithCloseIcons jTabbedPane;
    private JTable resultTable;
    private File openFile;

    public QueryPanel(IEnvConnection conn, MainFrame mainFrame) throws Exception {
        this.mainFrame = mainFrame;
        queryHelper = new QueryHelper(conn);
        jbInit();
        queryText.requestFocus();
        queryText.addKeyListener(this);
    }
    
    public void closeConnection() {
        try {
            queryHelper.close();
        } catch (SQLException e) {
             mainFrame.showDialog("ERROR", e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void openFile(File file) {
        mainFrame.setWaitCursor();
        this.openFile = file;
        try {
            FileInputStream fis = new FileInputStream(file);
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(fis));
            queryText.read(reader, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainFrame.setNormalCursor();
        mainFrame.setStatusMessage("Opened file " + file.getName());
    }
    
    public boolean saveFile() {  
        if(openFile == null) {
            return false;
        }
        else
        {
            mainFrame.setWaitCursor();
            saveFile(openFile);
            mainFrame.setNormalCursor();
            return true;
        }
        
    }
    
    public void saveFile(File file) {
        mainFrame.setWaitCursor();
        this.openFile = file;       
        try {
           SQLDocument doc = (SQLDocument) queryText.getDocument();
           doc.save(file);
        } catch (IOException e) {
           mainFrame.showDialog("ERROR", e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
        mainFrame.setNormalCursor();
        mainFrame.setStatusMessage("Saved file " + file.getName());
    }

    private void jbInit() throws Exception {
        this.setLayout(new BorderLayout());
        
        jSplitPane1 = new JSplitPane();
        jScrollPane1 = new JScrollPane();
        jTabbedPane = new JTabbedPaneWithCloseIcons();
        
        queryText = new JEditorPane();
        console = new JTextArea();
        console.setEditable(false);
        SQLEditorKit kit = new SQLEditorKit();
        queryText.setEditorKitForContentType("text/sql", kit);
        queryText.setContentType("text/sql");
        queryText.setFont(new Font("Courier", 0, 12));
        queryText.setEditable(true);
        queryText.grabFocus();

        SQLContext styles = kit.getStylePreferences();

        Style s;

        s = styles.getStyleForScanValue(SQLDocument.NORMAL);
        setStyles(s, "syntax.default.");
        s = styles.getStyleForScanValue(SQLDocument.COMMENT);
        setStyles(s, "syntax.comments.");
        s = styles.getStyleForScanValue(SQLDocument.KEYWORD);
        setStyles(s, "syntax.keywords.");
        s = styles.getStyleForScanValue(SQLDocument.LITERAL);
        setStyles(s, "syntax.literals.");       


        resultTable = new JHeaderTable();
         
        jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setDividerLocation(200);
        jScrollPane1.getViewport().add(queryText, null);
        jSplitPane1.add(jScrollPane1, JSplitPane.TOP);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane2 = new JScrollPane(resultTable);
        jTabbedPane.add("Results", jScrollPane2);
        jScrollPane3 = new JScrollPane(console);
        jTabbedPane.add("Console", jScrollPane3);
        jSplitPane1.add(jTabbedPane, JSplitPane.BOTTOM);
        this.add(jSplitPane1, BorderLayout.CENTER);
    }
    
    private void setStyles(Style s, String prefix) {
        
        String value;

        value = MainFrame.getString(prefix + "font.name");

        if (value != null && value.trim().length() != 0) {
            StyleConstants.setFontFamily(s, value);
        }

        value = MainFrame.getString(prefix + "font.size");

        if (value != null && value.trim().length() != 0) {
                
            try {
                int n = Integer.parseInt(value);
                StyleConstants.setFontSize(s, n);
            } catch (NumberFormatException nfe) {
            }
        }

        value = MainFrame.getString(prefix + "color");

        if (value != null && value.trim().length() != 0) {
                
            StringTokenizer tk = new StringTokenizer(value.trim(), ", ");

            try {
                
                int r = Integer.parseInt(tk.nextToken());
                int g = Integer.parseInt(tk.nextToken());
                int b = Integer.parseInt(tk.nextToken());

                StyleConstants.setForeground(s, new Color(r, g, b));
                
            } catch (Exception e) {
            }
        }

        value = MainFrame.getString(prefix + "italic");

        if (value != null && value.trim().length() != 0) {
            value = value.trim();

            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"))
                StyleConstants.setItalic(s, true);
            else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no"))
                StyleConstants.setItalic(s, false);
        }

        value = MainFrame.getString(prefix + "bold");

        if (value != null && value.trim().length() != 0) {
                
            value = value.trim();

            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"))
                StyleConstants.setBold(s, true);
            else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no"))
                StyleConstants.setBold(s, false);
        }
    }
    
    public void query() {
        mainFrame.setStatusMessage("Querying....");
        mainFrame.setWaitCursor();
        //First get selected text
        String query = queryText.getSelectedText();
        //If nothing selected, take the entire query
        if (query == null) {
            query = queryText.getText();
        }
        
        try {
            
            //--------------- FETCH PAGE WISE--------------------     
        	// if query is desc 
        	if(query.toUpperCase().startsWith("DESC"))
        	{
        		tableModel = new PageTableModel(queryHelper.descTable(query.split(" +")[1]), 30);
        		jTabbedPane.setSelectedComponent(jScrollPane2);
        	}
        	else if(query.toUpperCase().startsWith("UPDATE") || query.toUpperCase().startsWith("INSERT") || query.toUpperCase().startsWith("DELETE"))
        	{
        		tableModel = null;
        		int count = queryHelper.executeQuery(query);
        		console.append(count + "row(s) updated \n");
        		jTabbedPane.setSelectedComponent(jScrollPane3);
        	}
        	else if(query.equalsIgnoreCase("COMMIT"))
        	{
        		queryHelper.executeCommit();
        		console.append("Commited change(s) \n");
        		jTabbedPane.setSelectedComponent(jScrollPane3);
        	}
        	else
        	{
        		tableModel = new QueryTableModel(queryHelper.getResultSet(query), 10);
        		jTabbedPane.setSelectedComponent(jScrollPane2);
        	}
        	
            if(tableModel != null)
            {
            	resultTable.setModel(tableModel);
            	resultTable.setVisible(true);
            }
            else
            {
            	resultTable.setVisible(false);
            }
            
        } catch (SQLException f) {
            mainFrame.showDialog("ERROR", f.getMessage(), JOptionPane.ERROR_MESSAGE);
            console.append("ERROR OCCURED : \n");
            StackTraceElement[] stack = f.getStackTrace();
            for(int i = 0 ; i < stack.length; ++i)
            {
            	console.append(stack[i].toString() + "\n");
            }
        }
        mainFrame.setNormalCursor();
        mainFrame.setStatusMessage("Done");
    }

    public JTable getResultTable() {
        return resultTable;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F9) {
            query();
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
