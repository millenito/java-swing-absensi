import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        System.out.println(absen_status);
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
        panel_add_user.add(btn_add_user);
        panel_atas.add(panel_add_user);

        panel_table = new JPanel();
        panel_table.setLayout(new BorderLayout());
        panel.add(panel_table, BorderLayout.CENTER);

        tbl_list_user = new JTable();
        tableModel = new DefaultTableModel(null,
                new String[]{
                        "ID User", "Nama", "Sudah Absen", "", ""
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        tbl_list_user.setModel(tableModel);
        getUsers();

        panel_scroll = new JScrollPane();
        panel_scroll.setViewportView(tbl_list_user);
        if (tbl_list_user.getColumnModel().getColumnCount() > 0) {
            tbl_list_user.getColumnModel().getColumn(3).setPreferredWidth(50);
            tbl_list_user.getColumnModel().getColumn(3).setMaxWidth(50);
            tbl_list_user.getColumnModel().getColumn(4).setPreferredWidth(50);
            tbl_list_user.getColumnModel().getColumn(4).setMaxWidth(50);
        }
        panel_table.add(panel_scroll, BorderLayout.CENTER);

        pack();
        setVisible(true);
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
                };
                tableModel.addRow(data);
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Error!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }

//    public static void main(String args[]) {
//        new AdminDashboard();
//    }
}
