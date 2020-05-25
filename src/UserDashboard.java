import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class UserDashboard extends Dashboard {

    private JButton absen;
    private JButton sign_out;
    private JLabel absen_info;
    private JLabel greet;
    private JPanel panel;
    private JPanel panel_atas;
    private JPanel panel_tengah;
    private JPanel panel_bawah;

    UserDashboard(String user_id, String user_type, String user_name) {
        super(user_id, user_type, user_name);
        initGUI();
    }

    public void initGUI(){
        setTitle("Absen");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setSize(new Dimension(800, 600));

        // Panel utama
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        add(panel, BorderLayout.CENTER);

        // Panel atas
        GridBagConstraints gbc;
        panel_atas = new JPanel();
        panel_atas.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(6, 74, 0, 138);
        sign_out = new JButton();
        sign_out.setText("Sign out");
        sign_out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                signout(evt);
            }
        });
        panel_atas.add(sign_out, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(12, 277, 53, 0);
        greet = new JLabel();
        greet.setFont(new Font("Dialog", 1, 36));
        greet.setHorizontalAlignment(SwingConstants.LEFT);
        greet.setText("Selamat Datang, " + user_name + "!");
        panel_atas.add(greet, gbc);
        panel.add(panel_atas);

        // Panel Tengah
        panel_tengah = new JPanel();
        panel_tengah.setLayout(new GridBagLayout());
        absen_info = new JLabel();
        absen_info.setFont(new Font("Dialog", 1, 18));
        if (!checkAbsen()){
            absen_info.setText("Anda belum absen, silahkan absen terlebih dahulu!");
        }else{
            absen_info.setText("Anda sudah absen pada jam " + absen_time + ", Terimakasih!");
        }
        panel_tengah.add(absen_info, new GridBagConstraints());
        panel.add(panel_tengah);

        panel_bawah = new JPanel();
        panel_bawah.setLayout(new BorderLayout());
        System.out.println(absen_status);
        if(!absen_status){
            absen = new JButton();
            absen.setText("Absen");
            absen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(insertAbsen()){
                        checkAbsen();
                        absen.setVisible(false);
                        absen_info.setText("Anda sudah absen pada jam " + absen_time + ", Terimakasih!");
                    }
                }
            });
            panel_bawah.add(absen, BorderLayout.CENTER);
        }
        panel.add(panel_bawah);

        pack();
        setVisible(true);
    }
}
