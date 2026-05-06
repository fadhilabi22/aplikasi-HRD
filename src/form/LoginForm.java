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
/**
 *
 * @author fadhil abi
 */
public class LoginForm extends JFrame{
    // Deklarasi Komponen
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        // 1. Pengaturan Dasar Frame
        setTitle("Login - Aplikasi HRD PT.Inti Sejahtera");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // 2. Setup Warna Tema
        Color bgUtama = new Color(30, 61, 89);
        Color warnaTeks = Color.WHITE;
        Color warnaTombol = new Color(255, 110, 64);
        Color warnaTombolHover = new Color(255, 138, 101);

        // 3. Panel Utama
        JPanel panelUtama = new JPanel(new BorderLayout(10, 20));
        panelUtama.setBackground(bgUtama);
        panelUtama.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- Header ---
        JLabel lblJudul = new JLabel("LOGIN HRD PT.Inti Sejahtera", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblJudul.setForeground(warnaTeks);
        panelUtama.add(lblJudul, BorderLayout.NORTH);

        // --- Input Form ---
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 20));
        panelForm.setBackground(bgUtama);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUsername.setForeground(warnaTeks);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPassword.setForeground(warnaTeks);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        panelForm.add(lblUsername); panelForm.add(txtUsername);
        panelForm.add(lblPassword); panelForm.add(txtPassword);
        panelUtama.add(panelForm, BorderLayout.CENTER);

        // --- Tombol ---
        btnLogin = new JButton("MASUK SEKARANG");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(warnaTombol);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(100, 45));
        btnLogin.setBorder(BorderFactory.createEmptyBorder());

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(warnaTombolHover); }
            @Override
            public void mouseExited(MouseEvent e) { btnLogin.setBackground(warnaTombol); }
        });

        panelUtama.add(btnLogin, BorderLayout.SOUTH);
        setContentPane(panelUtama);

        // --- LOGIC BACKEND NYA DI SINI MASBRO ---
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password harus diisi masbro!", "Gagal", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    // Kita minta database nyari baris yang username dan password-nya pas (eq)
                    String query = "users?username=eq." + username + "&password=eq." + password + "&select=*";
                    String response = DatabaseConnection.fetchData(query);

                    // Kalau Supabase balikin "[]" berarti data nggak ada
                    if (!response.equals("[]")) {
                        showSuccessAlert("Selamat! Berhasil Masuk Sistem");
                        
                        // Tutup login, buka dashboard
                        new Karyawan().setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Masalah koneksi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void showSuccessAlert(String message) {
        JDialog dialog = new JDialog(this, "Sukses", true);
        dialog.setUndecorated(true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);

        Color bgAlert = Color.WHITE;
        Color greenSuccess = new Color(46, 204, 113);
        Color textDark = new Color(50, 50, 50);

        JPanel panelUtamaAlert = new JPanel(new BorderLayout());
        panelUtamaAlert.setBackground(bgAlert);
        panelUtamaAlert.setBorder(BorderFactory.createLineBorder(greenSuccess, 2));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(greenSuccess);
        panelHeader.setPreferredSize(new Dimension(350, 35));
        JLabel lblHeader = new JLabel("  SUKSES", SwingConstants.LEFT);
        lblHeader.setForeground(Color.WHITE);
        panelHeader.add(lblHeader, BorderLayout.CENTER);

        JLabel lblMessage = new JLabel(message, SwingConstants.CENTER);
        lblMessage.setForeground(textDark);

        JButton btnOk = new JButton("Lanjutkan");
        btnOk.setBackground(greenSuccess);
        btnOk.setForeground(Color.WHITE);
        btnOk.addActionListener(al -> dialog.dispose());

        panelUtamaAlert.add(panelHeader, BorderLayout.NORTH);
        panelUtamaAlert.add(lblMessage, BorderLayout.CENTER);
        panelUtamaAlert.add(btnOk, BorderLayout.SOUTH);

        dialog.setContentPane(panelUtamaAlert);
        dialog.setVisible(true);
    }
}

