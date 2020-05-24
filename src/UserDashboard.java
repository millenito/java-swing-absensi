import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
public class UserDashboard extends JFrame {

    private JButton absen;
    private JLabel absenYet;
    private JLabel greet;
    private JPanel panel;
    private JPanel panelAtas;
    private JPanel panelBawah;
    private JPanel panelTengah;

    private String user_id, user_type, user_name;
    private String date_now, time_now, absen_time;
    private boolean absen_status;

    public UserDashboard(String user_id, String user_type, String user_name) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.user_name = user_name;

        DateTimeFormatter datef = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        date_now = datef.format(now);
        time_now = timef.format(now);

        initGUI();
    }

    private void initGUI(){
        setTitle("Absen");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setSize(new Dimension(800, 600));

        // Panel utama
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        add(panel, BorderLayout.CENTER);

        // Panel atas
        panelAtas = new JPanel();
        panelAtas.setLayout(new GridBagLayout());
        greet = new JLabel();
        greet.setFont(new Font("Dialog", 1, 36));
        greet.setText("Selamat Datang, " + user_name + "!");
        panelAtas.add(greet, new GridBagConstraints());
        panel.add(panelAtas);

        // Panel Tengah
        panelTengah = new JPanel();
        panelTengah.setLayout(new GridBagLayout());
        absenYet = new JLabel();
        absenYet.setFont(new Font("Dialog", 1, 18));
        checkAbsen();
        if (!absen_status){
            absenYet.setText("Anda belum absen, silahkan absen terlebih dahulu!");
        }else{
            absenYet.setText("Anda sudah absen pada jam " + absen_time + ", Terimakasih!");
        }
        panelTengah.add(absenYet, new GridBagConstraints());
        panel.add(panelTengah);

        panelBawah = new JPanel();
        panelBawah.setLayout(new BorderLayout());
        if(!absen_status){
            absen = new JButton();
            absen.setText("Absen");
            absen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(insertAbsen()){
                        checkAbsen();
                        absen.setVisible(false);
                        absenYet.setText("Anda sudah absen pada jam " + absen_time + ", Terimakasih!");
                    }
                }
            });
            panelBawah.add(absen, BorderLayout.CENTER);
        }
        panel.add(panelBawah);

        pack();
        setVisible(true);
    }

    private boolean insertAbsen(){
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

    private void checkAbsen() {
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
    }
//    public static void main(String args[]) {
//        new UserDashboard();
//    }
}
