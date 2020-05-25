import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

final public class AddUser extends Dashboard {

    private JButton back;
    private JComboBox<String> combo_sex;
    private JComboBox<String> combo_user_type;
    private JLabel label_password;
    private JLabel label_first_name;
    private JLabel label_last_name;
    private JLabel label_position;
    private JLabel label_sex;
    private JLabel label_user_id;
    private JLabel label_user_type;
    private JPanel panel;
    private JButton submit;
    private JTextField text_first_name;
    private JTextField text_last_name;
    private JTextField text_position;
    private JTextField text_password;
    private JTextField text_user_id;

    private String action, edit_user;

    AddUser(String user_id, String user_type, String user_name) {
        super(user_id, user_type, user_name);
        this.action = "Insert";
        initGUI();
    }

    AddUser(String user_id, String user_type, String user_name, String edit_user) {
        super(user_id, user_type, user_name);
        this.action = "Edit";
        this.edit_user = edit_user;

        initGUI();
        GetUserByID();
        text_user_id.setEditable(false);
    }

    public void initGUI() {
        setTitle("Tambah User");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setSize(new Dimension(800, 600));

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setLayout(new GridLayout(8, 2, 0, 10));
        add(panel, BorderLayout.CENTER);

        label_user_id = new JLabel();
        text_user_id = new JTextField();
        label_user_id.setFont(new Font("Dialog", 1, 14));
        label_user_id.setHorizontalAlignment(SwingConstants.LEFT);
        label_user_id.setText("  ID User");
        panel.add(label_user_id);
        panel.add(text_user_id);

        // Ubah yang ditulis pada kolom 'user' jadi huruf kapital
        DocumentFilter filter = new UppercaseDocumentFilter();
        AbstractDocument userNameDoc = (AbstractDocument) text_user_id.getDocument();
        userNameDoc.setDocumentFilter(filter);

        label_first_name = new JLabel();
        text_first_name = new JTextField();
        label_first_name.setFont(new Font("Dialog", 1, 14));
        label_first_name.setHorizontalAlignment(SwingConstants.LEFT);
        label_first_name.setText("  Nama Depan");
        panel.add(label_first_name);
        panel.add(text_first_name);

        label_last_name = new JLabel();
        text_last_name = new JTextField();
        label_last_name.setFont(new Font("Dialog", 1, 14));
        label_last_name.setHorizontalAlignment(SwingConstants.LEFT);
        label_last_name.setText("  Nama Belakang");
        panel.add(label_last_name);
        panel.add(text_last_name);

        label_sex = new JLabel();
        label_sex.setFont(new Font("Dialog", 1, 14));
        label_sex.setHorizontalAlignment(SwingConstants.LEFT);
        label_sex.setText("  Jenis Kelamin");
        panel.add(label_sex);

        combo_sex = new JComboBox<>();
        combo_sex.setModel(new DefaultComboBoxModel<>(new String[] { "Laki-laki", "Perempuan" }));
        panel.add(combo_sex);

        text_position = new JTextField();
        label_position = new JLabel();
        label_position.setFont(new Font("Dialog", 1, 14));
        label_position.setHorizontalAlignment(SwingConstants.LEFT);
        label_position.setText("  Posisi Jabatan");
        panel.add(label_position);
        panel.add(text_position);

        label_user_type = new JLabel();
        label_user_type.setFont(new Font("Dialog", 1, 14));
        label_user_type.setHorizontalAlignment(SwingConstants.LEFT);
        label_user_type.setText("  Tipe User");
        panel.add(label_user_type);

        combo_user_type = new JComboBox<>();
        combo_user_type.setModel(new DefaultComboBoxModel<>(new String[] { "USER", "ADMIN" }));
        panel.add(combo_user_type);

        label_password = new JLabel();
        text_password = new JTextField();
        label_password.setFont(new Font("Dialog", 1, 14));
        label_password.setHorizontalAlignment(SwingConstants.LEFT);
        label_password.setText("  Password");
        panel.add(label_password);
        panel.add(text_password);

        back = new JButton();
        back.setText("Kembali");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new AdminDashboard(user_id, user_type, user_name);
                getContentPane().setVisible(false);
                dispose();
            }
        });
        panel.add(back);

        submit = new JButton();
        submit.setText("Submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });
        panel.add(submit);

        pack();
        setVisible(true);
    }

    private void submitActionPerformed(ActionEvent evt) {
        if (action.equals("Insert")){
            insertUser();
        } else{
            updateUser();
        }
    }

    private void insertUser(){
        try {
            Connection conn = Koneksi.koneksiDB();
            Statement stm = conn.createStatement();
            String query = "insert into users values( "
                    + "'" + text_user_id.getText() + "',"
                    + "'" + text_first_name.getText() + "',"
                    + "'" + text_last_name.getText() + "',"
                    + "'" + (combo_sex.getSelectedItem() == "Perempuan" ? "F" : "M") + "',"
                    + "'" + text_position.getText() + "',"
                    + "'" + combo_user_type.getSelectedItem() + "',"
                    + "'" + text_password.getText() + "')";
            stm.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Simpan Berhasil!");
            text_user_id.setText("");
            text_first_name.setText("");
            text_last_name.setText("");
            text_position.setText("");
            text_password.setText("");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Maaf Terjadi Kesalahan!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }

    private void updateUser(){
        try {
            Connection conn = Koneksi.koneksiDB();
            Statement stm = conn.createStatement();
            String query = "update users set "
                    + "user_id = " + "'" + text_user_id.getText() + "',"
                    + "first_name = " + "'" + text_first_name.getText() + "',"
                    + "last_name = " + "'" + text_last_name.getText() + "',"
                    + "sex = " + "'" + (combo_sex.getSelectedItem() == "Perempuan" ? "F" : "M") + "',"
                    + "position = " + "'" + text_position.getText() + "',"
                    + "user_type = " + "'" + combo_user_type.getSelectedItem() + "',"
                    + "password = " + "'" + text_password.getText() + "'"
                    + " where user_id = " + "'" + edit_user + "'";
            stm.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Simpan Berhasil!");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Maaf Terjadi Kesalahan!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }

    private void GetUserByID() {
        try {
            Connection conn = Koneksi.koneksiDB();
            Statement stm = conn.createStatement();

            String query = "select * from users where user_id = '" + edit_user + "'";
            ResultSet sql = stm.executeQuery(query);
            if (sql.next()) {
                text_user_id.setText(sql.getString("user_id"));
                text_first_name.setText(sql.getString("first_name"));
                text_last_name.setText(sql.getString("last_name"));
                text_position.setText(sql.getString("position"));
                text_password.setText(sql.getString("password"));
                combo_user_type.setSelectedItem(sql.getString("user_type"));
                combo_sex.setSelectedItem((sql.getString("sex").equals("F")) ? "Perempuan" : "Laki-laki");
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Error!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
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
}
