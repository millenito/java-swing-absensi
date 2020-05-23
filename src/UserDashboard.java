import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserDashboard extends JFrame implements ActionListener {

    private JButton absen;
    private JLabel absenYet;
    private JLabel greet;
    private JPanel panel;
    private JPanel panelAtas;
    private JPanel panelBawah;
    private JPanel panelTengah;

    private String user_id, user_type, user_name;

    public UserDashboard(String user_id, String user_type, String user_name) {
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
        greet.setText("Selamat Datang, USER!");
        panelAtas.add(greet, new GridBagConstraints());
        panel.add(panelAtas);

        // Panel Tengah
        panelTengah = new JPanel();
        panelTengah.setLayout(new GridBagLayout());
        absenYet = new JLabel();
        absenYet.setFont(new Font("Dialog", 1, 18)); // NOI18N
        absenYet.setText("Anda belum absen, silahkan absen terlebih dahulu!");
        panelTengah.add(absenYet, new GridBagConstraints());

        panelBawah = new JPanel();
        panel.add(panelTengah);
        panelBawah.setLayout(new BorderLayout());
        absen = new JButton();
        absen.setText("Absen");
        absen.addActionListener(this);
        panelBawah.add(absen, BorderLayout.CENTER);

        panel.add(panelBawah);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }

//    public static void main(String args[]) {
//        new UserDashboard();
//    }
}
