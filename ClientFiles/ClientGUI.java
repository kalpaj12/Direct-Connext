import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.awt.Desktop.*;
import java.awt.Image.*;
import java.awt.*;


public class ClientGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    public int ProgressINT;
    String directory;
    File file;

    JScrollPane scrollPane;
    JList fileList;
    DefaultListModel listModel;

    JTextField pathTextField;

    JPanel buttonPanel;
    JButton pathButton;
    JButton startButton;
    JButton suspendButton;

    JFileChooser fileChooser;

    static JProgressBar progressBar;

    Insets defaultInsets;
    Image Icon = Toolkit.getDefaultToolkit().getImage("Untitled.png");

    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.setVisible(true);
    }

    public ClientGUI() {

        scrollPane = new JScrollPane();
        fileList = new JList();
        listModel = new DefaultListModel();

        pathTextField = new JTextField();

        buttonPanel = new JPanel();
        pathButton = new JButton();
        startButton = new JButton();
        suspendButton = new JButton();

        fileChooser = new JFileChooser();

        progressBar = new JProgressBar();

        defaultInsets = new Insets(3, 3, 3, 3);

        scrollPane.setPreferredSize(new Dimension(400, 250));
        scrollPane.setViewportView(fileList);
        fileList.setModel(listModel);

        pathTextField.setColumns(30);
        pathTextField.setEditable(false);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder());
        pathButton.setText("Choose File");
        startButton.setText("Begin Upload");

        progressBar.setPreferredSize(new Dimension(400, 20));

        initialize();

        startButton.addActionListener(e -> startButtonActionPerformed(e));
        pathButton.addActionListener(e -> pathButtonActionPerformed(e));

        Container contents = this.getContentPane();
        contents.setLayout(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.insets = defaultInsets;
        int y = 0;

        gridConstraints.gridx = 0;
        gridConstraints.gridy = y++;
        contents.add(progressBar, gridConstraints);

        gridConstraints.gridy = y++;
        contents.add(pathTextField, gridConstraints);
        gridConstraints.gridy = y++;
        contents.add(buttonPanel, gridConstraints);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.insets = defaultInsets;

        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.gridy = 0;
        buttonPanel.add(startButton, buttonPanelConstraints);

        buttonPanelConstraints.gridx = 1;
        buttonPanel.add(pathButton, buttonPanelConstraints);

        setTitle("ClientGUI Application");
        setIconImage(Icon);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        startButton.setEnabled(true);

    }

    private void startButtonActionPerformed(ActionEvent e) {
        Thread t = new Thread() {
            public void run() {
                try {
                    if (file != null) {
                        progressBar.setValue(0);
                        Client.send(file);
                    } else {
                        System.out.println("File not Selected");
                    }
                } catch (IOException ioe) {
                    System.out.println("Exception at upload.");
                }
            }
        };
        t.start();

    }

    private void pathButtonActionPerformed(ActionEvent e) {
        int result = fileChooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            pathTextField.setText(file.toString());
            progressBar.setValue(0);
        }
    }

    private void initialize() {
        startButton.setEnabled(false);
        pathTextField.setText("-");
    }
}