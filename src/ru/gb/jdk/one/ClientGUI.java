package ru.gb.jdk.one;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame {
    private static final int WINDOW_HEIGHT = 300;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_POSX = 500;
    private static final int WINDOW_POSY = 100;

    String name;
    ServerWindow server;
    JTextArea log;
    JTextField loginField, passwordField, IPField, portField, messageField;
    JButton sendMessageButton, loginButton;
    private String [] data = new String[]{"Vlad", "Masha", "Kira", "Phillip", "Victor"};;
    JList <String> clientList;
    private boolean connected;

    ClientGUI(ServerWindow server) {
        this.server = server;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(WINDOW_POSX, WINDOW_POSY,WINDOW_WIDTH,WINDOW_HEIGHT);
        setTitle("Client Chat");
        setResizable(false);


        add(createTextFields(), BorderLayout.NORTH);
        add(createTextAndButtonArea(), BorderLayout.SOUTH);
        add(createDataList());
        add(createLog());
        setVisible(true);
    }

    private Component createTextFields() {
        JPanel textFieldPanel = new JPanel(new GridLayout(2,5));
        loginField = new JTextField("ivan_igorevich");
        passwordField = new JPasswordField("123456");
        IPField = new JTextField("127.0.0.1");
        portField = new JTextField("8189");
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectServer();
            }
        });

        textFieldPanel.add(IPField);
        textFieldPanel.add(portField);
        textFieldPanel.add(loginField);
        textFieldPanel.add(passwordField);
        textFieldPanel.add(loginButton);

        return textFieldPanel;
    }

    private Component createTextAndButtonArea() {
        JPanel textBtnPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appendLog(messageField.getText());
                messageField.setText(null);
            }
        });

        sendMessageButton = new JButton("Send");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appendLog(messageField.getText());
                messageField.setText(null);
            }
        });

        textBtnPanel.add(messageField, BorderLayout.CENTER);
        textBtnPanel.add(sendMessageButton,BorderLayout.EAST);
        return textBtnPanel;
    }


    private Component createLog() {
        log = new JTextArea();
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        return scrollLog;
    }

    private void appendLog(String str) {
        log.append(str + "\n");
    }

    private Component createDataList() {
        clientList = new JList<>(data);
        return clientList;
    }

    public void connectServer() {
        if (server.connectUser(this)) {
            appendLog("Подключение прошло успешно!");
            connected = true;
            name = loginField.getText();
            appendLog(server.getLog());
        } else {
            appendLog("Не удалось подключиться к серверу!");
        }
    }
}
