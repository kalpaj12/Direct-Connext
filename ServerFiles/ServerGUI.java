import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.File;
import javax.swing.*;
import java.util.Calendar;
import java.text.DecimalFormat;
import java.net.*;
import java.awt.Desktop.*;
import java.awt.Image.*;
import java.awt.*;

public class ServerGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final int REFRESH_RATE = 1000;

    private static final int DEFAULT_FONT_SIZE = 14;

    File directory;
    long startTime;
    long stopTime;
    double elapsedTime;
    Calendar calendar = Calendar.getInstance();

    JLabel storageLabel;

    JScrollPane scrollPane;
    JList fileList;
    DefaultListModel listModel;

    JLabel pathLabel;
    JTextField storageTextField;

    JLabel serverLabel;
    JPanel serverPanel;
    JLabel uptimeLabel;
    JTextField uptimeTextField;
    JLabel startedLabel;
    JTextField startedTextField;

    JPanel buttonPanel;
    JButton pathButton;
    JButton startButton;

    JFileChooser fileChooser;

    Timer timer;
    Insets defaultInsets;
    DecimalFormat defaultDecimalFormat;

    Image Icon = Toolkit.getDefaultToolkit().getImage("Untitled.png");

    public static void main(String[] args) {
        ServerGUI serverGUI = new ServerGUI();
        serverGUI.setVisible(true);
    }

    public ServerGUI() {

        storageLabel = new JLabel();

        scrollPane = new JScrollPane();
        fileList = new JList();
        listModel = new DefaultListModel();

        pathLabel = new JLabel();
        storageTextField = new JTextField();

        serverLabel = new JLabel();
        serverPanel = new JPanel();
        uptimeLabel = new JLabel();
        uptimeTextField = new JTextField();
        startedLabel = new JLabel();
        startedTextField = new JTextField();

        buttonPanel = new JPanel();
        pathButton = new JButton();
        startButton = new JButton();

        fileChooser = new JFileChooser();

        defaultInsets = new Insets(3, 3, 3, 3);
        defaultDecimalFormat = new DecimalFormat("0.00");

        storageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, DEFAULT_FONT_SIZE));
        storageLabel.setText("Files in Storage: ");

        scrollPane.setPreferredSize(new Dimension(400, 250));
        scrollPane.setViewportView(fileList);

        pathLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, DEFAULT_FONT_SIZE));
        pathLabel.setText("Storage Path: ");
        storageTextField.setPreferredSize(new Dimension(400, 30));
        storageTextField.setEditable(false);

        serverLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, DEFAULT_FONT_SIZE));
        serverLabel.setText("Server Monitor:");
        serverPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        uptimeTextField.setEditable(false);
        uptimeTextField.setColumns(30);
        startedTextField.setEditable(false);
        startedTextField.setColumns(30);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder());
        pathButton.setText("Choose Storage");
        startButton.setText("Start Server");

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        initialize();

        startButton.addActionListener(e -> startButtonActionPerformed(e));
        pathButton.addActionListener(e -> pathButtonActionPerformed(e));

        timer = new Timer(REFRESH_RATE, e -> timerActionPerformed(e));

        Container contents = this.getContentPane();
        contents.setLayout(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.insets = defaultInsets;
        int y = 0;

        gridConstraints.gridx = 0;
        gridConstraints.gridy = y++;
        contents.add(storageLabel, gridConstraints);
        gridConstraints.gridy = y++;
        contents.add(scrollPane, gridConstraints);
        gridConstraints.gridy = y++;
        contents.add(pathLabel, gridConstraints);
        gridConstraints.gridy = y++;
        contents.add(storageTextField, gridConstraints);
        gridConstraints.gridy = y++;
        contents.add(serverLabel, gridConstraints);
        gridConstraints.gridy = y++;
        contents.add(serverPanel, gridConstraints);
        serverPanel.setLayout(new GridBagLayout());
        GridBagConstraints serverPanelConstraints = new GridBagConstraints();
        serverPanelConstraints.insets = defaultInsets;

        serverPanelConstraints.gridx = 0;
        serverPanelConstraints.gridy = 0;
        serverPanelConstraints.anchor = GridBagConstraints.WEST;
        serverPanel.add(uptimeLabel, serverPanelConstraints);
        serverPanelConstraints.gridx = 1;
        serverPanelConstraints.anchor = GridBagConstraints.EAST;
        serverPanel.add(uptimeTextField, serverPanelConstraints);
        serverPanelConstraints.gridx = 0;
        serverPanelConstraints.gridy = 1;
        serverPanelConstraints.anchor = GridBagConstraints.WEST;
        serverPanel.add(startedLabel, serverPanelConstraints);
        serverPanelConstraints.gridx = 1;
        serverPanelConstraints.anchor = GridBagConstraints.EAST;
        serverPanel.add(startedTextField, serverPanelConstraints);

        InetAddress ip;
        String hostname = null;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostAddress();
        } catch (UnknownHostException e) {

        }

        System.out.println("Server IP:" + hostname);

        gridConstraints.gridy = y++;
        contents.add(buttonPanel, gridConstraints);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.insets = defaultInsets;

        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.gridy = 0;
        buttonPanel.add(startButton, buttonPanelConstraints);
        buttonPanelConstraints.gridx = 1;

        buttonPanelConstraints.gridx = 2;
        buttonPanel.add(pathButton, buttonPanelConstraints);

        setTitle("ServerGUI Application");
        setIconImage(Icon);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

    }

    private void startButtonActionPerformed(ActionEvent e) {
        pathButton.setEnabled(false);

        resetServerLabels();
        startedTextField.setText(calendar.getTime().toString());
        startTime = System.currentTimeMillis();
        uptimeTextField.setText("-");
        timer.start();

        Thread t = new Thread() {
            public void run() {
                try {
                    Server.setPath(directory.toString());
                    Server.listen();
                } catch (IOException ioe) {
                    System.out.println("Listen server exception.");
                }
            }
        };
        t.start();
        startButton.setEnabled(false);

    }

    private void pathButtonActionPerformed(ActionEvent e) {
        int result = fileChooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            directory = fileChooser.getSelectedFile();
            Server.setPath(directory.toString());
            updateStorageField();
            updateFileList();
            fileChooser.setSelectedFile(directory);
            startButton.setEnabled(true);
        }
    }

    private void timerActionPerformed(ActionEvent e) {

        elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
        uptimeTextField.setText(defaultDecimalFormat.format(elapsedTime));

        updateFileList();
    }

    private void updateStorageField() {
        storageTextField.setText(directory.toString());
    }

    private void updateFileList() {
        listModel.clear();
        for (File file : directory.listFiles()) {
            String FileName = file.getName();
            if (FileName.compareToIgnoreCase("FilesinServer.txt") != 0)
                listModel.addElement(file.getName());
        }
        fileList.setModel(listModel);
    }

    private void resetServerFields() {
        uptimeTextField.setText("-");
        startedTextField.setText("-");
    }

    private void resetServerLabels() {
        uptimeLabel.setText("Uptime(sec): ");
        startedLabel.setText("Started: ");
    }

    private void initialize() {
        storageTextField.setText("-");
        startButton.setEnabled(false);

        resetServerLabels();
        resetServerFields();
    }
}