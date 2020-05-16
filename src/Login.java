import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Login implements ActionListener {

    JFrame frame = new JFrame();
    JPanel panel = new JPanel(new GridLayout(3, 1));
    JLabel user_label, password_label, message;
    JTextField userName_text;
    JPasswordField password_text;
    JButton submit, cancel;

    Login() {
        frame.setSize(430,230);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setLayout(null);
//        panel.setMaximumSize(new Dimension(215, 115));
        frame.add(panel, BorderLayout.CENTER);

        user_label = new JLabel("User");
        user_label.setBounds(10,20,80,25);
        panel.add(user_label);

        userName_text = new JTextField();
        userName_text.setBounds(100,20,165,25);
        panel.add(userName_text);

        password_label = new JLabel("Password");
        password_label.setBounds(10,50,80,25);
        panel.add(password_label);

        password_text = new JPasswordField();
        password_text.setBounds(100,50,165,25);
        panel.add(password_text);

        frame.setTitle("Login");
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new Login();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userName = userName_text.getText();
        String password = password_text.getText();
        if (userName.trim().equals("admin") && password.trim().equals("admin")) {
            message.setText(" Hello " + userName
                    + "");
        } else {
            message.setText(" Invalid user.. ");
        }

    }

}