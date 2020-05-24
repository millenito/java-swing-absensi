import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Login extends JFrame implements ActionListener {

    private JPanel panel;
    private JLabel user_label, password_label;
    private JTextField userName_text;
    private JPasswordField password_text;
    private JButton submit;

    private String user_id, user_type, user_name;

    Login() {
        initGUI();
    }

    private void initGUI(){
        setTitle("Login");
        setSize(430, 230);
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

        // Ubah yang ditulis pada kolom 'user' jadi huruf kapital
        DocumentFilter filter = new UppercaseDocumentFilter();
        AbstractDocument userNameDoc = (AbstractDocument) userName_text.getDocument();
        userNameDoc.setDocumentFilter(filter);

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
        submit.addActionListener(this);
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userName = userName_text.getText();
        String password = password_text.getText();
        if(auth(userName, password)){
            new UserDashboard(user_id, user_type, user_name);
            getContentPane().setVisible(false);
            dispose();
        }else{
            userName_text.setText("");
            password_text.setText("");
        }
    }

    // Authentikasi dengan data dari database
    private boolean auth(String userName, String password) {
        try {
            Connection conn = Koneksi.koneksiDB();
            Statement stm = conn.createStatement();

            String query = "select * from users where user_id = '" + userName + "' and password = '" + password + "'";
            ResultSet sql = stm.executeQuery(query);

            if (sql.next()) {
                user_id = sql.getString("user_id");
                user_name = sql.getString("first_name");
                user_type = sql.getString("user_type");
                System.out.println("login berhasil!");

                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Maaf! password atau user anda salah, silahkan coba lagi");
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Error!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }

        return false;
    }

    class UppercaseDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
            fb.insertString(offset, text.toUpperCase(), attr);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            fb.replace(offset, length, text.toUpperCase(), attrs);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}