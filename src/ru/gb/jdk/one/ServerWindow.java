package ru.gb.jdk.one;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_WIDTH = 550;
    private static final int WINDOW_POSX = 500;
    private static final int WINDOW_POSY = 100;
    private static final String FILE_NAME = "log.txt";
    ClientGUI clientGUI;
    List<ClientGUI> clientGUIList;
    JButton btnStart, btnStop;
    JTextArea log;
    private boolean isServerWorking;


    ServerWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(WINDOW_POSX, WINDOW_POSY, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Chat server");
        setResizable(false);
        isServerWorking = false;
        clientGUIList = new ArrayList<>();

        add(createTextArea());
        add(createButtonPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private Component createButtonPanel() {
        JPanel btnPanel = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerWorking) {
                    log.append("Server is working!\n");
                } else {
                    isServerWorking = true;
                    log.append("Server started!\n");
                }
            }
        });

        btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isServerWorking) {
                    log.append("Server is already stopped!\n");
                } else {
                    isServerWorking = false;
                    for (ClientGUI clientGUI : clientGUIList) {
                        disconnectUser(clientGUI);
                    }
                    log.append("Server stopped!\n");
                }
            }
        });

        btnPanel.add(btnStart);
        btnPanel.add(btnStop);

        return btnPanel;
    }

    private Component createTextArea() {
        log = new JTextArea();
        return log;
    }

    private void fileWriter(String s) {
        try(BufferedWriter bfWriter = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bfWriter.append(s);
            bfWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fileReader() {
        StringBuilder strBuilder = new StringBuilder();
        try(FileReader reader = new FileReader(FILE_NAME)){
            int c;
            while ((c = reader.read()) != -1) {
                strBuilder.append((char) c);
            }
            strBuilder.delete(strBuilder.length() - 1, strBuilder.length());
            return strBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getLog() {
        return fileReader();
    }

    private void appendLog(String str) {
        log.append(str + "\n");
    }

    private void answerAll(String text) {
        for (ClientGUI clientGUI: clientGUIList) {
            clientGUI.answer(text);
        }
    }

    public boolean connectUser(ClientGUI clientGUI) {
        if (!isServerWorking) {
            return false;
        }
        clientGUIList.add(clientGUI);
        return true;
    }

    public void disconnectUser(ClientGUI clientGUI) {
        clientGUIList.remove(clientGUI);
        if (clientGUIList != null) {
            clientGUI.disconnectServer();
        }
    }

    public void message(String text) {
        if (!isServerWorking) {
            appendLog("Не удалось подключиться к серверу!");
        }
        appendLog(text);
        answerAll(text);
        fileWriter(text);
    }

}