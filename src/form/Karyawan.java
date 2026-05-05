/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 *
 * @author fadhil abi
 */
public class Karyawan extends JFrame {
   // Deklarasi Komponen CRUD
    private JTextField txtId, txtNama, txtPosisi;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable tabelKaryawan;
    private DefaultTableModel tableModel;

    // Palette Warna Tema
    private Color bgSidebar = new Color(30, 61, 89);        // Navy (Sidebar & Pop-up Header)
    private Color bgContent = new Color(245, 247, 250);     // Light Gray/White (Konten Utama)
    private Color btnPrimary = new Color(255, 110, 64);     // Coral / Orange
    private Color btnDanger = new Color(220, 53, 69);       // Merah
    private Color btnSecondary = new Color(108, 117, 125);  // Abu-abu
    private Color textDark = new Color(50, 50, 50);         // Teks gelap

    public Karyawan() {
        // 1. Pengaturan Dasar Frame
        setTitle("Dashboard HRD - Kelola Karyawan");
        setSize(950, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panelUtama = new JPanel(new BorderLayout());
        
        // ==========================================
        // 1. MEMBUAT SIDEBAR (Posisi Kiri)
        // ==========================================
        JPanel panelSidebar = new JPanel(new BorderLayout());
        panelSidebar.setBackground(bgSidebar);
        panelSidebar.setPreferredSize(new Dimension(220, 0)); 

        JLabel lblLogo = new JLabel("HRD SYSTEM", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBorder(new EmptyBorder(30, 10, 30, 10)); 
        panelSidebar.add(lblLogo, BorderLayout.NORTH);

        JPanel panelMenu = new JPanel(new GridLayout(5, 1, 0, 5)); 
        panelMenu.setBackground(bgSidebar);
        panelMenu.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton navDashboard = createNavButton("   Dashboard");
        JButton navKaryawan = createNavButton("   Data Karyawan");
        navKaryawan.setBackground(new Color(40, 80, 115)); // Menu aktif
        
        panelMenu.add(navDashboard);
        panelMenu.add(navKaryawan);
        panelSidebar.add(panelMenu, BorderLayout.CENTER);

        JButton btnLogout = createNavButton("   Logout");
        btnLogout.setForeground(new Color(255, 150, 150)); 
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 80, 110)), 
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panelSidebar.add(btnLogout, BorderLayout.SOUTH);

        panelUtama.add(panelSidebar, BorderLayout.WEST);

        // ==========================================
        // 2. MEMBUAT KONTEN UTAMA (Posisi Tengah)
        // ==========================================
        JPanel panelContent = new JPanel(new BorderLayout(15, 15));
        panelContent.setBackground(bgContent);
        panelContent.setBorder(new EmptyBorder(25, 30, 25, 30)); 

        // --- BAGIAN ATAS KONTEN (Header & Input) ---
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setBackground(bgContent);
        
        JLabel lblJudul = new JLabel("Kelola Data Karyawan");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblJudul.setForeground(textDark);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelAtas.add(lblJudul, BorderLayout.NORTH);

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBackground(bgContent);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 15);

        gbc.gridx = 0; gbc.gridy = 0; panelInput.add(createLabel("ID Karyawan", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtId = createTextField(fontInput); txtId.setEditable(false); txtId.setBackground(new Color(220, 220, 220)); panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelInput.add(createLabel("Nama Lengkap", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtNama = createTextField(fontInput); panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelInput.add(createLabel("Posisi / Jabatan", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtPosisi = createTextField(fontInput); panelInput.add(txtPosisi, gbc);

        panelAtas.add(panelInput, BorderLayout.CENTER);

        // --- TOMBOL AKSI ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10)); 
        panelTombol.setBackground(bgContent);
        panelTombol.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        btnSimpan = createActionButton("Simpan", btnPrimary);
        btnUbah = createActionButton("Ubah", btnPrimary);
        btnHapus = createActionButton("Hapus", btnDanger);
        btnClear = createActionButton("Clear", btnSecondary);

        panelTombol.add(btnSimpan); panelTombol.add(btnUbah); panelTombol.add(btnHapus); panelTombol.add(btnClear);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);

        panelContent.add(panelAtas, BorderLayout.NORTH);

        // --- BAGIAN BAWAH KONTEN (Tabel) ---
        String[] kolom = {"ID", "Nama Karyawan", "Posisi"};
        tableModel = new DefaultTableModel(kolom, 0);
        tabelKaryawan = new JTable(tableModel);
        
        tabelKaryawan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelKaryawan.setRowHeight(35); 
        tabelKaryawan.setSelectionBackground(new Color(255, 220, 200)); 
        tabelKaryawan.setShowVerticalLines(false);
        tabelKaryawan.setGridColor(new Color(220, 220, 220));

        JTableHeader headerTabel = tabelKaryawan.getTableHeader();
        headerTabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        headerTabel.setBackground(bgSidebar); 
        headerTabel.setForeground(Color.WHITE);
        headerTabel.setPreferredSize(new Dimension(100, 40));
        
        JScrollPane scrollPane = new JScrollPane(tabelKaryawan);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); 

        panelContent.add(scrollPane, BorderLayout.CENTER);

        panelUtama.add(panelContent, BorderLayout.CENTER);
        setContentPane(panelUtama);

        // --- DUMMY DATA ---
        tableModel.addRow(new Object[]{"1", "Budi Santoso", "HR Manager"});
        tableModel.addRow(new Object[]{"2", "Siti Aminah", "Staff IT"});

        // ==========================================
        // ACTION LISTENERS
        // ==========================================
        
        // Listener Tabel
        tabelKaryawan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int baris = tabelKaryawan.getSelectedRow();
                if (baris != -1) {
                    txtId.setText(tableModel.getValueAt(baris, 0).toString());
                    txtNama.setText(tableModel.getValueAt(baris, 1).toString());
                    txtPosisi.setText(tableModel.getValueAt(baris, 2).toString());
                }
            }
        });

        // Tombol Clear
        btnClear.addActionListener(e -> clearForm());

        // Tombol Simpan
        btnSimpan.addActionListener(e -> {
            if (txtNama.getText().isEmpty() || txtPosisi.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan Posisi harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Simulasi: Menyimpan ke Supabase...");
                clearForm();
            }
        });

        // Tombol Ubah
        btnUbah.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel dulu yang mau diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Simulasi: Mengubah data di Supabase...");
                clearForm();
            }
        });

        // Tombol Hapus (Pake Custom Pop-up)
        btnHapus.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel dulu yang mau dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                boolean yakinHapus = showCustomConfirm("Konfirmasi Hapus", "Yakin mau menghapus karyawan ini?");
                if (yakinHapus) {
                    JOptionPane.showMessageDialog(this, "Simulasi: Menghapus data dari Supabase...");
                    clearForm();
                }
            }
        });

        // Tombol Logout (Pake Custom Pop-up)
        btnLogout.addActionListener(e -> {
            boolean yakinKeluar = showCustomConfirm("Konfirmasi Keluar", "Apakah Anda yakin ingin keluar dari sistem?");
            if (yakinKeluar) {
                new LoginForm().setVisible(true);
                this.dispose(); 
            }
        });
    }

    // ==========================================
    // METHOD HELPER UI AESTHETIC
    // ==========================================
    
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgSidebar);
        btn.setHorizontalAlignment(SwingConstants.LEFT); 
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(!text.contains("Data Karyawan")) btn.setBackground(new Color(50, 80, 110)); 
            }
            public void mouseExited(MouseEvent e) {
                if(!text.contains("Data Karyawan")) btn.setBackground(bgSidebar);
            }
        });
        return btn;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgColor.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(textDark);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField tf = new JTextField(25); 
        tf.setFont(font);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return tf;
    }

    private void clearForm() {
        txtId.setText(""); txtNama.setText(""); txtPosisi.setText("");
        tabelKaryawan.clearSelection();
    }

    // --- METHOD CUSTOM POP-UP AESTHETIC ---
    private boolean showCustomConfirm(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true); 
        dialog.setSize(380, 170);
        dialog.setLocationRelativeTo(this);
        
        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBorder(BorderFactory.createLineBorder(bgSidebar, 2)); 
        panelUtama.setBackground(Color.WHITE);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(bgSidebar);
        panelHeader.setPreferredSize(new Dimension(380, 40));
        
        JLabel lblTitle = new JLabel("   " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle, BorderLayout.CENTER);
        panelUtama.add(panelHeader, BorderLayout.NORTH);

        JLabel lblMessage = new JLabel(message, SwingConstants.CENTER);
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMessage.setForeground(textDark);
        panelUtama.add(lblMessage, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelTombol.setBackground(Color.WHITE);
        
        JButton btnYes = createActionButton("Ya", btnDanger); 
        JButton btnNo = createActionButton("Batal", btnSecondary);    

        final boolean[] jawaban = {false};

        btnYes.addActionListener(e -> { jawaban[0] = true; dialog.dispose(); });
        btnNo.addActionListener(e -> { jawaban[0] = false; dialog.dispose(); });

        panelTombol.add(btnYes);
        panelTombol.add(btnNo);
        panelUtama.add(panelTombol, BorderLayout.SOUTH);

        dialog.setContentPane(panelUtama);
        dialog.setVisible(true); 

        return jawaban[0];
    }
}
