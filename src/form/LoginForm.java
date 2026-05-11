/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import helper.DatabaseConnection;
import javax.swing.border.MatteBorder;
/**
 *
 * @author fadhil abi
 */
public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        setTitle("Login - HRD System");
        setSize(750, 450); // Dibikin agak lebar buat split-screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- PALETTE WARNA ---
        Color bgNavy = new Color(30, 61, 89);          
        Color bgWhite = Color.WHITE;
        Color btnCoral = new Color(255, 110, 64);      
        Color btnCoralHover = new Color(255, 138, 101);
        Color textDark = new Color(50, 50, 50);
        Color lineGray = new Color(200, 200, 200);

        // Panel Utama pake GridLayout (1 baris, 2 kolom)
        JPanel panelUtama = new JPanel(new GridLayout(1, 2));

        // ==========================================
        // SISI KIRI (BRANDING / LOGO)
        // ==========================================
        JPanel panelKiri = new JPanel(new GridBagLayout());
        panelKiri.setBackground(bgNavy);
        
        GridBagConstraints gbcKiri = new GridBagConstraints();
        gbcKiri.gridx = 0; gbcKiri.gridy = 0;
        
        JLabel lblWelcome = new JLabel("Welcome to");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblWelcome.setForeground(new Color(200, 210, 220));
        panelKiri.add(lblWelcome, gbcKiri);

        gbcKiri.gridy = 1;
        gbcKiri.insets = new Insets(5, 0, 10, 0);
        JLabel lblLogo = new JLabel("HRD SYSTEM");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblLogo.setForeground(Color.WHITE);
        panelKiri.add(lblLogo, gbcKiri);

        gbcKiri.gridy = 2;
        gbcKiri.insets = new Insets(0, 0, 0, 0);
        JLabel lblCompany = new JLabel("PT. Inti Sejahtera");
        lblCompany.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCompany.setForeground(btnCoral); // Pake warna coral buat aksen
        panelKiri.add(lblCompany, gbcKiri);

        panelUtama.add(panelKiri);

        // ==========================================
        // SISI KANAN (FORM LOGIN PUTIH BERSIH)
        // ==========================================
        JPanel panelKanan = new JPanel(new GridBagLayout());
        panelKanan.setBackground(bgWhite);
        GridBagConstraints gbcKanan = new GridBagConstraints();
        gbcKanan.fill = GridBagConstraints.HORIZONTAL;
        gbcKanan.insets = new Insets(10, 40, 10, 40); // Padding pinggir kiri kanan
        gbcKanan.gridx = 0;

        // Judul Form
        gbcKanan.gridy = 0;
        gbcKanan.insets = new Insets(0, 40, 40, 40);
        JLabel lblMasuk = new JLabel("Sign In");
        lblMasuk.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblMasuk.setForeground(textDark);
        panelKanan.add(lblMasuk, gbcKanan);

        // Input Username (Gaya Material Design)
        gbcKanan.gridy = 1;
        gbcKanan.insets = new Insets(0, 40, 5, 40);
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(Color.GRAY);
        panelKanan.add(lblUser, gbcKanan);

        gbcKanan.gridy = 2;
        gbcKanan.insets = new Insets(0, 40, 20, 40);
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.setPreferredSize(new Dimension(250, 30));
        // Trik garis bawah doang
        txtUsername.setBorder(new MatteBorder(0, 0, 2, 0, lineGray)); 
        txtUsername.setBackground(bgWhite);
        panelKanan.add(txtUsername, gbcKanan);

        // Input Password
        gbcKanan.gridy = 3;
        gbcKanan.insets = new Insets(0, 40, 5, 40);
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(Color.GRAY);
        panelKanan.add(lblPass, gbcKanan);

        gbcKanan.gridy = 4;
        gbcKanan.insets = new Insets(0, 40, 40, 40);
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setPreferredSize(new Dimension(250, 30));
        txtPassword.setBorder(new MatteBorder(0, 0, 2, 0, lineGray));
        txtPassword.setBackground(bgWhite);
        panelKanan.add(txtPassword, gbcKanan);

        // Tombol Login
        gbcKanan.gridy = 5;
        gbcKanan.insets = new Insets(0, 40, 0, 40);
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(btnCoral);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(250, 40));
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(btnCoralHover); }
            public void mouseExited(MouseEvent e) { btnLogin.setBackground(btnCoral); }
        });
        panelKanan.add(btnLogin, gbcKanan);

        panelUtama.add(panelKanan);
        setContentPane(panelUtama);

        // ==========================================
        // LOGIC LOGIN DENGAN THREAD
        // ==========================================
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi dulu Username sama Passwordnya masbro!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            btnLogin.setText("MEMPROSES...");
            btnLogin.setEnabled(false);
                
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    String query = "users?username=eq." + username + "&password=eq." + password + "&select=*";
                    String response = DatabaseConnection.fetchData(query);

                    if (response != null && !response.equals("[]")) {
                        new Karyawan().setVisible(true);
                        dispose();
                        showSuccessAlert("Berhasil Masuk Sistem");
                    } else {
                        JOptionPane.showMessageDialog(LoginForm.this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }
                @Override
                protected void done() {
                    btnLogin.setText("LOGIN");
                    btnLogin.setEnabled(true);
                }
            };
            worker.execute();
        });
    }

    // --- POP-UP SUCCESS ---
    private void showSuccessAlert(String message) {
        JDialog dialog = new JDialog((Frame)null, "Sukses", false); 
        dialog.setUndecorated(true); dialog.setSize(350, 150); dialog.setLocationRelativeTo(null);
        Color greenSuccess = new Color(46, 204, 113);
        
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(Color.WHITE); p.setBorder(BorderFactory.createLineBorder(greenSuccess, 2));
        JPanel pH = new JPanel(new BorderLayout()); pH.setBackground(greenSuccess); pH.setPreferredSize(new Dimension(350, 35));
        JLabel lH = new JLabel("  SUKSES", SwingConstants.LEFT); lH.setFont(new Font("Segoe UI", Font.BOLD, 14)); lH.setForeground(Color.WHITE);
        pH.add(lH, BorderLayout.CENTER);
        JLabel lM = new JLabel(message, SwingConstants.CENTER); lM.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        JPanel pB = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15)); pB.setBackground(Color.WHITE);
        JButton bO = new JButton("Lanjutkan"); bO.setBackground(greenSuccess); bO.setForeground(Color.WHITE);
        bO.setFocusPainted(false); bO.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25)); bO.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bO.addActionListener(al -> dialog.dispose()); pB.add(bO);

        p.add(pH, BorderLayout.NORTH); p.add(lM, BorderLayout.CENTER); p.add(pB, BorderLayout.SOUTH);
        dialog.setContentPane(p); dialog.setVisible(true);
    }
}

