import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {

    private JButton btn_absen;
    private JButton btn_add_user;
    private JPanel panel;
    private JPanel top_panel;
    private JPanel panel_absen;
    private JPanel panel_add_user;
    private JPanel table_panel;
    private JScrollPane scroll_panel;
    private JTable tbl_list_user;
    private DefaultTableModel tableModel;

    public AdminDashboard() {
        initGUI();
    }

    private void initGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setSize(new Dimension(800, 600));

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        top_panel = new JPanel();
        top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.LINE_AXIS));
        panel.add(top_panel, BorderLayout.PAGE_START);

        // Tombol Absen
        panel_absen = new JPanel();
        panel_absen.setLayout(new FlowLayout(FlowLayout.LEFT));
        btn_absen = new JButton();
        btn_absen.setText("Absen");
        btn_absen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btn_absenActionPerformed(evt);
            }
        });
        panel_absen.add(btn_absen);
        top_panel.add(panel_absen);

        // Tombol Tambah User
        panel_add_user = new JPanel();
        panel_add_user.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btn_add_user = new JButton();
        btn_add_user.setText("Tambah");
        panel_add_user.add(btn_add_user);
        top_panel.add(panel_add_user);

        table_panel = new JPanel();
        table_panel.setLayout(new BorderLayout());
        panel.add(table_panel, BorderLayout.CENTER);

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

        scroll_panel = new JScrollPane();
        scroll_panel.setViewportView(tbl_list_user);
        if (tbl_list_user.getColumnModel().getColumnCount() > 0) {
            tbl_list_user.getColumnModel().getColumn(3).setPreferredWidth(50);
            tbl_list_user.getColumnModel().getColumn(3).setMaxWidth(50);
            tbl_list_user.getColumnModel().getColumn(4).setPreferredWidth(50);
            tbl_list_user.getColumnModel().getColumn(4).setMaxWidth(50);
        }
        table_panel.add(scroll_panel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void btn_absenActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

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
                            + "         and a.absen_date = '2020-05-24') > 0 then 'Ya'"
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

    public static void main(String args[]) {
        new AdminDashboard();
    }
}
