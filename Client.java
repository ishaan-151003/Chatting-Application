package Chatting_application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.*;
import java.util.Calendar;
import java.net.*;

public class Client implements ActionListener {
    JTextField text1;
    static JPanel chatPanel;
    static Box chatBox = Box.createVerticalBox(); // Moved inside Client class
    static DataOutputStream outputStream;
    static JFrame frame = new JFrame();

    Client() {
        frame.setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 0)); // Neon blue background
        topPanel.setBounds(0, 0, 450, 75);
        topPanel.setLayout(null);
        frame.add(topPanel);

        JLabel back = new JLabel("◄");
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 18));
        back.setBounds(10, 20, 25, 25);
        topPanel.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        JLabel profile = new JLabel("B");
        profile.setForeground(Color.WHITE);
        profile.setFont(new Font("Arial", Font.BOLD, 30));
        profile.setBounds(50, 10, 50, 50);
        frame.setUndecorated(true);
        topPanel.add(profile);

        JLabel video = new JLabel("V");
        video.setForeground(Color.WHITE);
        video.setFont(new Font("Arial", Font.BOLD, 20));
        video.setBounds(300, 20, 30, 30);
        topPanel.add(video);

        JLabel phone = new JLabel("☎");
        phone.setForeground(Color.WHITE);
        phone.setFont(new Font("Arial", Font.BOLD, 20));
        phone.setBounds(360, 20, 35, 30);
        topPanel.add(phone);

        JLabel more = new JLabel("...");
        more.setForeground(Color.WHITE);
        more.setFont(new Font("Arial", Font.BOLD, 20));
        more.setBounds(420, 20, 10, 25);
        topPanel.add(more);

        JLabel nameLabel = new JLabel("Bunty");
        nameLabel.setBounds(110, 15, 100, 18);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(nameLabel);

        JLabel statusLabel = new JLabel("Active");
        statusLabel.setBounds(110, 35, 100, 18);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(statusLabel);

        chatPanel = new JPanel();
        chatPanel.setBounds(5, 75, 425, 570);
        chatPanel.setBackground(Color.WHITE);
        frame.add(chatPanel);

        text1 = new JTextField();
        text1.setBounds(5, 655, 310, 40);
        text1.setFont(new Font("Arial", Font.PLAIN, 16));
        text1.setBackground(new Color(0, 0, 0)); // Neon blue background
        text1.setForeground(Color.WHITE);
        text1.setCaretColor(Color.WHITE);
        frame.add(text1);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(320, 655, 123, 40);
        sendButton.setBackground(new Color(57, 255, 20)); // Neon orange button
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.addActionListener(this);
        frame.add(sendButton);

        frame.setSize(450, 750);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(0, 0, 0)); // Dark background
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String message = text1.getText();
            JPanel messagePanel = formatMessage(message);

            chatPanel.setLayout(new BorderLayout());
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.add(messagePanel, BorderLayout.LINE_END);
            chatBox.add(rightPanel);
            chatBox.add(Box.createVerticalStrut(15));
            chatPanel.add(chatBox, BorderLayout.PAGE_START);
            outputStream.writeUTF(message);
            text1.setText("");
            frame.repaint();
            frame.invalidate();
            frame.validate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JPanel formatMessage(String message) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel("<html><p style=\"width: 150px\">" + message + "</p></html>");
        messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        messageLabel.setBackground(new Color(65, 105, 225)); // Neon blue background
        messageLabel.setOpaque(true);
        messageLabel.setBorder(new EmptyBorder(15, 15, 15, 50));
        messageLabel.setForeground(Color.WHITE);

        panel.add(messageLabel);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel timeLabel = new JLabel();
        timeLabel.setText(sdf.format(cal.getTime()));
        timeLabel.setForeground(Color.GRAY);

        panel.add(timeLabel);

        return panel;
    }

    public static void main(String args[]) {
        new Client();
        try {
            Socket socket = new Socket("127.0.0.1", 6001);
            outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                chatPanel.setLayout(new BorderLayout());
                String receivedMessage = inputStream.readUTF();
                JPanel receivedPanel = formatMessage(receivedMessage);
                JPanel leftPanel = new JPanel(new BorderLayout());
                leftPanel.add(receivedPanel, BorderLayout.LINE_START);
                chatBox.add(leftPanel);
                chatBox.add(Box.createVerticalStrut(15));
                chatPanel.add(chatBox, BorderLayout.PAGE_START);
                frame.validate();
                frame.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
