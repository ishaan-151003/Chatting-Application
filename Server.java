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


public class Server implements ActionListener {
    JTextField text1;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;

    Server() {
        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(0, 0, 0)); // Neon blue background
        p1.setBounds(0, 0, 450, 75);
        p1.setLayout(null);
        f.add(p1);

        JLabel back = new JLabel("◄");
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 18));
        back.setBounds(10, 20, 25, 25);
        p1.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        JLabel profile = new JLabel("G");
        profile.setForeground(Color.WHITE);
        profile.setFont(new Font("Arial", Font.BOLD, 30));
        profile.setBounds(50, 10, 50, 50);
        f.setUndecorated(true);
        p1.add(profile);

        JLabel video = new JLabel("V");
        video.setForeground(Color.WHITE);
        video.setFont(new Font("Arial", Font.BOLD, 20));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        JLabel phone = new JLabel("☎");
        phone.setForeground(Color.WHITE);
        phone.setFont(new Font("Arial", Font.BOLD, 20));
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        JLabel more = new JLabel("...");
        more.setForeground(Color.WHITE);
        more.setFont(new Font("Arial", Font.BOLD, 20));
        more.setBounds(420, 20, 10, 25);
        p1.add(more);

        JLabel name = new JLabel("Gaitonde");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 18));
        p1.add(name);

        JLabel Status = new JLabel("Active");
        Status.setBounds(110, 35, 100, 18);
        Status.setForeground(Color.WHITE);
        Status.setFont(new Font("Arial", Font.BOLD, 14));
        p1.add(Status);

        a1 = new JPanel();
        a1.setBounds(5, 75, 425, 570);
        a1.setBackground(Color.WHITE); 
        f.add(a1);

        text1 = new JTextField();
        text1.setBounds(5, 655, 310, 40);
        text1.setFont(new Font("Arial", Font.PLAIN, 16));
        text1.setBackground(new Color(0, 0, 0)); // Neon blue background
        text1.setForeground(Color.WHITE);
        text1.setCaretColor(Color.WHITE);
        f.add(text1);

        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(57, 255, 20)); // Neon orange button
        send.setForeground(Color.WHITE);
        send.setFont(new Font("Arial", Font.BOLD, 16));
        send.addActionListener(this);
        f.add(send);

        f.setSize(450, 750);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(new Color(0, 0, 0)); // Dark background
        
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String out = text1.getText();

            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);

            text1.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } 
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(65, 105, 225)); // Neon blue background
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        output.setForeground(Color.WHITE);

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        time.setForeground(Color.GRAY);

        panel.add(time);

        return panel;
    }

    public static void main(String args[]) {
        new Server();
        try {
            ServerSocket skt = new ServerSocket(6001);
            while (true) {
                Socket s=skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                while (true) {
                    String mag = din.readUTF();
                    JPanel panel = formatLabel(mag);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    f.validate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
