import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class Dashboard extends JFrame {

    protected String user_id, user_type, user_name;
    protected String date_now, time_now, absen_time;
    protected boolean absen_status;

    Dashboard (String user_id, String user_type, String user_name){
        this.user_id = user_id;
        this.user_type = user_type;
        this.user_name = user_name;

        DateTimeFormatter datef = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        date_now = datef.format(now);
        time_now = timef.format(now);
    }

    abstract void initGUI();

    protected void signout(ActionEvent evt){
        new Login();
        getContentPane().setVisible(false);
        dispose();
    }

    protected boolean insertAbsen(){
        try {
            Connection conn = Koneksi.koneksiDB();
            Statement stm = conn.createStatement();
            String query = "insert into absen_harian values( "
                    + "'" + user_id + "',"
                    + "'" + date_now + "',"
                    + "'" + time_now + "')";
            stm.executeUpdate(query);
            return true;
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Error!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }

        return false;
    }

    protected boolean checkAbsen() {
        try {
            Connection conn = Koneksi.koneksiDB();
            Statement stm = conn.createStatement();

            String query = "select absen_time from absen_harian where user_id = '" + user_id + "' and absen_date = '" + date_now + "'";
            ResultSet sql = stm.executeQuery(query);
            if (sql.next()) {
                absen_time = sql.getString("absen_time");
                absen_status = true;
            } else {
                absen_status = false;
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e, "Error!", JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
        return absen_status;
    }
}
