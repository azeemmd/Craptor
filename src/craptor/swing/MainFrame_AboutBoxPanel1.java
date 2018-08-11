package craptor.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class MainFrame_AboutBoxPanel1 extends JPanel {
    private JLabel labelTitle = new JLabel();

    private JLabel labelAuthor = new JLabel();

    private JLabel labelCopyright = new JLabel();

    private GridBagLayout layoutMain = new GridBagLayout();

    private Border border = BorderFactory.createEtchedBorder();

    public MainFrame_AboutBoxPanel1() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(layoutMain);
        this.setBorder(border);
        labelTitle.setText("Title : Craptor");
        labelAuthor.setText("Author : Azeem Md");
        labelCopyright.setText("Copyright : 2006");
        this
        .add(labelTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints
                                                    .WEST,
                                                    GridBagConstraints.NONE,
                                                    new Insets(5, 15, 0, 15),
                                                    0, 0));
        this
        .add(labelAuthor, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints
                                                     .WEST,
                                                     GridBagConstraints.NONE,
                                                     new Insets(0, 15, 0, 15),
                                                     0, 0));
        this
        .add(labelCopyright, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints
                                                        .WEST,
                                                        GridBagConstraints
                                                        .NONE,
                                                        new Insets(0, 15, 0,
                                                                          15),
                                                        0, 0));
    }
}
