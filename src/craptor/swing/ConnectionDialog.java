package craptor.swing;

import craptor.core.EnvConnection;
import craptor.core.QueryHelper;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class ConnectionDialog extends JDialog implements ActionListener{
    
    private JTextField name;
    private JTextField host;
    private JTextField port;
    private JTextField sid;
    private JTextField username;
    private JPasswordField password;
    private JTextArea url;
    private JList<String> dbType;

    private JButton ok;
    private JButton test;
    private JButton cancel;
    
    private EnvConnection conn;
    private boolean isNew;
    
    public ConnectionDialog(Frame parent, EnvConnection conn, boolean isNew) {
        super(parent, conn.getName());
        this.conn = conn;
        this.isNew = isNew;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 500));
        this.getContentPane().setLayout(new BorderLayout());
        
        ok = new JButton("OK");
        test = new JButton("Test");
        cancel = new JButton("Cancel");
        
        ok.setName("OK");
        test.setName("Test");
        cancel.setName("Cancel");
        
        ok.addActionListener(this);
        test.addActionListener(this);
        cancel.addActionListener(this);
        
        name = new JTextField(conn.getName()==null?"":conn.getName().trim());
        host = new JTextField(conn.getHost()==null?"":conn.getHost().trim());
        port = new JTextField(conn.getPort()==null?"":conn.getPort().trim());
        sid = new JTextField(conn.getSid()==null?"":conn.getSid().trim());
        username = new JTextField(conn.getUserName()==null?"":conn.getUserName().trim());
        password = new JPasswordField(conn.getPassword()==null?"":conn.getPassword().trim());
        url = new JTextArea(conn.getUrl()==null?"":conn.getUrl().trim());
        url.setRows(5);
        dbType = new JList<String>();
        // Set the types of db to list
        Enumeration<Object> dbTypes = CraptorSettings.getDbtypes().keys();
        DefaultListModel<String> lm = new DefaultListModel<String>();
        while(dbTypes.hasMoreElements())
        {
        	lm.addElement(dbTypes.nextElement().toString().trim());
        }
        dbType.setModel(lm);
        dbType.setSelectedValue(conn.getDbtype().trim(), true);
        // End : Set the types of db to list
        
        JPanel tempPanel = new JPanel(new BorderLayout());
        JPanel fieldPanel = new JPanel(new GridLayout(7,2));
        fieldPanel.add(new JLabel("Name:"));
        fieldPanel.add(name, null);
        fieldPanel.add(new JLabel("Host:"));
        fieldPanel.add(host, null);
        fieldPanel.add(new JLabel("Port:"));
        fieldPanel.add(port, null);
        fieldPanel.add(new JLabel("SID:"));
        fieldPanel.add(sid, null);
        fieldPanel.add(new JLabel("UserName:"));        
        fieldPanel.add(username, null);
        fieldPanel.add(new JLabel("Password:"));        
        fieldPanel.add(password, null);
        fieldPanel.add(new JLabel("DB Type :"));
        fieldPanel.add(dbType, null);
        fieldPanel.setBorder(new TitledBorder(new CompoundBorder(new EmptyBorder(10,10,10,10), new LineBorder(Color.gray)), "Connection"));
        
        tempPanel.add(fieldPanel, BorderLayout.NORTH);
        tempPanel.add(url, BorderLayout.SOUTH);
        this.getContentPane().add(tempPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(ok);
        buttonPanel.add(test);
        buttonPanel.add(cancel);
        buttonPanel.setBorder(new EmptyBorder(10,10,10,10));
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /*public static void main(String[] args) {
        JDialog dialog = new ConnectionDialog();
        dialog.setVisible(true);
    }*/

    public void actionPerformed(ActionEvent e) {
        String buttonName = ((JComponent)e.getSource()).getName();
        if("OK".equals(buttonName)) {
            makeConnection();
            conn.setUrl();
            if(isNew){
                ((MainFrame)this.getParent()).saveConnection(conn);
            }
            else {
                
            }
            this.setVisible(false);
        }
        else if("Cancel".equals(buttonName)){
            this.setVisible(false);
        } else if("Test".equals(buttonName)){
            makeConnection();
            String result = QueryHelper.testConnection(conn);
            JOptionPane.showConfirmDialog(this, result ,"Result", JOptionPane.DEFAULT_OPTION);
        }
    }
    
    private void makeConnection() {
        conn.setName(name.getText().trim());
        conn.setHost(host.getText().trim());
        conn.setPort(port.getText().trim());
        conn.setSid(sid.getText().trim());
        conn.setUserName(username.getText().trim());
        conn.setPassword(new String(password.getPassword()).trim());
        conn.setUrl(url.getText().trim());
        conn.setDbtype(dbType.getSelectedValue().toString());
    }
}
