import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Login extends JFrame implements ActionListener {

    JPanel panel;
    JLabel user_label, password_label, message;
    JTextField userName_text;
    JPasswordField password_text;
    JButton submit, cancel;

    Login() {
        setTitle("Login");
        setSize(430,230);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridBagConstraints gbc;

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        add(panel);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(132, 103, 0, 0);
        user_label = new JLabel("User");
        panel.add(user_label, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.ipadx = 100;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(130, 38, 0, 111);
        userName_text = new JTextField();
        panel.add(userName_text, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(20, 105, 0, 0);
        password_label = new JLabel("Password");
        panel.add(password_label, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.ipadx = 100;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(18, 38, 0, 111);
        password_text = new JPasswordField();
        panel.add(password_text, gbc);

        submit = new JButton("Submit");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(30, 200, 102, 0);
        panel.add(submit, gbc);

        pack();
        setVisible(true);
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