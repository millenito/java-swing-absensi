import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class AdminDashboard extends Dashboard {

    private JButton sign_out;
    private JButton btn_absen;
    private JButton btn_add_user;
    private JPanel panel;
    private JPanel panel_atas;
    private JPanel panel_absen;
    private JPanel panel_add_user;
    private JPanel panel_table;
    private JScrollPane panel_scroll;
    private JTable tbl_list_user;
    private DefaultTableModel tableModel;

    AdminDashboard(String user_id, String user_type, String user_name) {
        super(user_id, user_type, user_name);
        initGUI();
    }

    public void initGUI() {
        setTitle("Admin");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setSize(new Dimension(800, 600));

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        panel_atas = new JPanel();
        panel_atas.setLayout(new BoxLayout(panel_atas, BoxLayout.LINE_AXIS));
        panel.add(panel_atas, BorderLayout.PAGE_START);

         // Tombol Signout
        sign_out = new JButton();
        sign_out.setText("Sign out");
        sign_out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                signout(evt);
            }
        });
        panel_atas.add(sign_out);

        // Tombol Absen
        panel_absen = new JPanel();
        panel_absen.setLayout(new FlowLayout(FlowLayout.LEFT));
        if(!checkAbsen()) {
            btn_absen = new JButton();
            btn_absen.setText("Absen");
            btn_absen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (insertAbsen()) {
                        checkAbsen();
                        getUsers();
                        btn_absen.setVisible(false);
                    }
                }
            });
            panel_absen.add(btn_absen);
        }
        panel_atas.add(panel_absen);

        // Tombol Tambah User
        panel_add_user = new JPanel();
        panel_add_user.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btn_add_user = new JButton();
        btn_add_user.setText("Tambah");
        btn_add_user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AddUser(user_id, user_type, user_name);
                getContentPane().setVisible(false);
                dispose();
            }
        });
        panel_add_user.add(btn_add_user);
        panel_atas.add(panel_add_user);

        panel_table = new JPanel();
        panel_table.setLayout(new BorderLayout());
        panel.add(panel_table, BorderLayout.CENTER);

        tbl_list_user = new JTable();
        tableModel = new DefaultTableModel(null,
                new String[]{
                        "ID User", "Nama", "Sudah Absen", "Edit", "Hapus"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        tbl_list_user.setModel(tableModel);
        getUsers();
        tbl_list_user.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        tbl_list_user.getColumn("Edit").setCellEditor(new ButtonColumn(new JCheckBox()));
        tbl_list_user.getColumn("Hapus").setCellRenderer(new ButtonRenderer());
        tbl_list_user.getColumn("Hapus").setCellEditor(new ButtonColumn(new JCheckBox()));

        panel_scroll = new JScrollPane();
        panel_scroll.setViewportView(tbl_list_user);
        if (tbl_list_user.getColumnModel().getColumnCount() > 0) {
            tbl_list_user.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbl_list_user.getColumnModel().getColumn(2).setMaxWidth(100);
            tbl_list_user.getColumnModel().getColumn(3).setMaxWidth(80);
            tbl_list_user.getColumnModel().getColumn(3).setPreferredWidth(80);
            tbl_list_user.getColumnModel().getColumn(4).setMaxWidth(80);
            tbl_list_user.getColumnModel().getColumn(4).setPreferredWidth(80);
        }
        panel_table.add(panel_scroll, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void btnEditAction(){
        int column = 0;
        int row = tbl_list_user.getSelectedRow();
        String value = tbl_list_user.getModel().getValueAt(row, column).toString();

        new AddUser(user_id, user_type, user_name, value);
        getContentPane().setVisible(false);
        dispose();
    }

    private void btnHapusAction(){
        int column = 0;
        int row = tbl_list_user.getSelectedRow();
        String value = tbl_list_user.getModel().getValueAt(row, column).toString();

        int input = JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin menghapus user " + value + "?", "Hapus User", JOptionPane.YES_NO_OPTION);
        if (input == 0){
            deleteUser(value);
            getUsers();
        }
    }

    // Mengisi table dengan data2 users
    private void getUsers() {
        try {
            Connection conn = Koneksi.koneksiDB();
            Statement stm = conn.createStatement();
            String query = "select "
                            + "u.user_id,"
                            + "u.first_name,"
                            + "u.last_name,"
                            + "(case"
                            + "     when ("
                            + "     select"
                            + "         count(a.absen_time)"
                            + "     from"
                            + "         absen_harian a"
                            + "     where"
                            + "         a.user_id = u.user_id"
                            + "         and a.absen_date = '" + date_now + "') > 0 then 'Ya'"
                            + "     else 'Belum'"
                            + " END) as sudah_absen"
                          + "  from"
                          + "  users u";
            ResultSet sql = stm.executeQuery(query);

            tableModel.getDataVector().removeAllElements();
            tableModel.fireTableDataChanged();
            while (sql.next()) {
                Object[] data = {
                        sql.getString("user_id"),
                        sql.getString("first_name") + " " + sql.getString("last_name"),
                        sql.getString("sudah_absen"),
                        "Edit",
                        "Hapus"
                };
                tableModel.addRow(data);
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Error!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }

    private void deleteUser(String delete_user_id){
        try {
            Connection conn = Koneksi.koneksiDB();
            Statement stm = conn.createStatement();
            String query = "delete from users where user_id = " + "'" + delete_user_id + "'";
            stm.executeUpdate(query);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Maaf Terjadi Kesalahan!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonColumn extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonColumn(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                if(label.equals("Edit")){
                    btnEditAction();
                }else if (label.equals("Hapus")){
                    btnHapusAction();
                }
            }
            isPushed = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
