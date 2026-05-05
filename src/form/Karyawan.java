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
   // Komponen Navigasi
    private JPanel panelContent, panelCRUD, panelDashboard;
    private CardLayout cardLayout;

    // Deklarasi Komponen CRUD
    private JTextField txtId, txtNama, txtPosisi;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable tabelKaryawan;
    private DefaultTableModel tableModel;

    // Palette Warna Tema
    private Color bgSidebar = new Color(30, 61, 89);        // Navy
    private Color bgContent = new Color(245, 247, 250);     // Light Gray
    private Color btnPrimary = new Color(255, 110, 64);     // Coral
    private Color btnDanger = new Color(220, 53, 69);       // Merah
    private Color btnSecondary = new Color(108, 117, 125);  // Abu-abu
    private Color textDark = new Color(50, 50, 50);         // Teks gelap

    public Karyawan() {
        // 1. Pengaturan Dasar Frame
        setTitle("Dashboard HRD - PT. Inti Sejahtera");
        setSize(1000, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panelUtama = new JPanel(new BorderLayout());
        
        // 2. SETUP SIDEBAR (Kiri)
        initSidebar(panelUtama);

        // 3. SETUP KONTEN UTAMA DENGAN CARDLAYOUT (Tengah)
        cardLayout = new CardLayout();
        panelContent = new JPanel(cardLayout);
        
        initDashboardPage(); // Halaman Dashboard
        initCRUDPage();      // Halaman Data Karyawan
        
        panelContent.add(panelDashboard, "MENU_DASHBOARD");
        panelContent.add(panelCRUD, "MENU_KARYAWAN");
        
        panelUtama.add(panelContent, BorderLayout.CENTER);
        setContentPane(panelUtama);
        
        // Tampilan Awal
        cardLayout.show(panelContent, "MENU_DASHBOARD");
    }

    // ==========================================
    // HALAMAN DASHBOARD (INFO CARDS)
    // ==========================================
    private void initDashboardPage() {
        panelDashboard = new JPanel(new BorderLayout());
        panelDashboard.setBackground(bgContent);
        panelDashboard.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JLabel lblTitle = new JLabel("Ringkasan Perusahaan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(textDark);
        panelDashboard.add(lblTitle, BorderLayout.NORTH);

        // Grid Cards
        JPanel panelCards = new JPanel(new GridLayout(1, 3, 25, 0));
        panelCards.setBackground(bgContent);
        panelCards.setBorder(new EmptyBorder(30, 0, 0, 0));

        panelCards.add(createStatCard("Total Karyawan", "128", new Color(52, 152, 219)));
        panelCards.add(createStatCard("Total Divisi", "8", new Color(46, 204, 113)));
        panelCards.add(createStatCard("Hadir Hari Ini", "95%", new Color(155, 89, 182)));

        panelDashboard.add(panelCards, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String val, Color col) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel t = new JLabel(title); t.setFont(new Font("Segoe UI", Font.BOLD, 14)); t.setForeground(Color.GRAY);
        JLabel v = new JLabel(val); v.setFont(new Font("Segoe UI", Font.BOLD, 42)); v.setForeground(col);

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    // ==========================================
    // HALAMAN CRUD (DATA KARYAWAN)
    // ==========================================
    private void initCRUDPage() {
        panelCRUD = new JPanel(new BorderLayout(15, 15));
        panelCRUD.setBackground(bgContent);
        panelCRUD.setBorder(new EmptyBorder(25, 30, 25, 30));

        // --- BAGIAN ATAS (Header & Input) ---
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
        panelCRUD.add(panelAtas, BorderLayout.NORTH);

        // --- TABEL ---
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
        panelCRUD.add(scrollPane, BorderLayout.CENTER);

        // Dummy Data
        tableModel.addRow(new Object[]{"1", "Budi Santoso", "HR Manager"});
        tableModel.addRow(new Object[]{"2", "Siti Aminah", "Staff IT"});

        // Listeners
        tabelKaryawan.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int baris = tabelKaryawan.getSelectedRow();
                if (baris != -1) {
                    txtId.setText(tableModel.getValueAt(baris, 0).toString());
                    txtNama.setText(tableModel.getValueAt(baris, 1).toString());
                    txtPosisi.setText(tableModel.getValueAt(baris, 2).toString());
                }
            }
        });
        btnClear.addActionListener(e -> clearForm());
        btnSimpan.addActionListener(e -> {
            if (txtNama.getText().isEmpty()) JOptionPane.showMessageDialog(this, "Isi nama dulu masbro!");
            else JOptionPane.showMessageDialog(this, "Simulasi: Simpan ke Supabase...");
        });
    }

    // ==========================================
    // SIDEBAR SETUP
    // ==========================================
    private void initSidebar(JPanel mainPanel) {
        JPanel panelSidebar = new JPanel(new BorderLayout());
        panelSidebar.setBackground(bgSidebar);
        panelSidebar.setPreferredSize(new Dimension(220, 0)); 

        JLabel lblLogo = new JLabel("HRD SYSTEM", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBorder(new EmptyBorder(30, 10, 30, 10)); 
        panelSidebar.add(lblLogo, BorderLayout.NORTH);

        JPanel panelMenu = new JPanel(new GridLayout(10, 1, 0, 5)); 
        panelMenu.setBackground(bgSidebar);
        panelMenu.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton navDashboard = createNavButton("   Dashboard");
        JButton navKaryawan = createNavButton("   Data Karyawan");
        JButton btnLogout = createNavButton("   Logout");

        // Action Navigasi
        navDashboard.addActionListener(e -> cardLayout.show(panelContent, "MENU_DASHBOARD"));
        navKaryawan.addActionListener(e -> cardLayout.show(panelContent, "MENU_KARYAWAN"));
        btnLogout.addActionListener(e -> {
            if(showCustomConfirm("Logout", "Yakin mau keluar masbro?")) {
                new LoginForm().setVisible(true);
                this.dispose();
            }
        });

        panelMenu.add(navDashboard);
        panelMenu.add(navKaryawan);
        panelSidebar.add(panelMenu, BorderLayout.CENTER);
        
        btnLogout.setForeground(new Color(255, 150, 150));
        panelSidebar.add(btnLogout, BorderLayout.SOUTH);

        mainPanel.add(panelSidebar, BorderLayout.WEST);
    }

    // ==========================================
    // HELPERS (UI Aesthetics)
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
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(50, 80, 110)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgSidebar); }
        });
        return btn;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        return btn;
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text); label.setFont(font); label.setForeground(textDark);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField tf = new JTextField(25); tf.setFont(font);
        tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1), BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return tf;
    }

    private void clearForm() {
        txtId.setText(""); txtNama.setText(""); txtPosisi.setText("");
        tabelKaryawan.clearSelection();
    }

    private boolean showCustomConfirm(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true); dialog.setSize(380, 170); dialog.setLocationRelativeTo(this);
        JPanel p = new JPanel(new BorderLayout()); p.setBorder(BorderFactory.createLineBorder(bgSidebar, 2)); p.setBackground(Color.WHITE);
        JLabel m = new JLabel(message, SwingConstants.CENTER); m.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JPanel b = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15)); b.setBackground(Color.WHITE);
        JButton y = createActionButton("Ya", btnDanger); JButton n = createActionButton("Batal", btnSecondary);
        final boolean[] res = {false};
        y.addActionListener(e -> { res[0] = true; dialog.dispose(); });
        n.addActionListener(e -> { res[0] = false; dialog.dispose(); });
        b.add(y); b.add(n); p.add(m, BorderLayout.CENTER); p.add(b, BorderLayout.SOUTH);
        dialog.setContentPane(p); dialog.setVisible(true);
        return res[0];
    }
}
