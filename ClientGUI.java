import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

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

    JProgressBar progressBar;

    Insets defaultInsets;

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
        suspendButton.addActionListener(e -> suspendButtonActionPerformed(e));

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

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        startButton.setEnabled(true);

    }

    private void startButtonActionPerformed(ActionEvent e) {
        Thread t = new Thread() {
            public void run() {
                try {
                    Client.send(file);
                    progressBar.setValue(100);
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

    private void suspendButtonActionPerformed(ActionEvent e) {

    }

    private void initialize() {
        startButton.setEnabled(false);
        pathTextField.setText("-");
    }
}