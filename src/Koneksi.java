import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi { // koneksi ke database
    private static Connection mysqlkonek;

    public static Connection koneksiDB() throws SQLException {
        if (mysqlkonek == null) {
            try {
                String DB = "jdbc:mysql://localhost:3306/AbsensiKaryawan"; // Nama Database
                String user = "root"; // User database
                String pass = ""; // Password database
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                mysqlkonek = (Connection) DriverManager.getConnection(DB, user, pass);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e, "Gagal konek ke mysql", JOptionPane.ERROR_MESSAGE);
            }
        }
        return mysqlkonek;
    }
}