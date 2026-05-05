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
        setSize(550, 350); // Agak digedein dikit biar lega
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);
        
        // 2. Setup Warna Tema (Dark Blue & Accent Coral)
        Color bgUtama = new Color(30, 61, 89);        // Biru Gelap / Navy
        Color warnaTeks = Color.WHITE;                // Teks Putih
        Color warnaTombol = new Color(255, 110, 64);  // Coral / Orange
        Color warnaTombolHover = new Color(255, 138, 101); // Orange terang pas di-hover

        // 3. Panel Utama yang ngebungkus semuanya
        JPanel panelUtama = new JPanel(new BorderLayout(10, 20));
        panelUtama.setBackground(bgUtama);
        panelUtama.setBorder(new EmptyBorder(30, 40, 30, 40)); // Margin keliling (Atas, Kiri, Bawah, Kanan)

        // --- Panel Header (Judul) ---
        JLabel lblJudul = new JLabel("LOGIN HRD PT.Inti Sejahatera", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblJudul.setForeground(warnaTeks);
        panelUtama.add(lblJudul, BorderLayout.NORTH);

        // --- Panel Tengah (Input Form) ---
        // Pake GridLayout 2 Baris, 2 Kolom
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 20));
        panelForm.setBackground(bgUtama); // Samain warna background-nya

        // Label Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUsername.setForeground(warnaTeks);
        
        // TextField Username
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding text dalam
        ));

        // Label Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPassword.setForeground(warnaTeks);
        
        // TextField Password
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Masukin ke Panel Form
        panelForm.add(lblUsername);
        panelForm.add(txtUsername);
        panelForm.add(lblPassword);
        panelForm.add(txtPassword);

        panelUtama.add(panelForm, BorderLayout.CENTER);

        // --- Panel Bawah (Tombol) ---
        btnLogin = new JButton("MASUK SEKARANG");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(warnaTombol);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Kursor jadi bentuk tangan pas di tombol
        
        // Bikin ukuran tombol lebih tinggi
        btnLogin.setPreferredSize(new Dimension(100, 45));
        
        // Ilangin border default Java yang jelek
        btnLogin.setBorder(BorderFactory.createEmptyBorder());

        // Efek Hover (Warna berubah pas mouse ngelewatin tombol)
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(warnaTombolHover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(warnaTombol);
            }
        });

        panelUtama.add(btnLogin, BorderLayout.SOUTH);

        // Tempel Panel Utama ke Frame (Nge-replace warna default background bawaan frame)
        setContentPane(panelUtama);

        // --- Action Listener Tombol ---
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password harus diisi masbro!", "Gagal", JOptionPane.WARNING_MESSAGE);
            } else {
                // Logic Backend API di sini nanti
                JOptionPane.showMessageDialog(this, "Berhasil masuk!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                new Karyawan().setVisible(true);
                this.dispose();
            }
        });
    }
}

