package craptor.swing;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import craptor.core.EnvConnection;
import craptor.parser.ConnectionsFileHandler;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    private static ResourceBundle bundle;
    
    private JMenuBar menuBar;
    private JLabel statusBar;
    private JToolBar toolBar;   

    private JSplitPane treeTabSplitPane;
    private JScrollPane treeScrollPane;
    private JTabbedPane connectionsTabbedPane;
    private JTree connectionTree;
    private JPopupMenu treePopupMenu;
    private JPopupMenu treePopupMenu1;
    private DefaultTreeModel treeModel;
    private Cursor cursor;
    
    private ConnectionsFileHandler connectionHandler;

    public static void main(String[] args) {
        bundle = ResourceBundle.getBundle("resource");
        MainFrame frame = new MainFrame("Craptor");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MainFrame(String title) {
        super(title);
        cursor = getCursor();
        try {
            initTree();
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getString(String key) {
        return bundle.getString(key);
    }
    
    private void getTreeModel() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Connections");
        List<EnvConnection> connections;
        try {
            connectionHandler = ConnectionsFileHandler.getInstance("connections.xml");
            connections = connectionHandler.getConnections();
            for(int i = 0; i < connections.size(); ++i)
            {
                EnvConnection conn = connections.get(i);
                top.add(new DefaultMutableTreeNode(conn));
            }
        } catch (Exception e) {
            this.showDialog("Connection Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
        treeModel = new DefaultTreeModel(top);        
    }

    private void initTree() {
        //add the connections to JTree as nodes
        getTreeModel();
        connectionTree = new JTree(treeModel);
        DefaultTreeCellRenderer treeRenderer = new DefaultTreeCellRenderer();
        treeRenderer.setOpenIcon(new ImageIcon(MainFrame.class.getResource("/craptor/images/databases1.gif")));
        treeRenderer.setLeafIcon(new ImageIcon(MainFrame.class.getResource("/craptor/images/database.gif")));
        connectionTree.setCellRenderer(treeRenderer);
        connectionTree.getSelectionModel()
        .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        
        //setup the popup menu
        treePopupMenu = new JPopupMenu();
        treePopupMenu1 = new JPopupMenu();
        JMenuItem connect = new JMenuItem("Connect");
        JMenuItem disconnect = new JMenuItem("Disconnect");
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem properties = new JMenuItem("Properties");
        JMenuItem newItem = new JMenuItem("New");
        
        connect.setName("Connect");
        disconnect.setName("Disconnect");
        delete.setName("Delete");
        properties.setName("Properties");
        newItem.setName("New");
        
        treePopupMenu.add(connect);
        treePopupMenu.add(disconnect);
        treePopupMenu.addSeparator();
        treePopupMenu.add(delete);
        treePopupMenu.addSeparator();
        treePopupMenu.add(properties);
        
        treePopupMenu1.add(newItem);
        
        //Add mouse listners
        MouseListener listner = new TreeMouseListner(this);
        connectionTree.addMouseListener(listner);
        connect.addMouseListener(listner);
        disconnect.addMouseListener(listner);
        delete.addMouseListener(listner);
        properties.addMouseListener(listner);
        newItem.addMouseListener(listner);
    }
    public void treeNodeClicked() {
        statusBar.setText("Connecting");
        setWaitCursor();
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)connectionTree.getLastSelectedPathComponent();
        EnvConnection conn = (EnvConnection)node.getUserObject();        
        try{
            QueryPanel queryFrame = new QueryPanel(conn, this);
            connectionsTabbedPane.add(conn.getName(), queryFrame);
            queryFrame.setVisible(true);
        }
        catch(Exception e) {
        	e.printStackTrace();
            this.showDialog("Connection Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            try {
				e.printStackTrace(new PrintStream(new File("log.txt")));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        setNormalCursor();
        statusBar.setText("Ready");
    }
    
    public void treePopupMenuClicked(String name) {
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)connectionTree.getLastSelectedPathComponent();
        
        if("Properties".equals(name)){
            EnvConnection conn = (EnvConnection)node.getUserObject();
            ConnectionDialog dialog = new ConnectionDialog(this, conn, false);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
        else if("Delete".equals(name)) {
            //delete the entry in xml
            EnvConnection conn = (EnvConnection)node.getUserObject();
            connectionHandler.deleteConnection(conn.getName());
            //delete the node
            treeModel.removeNodeFromParent(node);

        }
        else if("New".equals(name)){
            EnvConnection conn = new EnvConnection();
            ConnectionDialog dialog = new ConnectionDialog(this, conn, true);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }
    
    public void saveConnection(EnvConnection conn) {
        connectionHandler.addConnection(conn);
        MutableTreeNode root = (MutableTreeNode)treeModel.getRoot();
        treeModel.insertNodeInto(new DefaultMutableTreeNode(conn), root, root.getChildCount());
    }

    private void jbInit() throws Exception {
    
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        // Add the Menu
        makeMenu();
        this.setJMenuBar(menuBar);
        
        this.getContentPane().setLayout(new BorderLayout());        
        this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 20);
        
        //Add the status bar
        statusBar = new JLabel();
        statusBar.setPreferredSize(new Dimension(100, 16));
        statusBar.setText("Ready");
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

        // Add the toolbar
        makeToolBar();
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        
        //Add splitpane and connection tree
        treeTabSplitPane = new JSplitPane();
        treeScrollPane = new JScrollPane();
        connectionsTabbedPane = new JTabbedPaneWithCloseIcons();
         
        treeTabSplitPane.setDividerLocation(150);
        treeScrollPane.getViewport().add(connectionTree, null);
        treeTabSplitPane.add(treeScrollPane, JSplitPane.LEFT);
        treeTabSplitPane.add(connectionsTabbedPane, JSplitPane.RIGHT);
        this.getContentPane().add(treeTabSplitPane, BorderLayout.CENTER);
    }

    void fileExit_ActionPerformed(ActionEvent e) {
        this.setVisible(false);
        this.dispose();
    }

    void helpAbout_ActionPerformed(ActionEvent e) {
        JOptionPane
        .showMessageDialog(this, new MainFrame_AboutBoxPanel1(), "About",
                                      JOptionPane.PLAIN_MESSAGE);
    }
    
    public void showDialog(String msgHeader, String msgText, int msgType) {
        JPanel errorPanel = new JPanel();
        errorPanel.add(new Label(msgText));
        JOptionPane.showMessageDialog(this, errorPanel, msgHeader, msgType);
        
    }
    
    private void makeMenu() {
        menuBar = new JMenuBar();
        
        JMenu menuFile = new JMenu();

        JMenuItem menuFileExit = new JMenuItem();

        JMenu menuHelp = new JMenu();

        JMenuItem menuHelpAbout = new JMenuItem();
        
        menuFile.setText("File");
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(new ActionListener() {
                                           public void actionPerformed(ActionEvent ae) {
                                               fileExit_ActionPerformed(ae);
                                           }
                                       }
        );
        menuHelp.setText("Help");
        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent ae) {
                                                helpAbout_ActionPerformed(ae);
                                            }
                                        }
        );
        
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        menuHelp.add(menuHelpAbout);
        menuBar.add(menuHelp);
        
    }

    private void makeToolBar() {
    
        toolBar = new JToolBar();
        
        JButton buttonOpen = new JButton();
        JButton buttonSave = new JButton();
        JButton buttonSaveAs = new JButton();
        JButton buttonClose = new JButton();
        JButton buttonHelp = new JButton();
        JButton buttonRun = new JButton();
        
        ImageIcon imageOpen =
            new ImageIcon(MainFrame.class.getResource("/craptor/images/Open24.gif"));
        ImageIcon imageSave =
            new ImageIcon(MainFrame.class.getResource("/craptor/images/Save24.gif"));
        ImageIcon imageSaveAs =
            new ImageIcon(MainFrame.class.getResource("/craptor/images/SaveAs24.gif"));
        ImageIcon imageClose =
            new ImageIcon(MainFrame.class.getResource("/craptor/images/close24.gif"));
       ImageIcon imageHelp =
            new ImageIcon(MainFrame.class.getResource("/craptor/images/help24.gif"));
        ImageIcon imageRun =
            new ImageIcon(MainFrame.class.getResource("/craptor/images/play24.gif"));
            
        
        buttonOpen.setIcon(imageOpen);   
        buttonSave.setIcon(imageSave);  
        buttonSaveAs.setIcon(imageSaveAs);  
        buttonClose.setIcon(imageClose);        
        buttonHelp.setIcon(imageHelp);
        buttonRun.setIcon(imageRun);
        
        buttonOpen.setToolTipText("Open File");
        buttonSave.setToolTipText("Save File");
        buttonSaveAs.setToolTipText("Save File As");
        buttonClose.setToolTipText("Close File");
        buttonHelp.setToolTipText("About");
        buttonRun.setToolTipText("Run");
        
        buttonOpen.addActionListener(new ActionListener(){
                                         public void actionPerformed(ActionEvent e) {
                                             JFileChooser chooser = new JFileChooser();
                                             int returnVal = chooser.showOpenDialog(MainFrame.this);
                                             if(returnVal == JFileChooser.APPROVE_OPTION) {
                                                File file = chooser.getSelectedFile();
                                                ((QueryPanel)connectionsTabbedPane.getSelectedComponent()).openFile(file);
                                             }
                                         }
                                     });
                                     
        buttonSave.addActionListener(new ActionListener(){
                                         public void actionPerformed(ActionEvent e) {
                                             boolean result = ((QueryPanel)connectionsTabbedPane.getSelectedComponent()).saveFile();
                                             if(!result){
                                                JFileChooser chooser = new JFileChooser();
                                                int returnVal = chooser.showSaveDialog(MainFrame.this);
                                                if(returnVal == JFileChooser.APPROVE_OPTION) {
                                                    File file = chooser.getSelectedFile();
                                                    ((QueryPanel)connectionsTabbedPane.getSelectedComponent()).saveFile(file);
                                                }
                                             }
                                         }
                                     });
        buttonSaveAs.addActionListener(new ActionListener(){
                                         public void actionPerformed(ActionEvent e) {
                                             JFileChooser chooser = new JFileChooser();
                                             int returnVal = chooser.showSaveDialog(MainFrame.this);
                                             if(returnVal == JFileChooser.APPROVE_OPTION) {
                                                File file = chooser.getSelectedFile();
                                                ((QueryPanel)connectionsTabbedPane.getSelectedComponent()).saveFile(file);
                                             }
                                         }
                                     });
        
        buttonHelp.addActionListener(new ActionListener(){
                                        public void actionPerformed(ActionEvent ae) {
                                                 helpAbout_ActionPerformed(ae);
                                        }
                                     });
        
        buttonRun.addActionListener(new ActionListener(){
                                        public void actionPerformed(ActionEvent e) {
                                            ((QueryPanel)connectionsTabbedPane.getSelectedComponent()).query();
                                        }
                                    });

        toolBar.add(buttonOpen);
        toolBar.add(buttonSave);
        toolBar.add(buttonSaveAs);
        toolBar.add(buttonClose);
        toolBar.add(buttonHelp);
        toolBar.add(buttonRun);
    }

    public void setStatusMessage(String text) {
        statusBar.setText(text);
    }
    
    public void setWaitCursor() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    public void setNormalCursor(){
        setCursor(cursor);
    }

    public JPopupMenu getTreePopupMenu() {
        return treePopupMenu;
    }

    public JTree getConnectionTree() {
        return connectionTree;
    }

    public JPopupMenu getTreePopupMenu1() {
        return treePopupMenu1;
    }
}
