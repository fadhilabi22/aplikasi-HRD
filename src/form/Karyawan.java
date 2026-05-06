package form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.io.OutputStream;
import java.util.HashSet; // Import HashSet buat logic hitung divisi
import helper.DatabaseConnection; 
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Karyawan extends JFrame {
    private JPanel panelContent, panelCRUD, panelDashboard;
    private CardLayout cardLayout;
    private JTextField txtId, txtNama, txtPosisi;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable tabelKaryawan;
    private DefaultTableModel tableModel;
    
    // Deklarasi label angka dashboard
    private JLabel lblTotalKaryawan, lblTotalDivisi;

    // --- PALETTE WARNA AESTHETIC ---
    private Color bgSidebar = new Color(30, 61, 89);        
    private Color bgContent = new Color(245, 247, 250);     
    private Color btnPrimary = new Color(255, 110, 64);     
    private Color btnDanger = new Color(220, 53, 69);       
    private Color btnSecondary = new Color(108, 117, 125);  
    private Color textDark = new Color(50, 50, 50);         

    public Karyawan() {
        setTitle("Dashboard HRD - PT. Inti Sejahtera");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelUtama = new JPanel(new BorderLayout());
        
        initSidebar(panelUtama);
        cardLayout = new CardLayout();
        panelContent = new JPanel(cardLayout);

        initDashboardPage();
        initCRUDPage();

        panelContent.add(panelDashboard, "MENU_DASHBOARD");
        panelContent.add(panelCRUD, "MENU_KARYAWAN");

        panelUtama.add(panelContent, BorderLayout.CENTER);
        setContentPane(panelUtama);
        
        cardLayout.show(panelContent, "MENU_DASHBOARD");
        loadData(); 
    }

    // ==========================================
    // LOGIC BACKEND SUPABASE
    // ==========================================
    private int sendRequest(String method, String endpoint, String json) throws Exception {
        URL url = new URI(DatabaseConnection.URL_API + endpoint).toURL();
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();

        if (method.equals("UPSERT")) {
            conn.setRequestMethod("POST"); 
            conn.setRequestProperty("Prefer", "return=representation, resolution=merge-duplicates");
        } else {
            conn.setRequestMethod(method);
            conn.setRequestProperty("Prefer", "return=representation");
        }

        conn.setRequestProperty("apikey", DatabaseConnection.API_KEY);
        conn.setRequestProperty("Authorization", "Bearer " + DatabaseConnection.API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");

        if (json != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }
        }
        return conn.getResponseCode();
    }

    private void loadData() {
        try {
            String response = DatabaseConnection.fetchData("karyawan?select=*&order=id.asc");
            tableModel.setRowCount(0);

            if (response != null && !response.equals("[]")) {
                JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject obj = jsonArray.get(i).getAsJsonObject();
                    String id = obj.get("id").getAsString();
                    String nama = obj.get("nama_karyawan").getAsString();
                    String posisi = obj.get("posisi").getAsString();
                    tableModel.addRow(new Object[]{id, nama, posisi});
                }
            }
            
            // --- UPDATE ANGKA DASHBOARD SECARA OTOMATIS ---
            
            // 1. Update Total Karyawan (Dari jumlah baris tabel)
            lblTotalKaryawan.setText(String.valueOf(tableModel.getRowCount()));
            
            // 2. Update Total Divisi (Pake HashSet biar nggak ada duplikat)
            HashSet<String> divisiSet = new HashSet<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                // Ambil teks dari kolom posisi (index ke-2)
                String posisi = tableModel.getValueAt(i, 2).toString().trim().toLowerCase();
                divisiSet.add(posisi);
            }
            lblTotalDivisi.setText(String.valueOf(divisiSet.size()));
            
        } catch (Exception e) {
            System.err.println("Gagal sinkron database: " + e.getMessage());
        }
    }

    // ==========================================
    // UI DASHBOARD (3 KARTU)
    // ==========================================
    // ==========================================
    // UI DASHBOARD (JADI 2 KARTU LEBIH CLEAN)
    // ==========================================
    private void initDashboardPage() {
        panelDashboard = new JPanel(new BorderLayout());
        panelDashboard.setBackground(bgContent);
        panelDashboard.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        JLabel lblTitle = new JLabel("Ringkasan Perusahaan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(textDark);
        panelDashboard.add(lblTitle, BorderLayout.NORTH);

        // Ubah jadi GridLayout(1, 2) biar 2 kartu ini melar dan simetris
        JPanel panelCards = new JPanel(new GridLayout(1, 2, 25, 0)); 
        panelCards.setBackground(bgContent);
        panelCards.setBorder(new EmptyBorder(30, 0, 0, 0));

        // --- KARTU 1: TOTAL KARYAWAN ---
        JPanel card1 = new JPanel(new BorderLayout());
        card1.setBackground(Color.WHITE);
        card1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel t1 = new JLabel("Total Karyawan"); t1.setFont(new Font("Segoe UI", Font.BOLD, 16)); t1.setForeground(Color.GRAY);
        lblTotalKaryawan = new JLabel("0");
        lblTotalKaryawan.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblTotalKaryawan.setForeground(new Color(52, 152, 219)); // Warna Biru
        card1.add(t1, BorderLayout.NORTH); card1.add(lblTotalKaryawan, BorderLayout.CENTER);
        
        // --- KARTU 2: TOTAL DIVISI ---
        JPanel card2 = new JPanel(new BorderLayout());
        card2.setBackground(Color.WHITE);
        card2.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel t2 = new JLabel("Total Divisi"); t2.setFont(new Font("Segoe UI", Font.BOLD, 16)); t2.setForeground(Color.GRAY);
        lblTotalDivisi = new JLabel("0");
        lblTotalDivisi.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblTotalDivisi.setForeground(new Color(46, 204, 113)); // Warna Hijau
        card2.add(t2, BorderLayout.NORTH); card2.add(lblTotalDivisi, BorderLayout.CENTER);

        // Masukin 2 kartu ke dalam grid
        panelCards.add(card1);
        panelCards.add(card2);
        
        panelDashboard.add(panelCards, BorderLayout.CENTER);
    }

    // ==========================================
    // UI CRUD KARYAWAN
    // ==========================================
    private void initCRUDPage() {
        panelCRUD = new JPanel(new BorderLayout(15, 15));
        panelCRUD.setBackground(bgContent);
        panelCRUD.setBorder(new EmptyBorder(25, 30, 25, 30));

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
        gbc.insets = new Insets(8, 10, 8, 10); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 15);

        gbc.gridx = 0; gbc.gridy = 0; panelInput.add(createLabel("ID Karyawan", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtId = createTextField(fontInput); txtId.setEditable(false); txtId.setBackground(new Color(220, 220, 220)); panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelInput.add(createLabel("Nama Lengkap", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtNama = createTextField(fontInput); panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelInput.add(createLabel("Posisi / Jabatan", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtPosisi = createTextField(fontInput); panelInput.add(txtPosisi, gbc);
        
        panelAtas.add(panelInput, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelTombol.setBackground(bgContent);
        panelTombol.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        btnSimpan = createActionButton("Simpan", btnPrimary);
        btnUbah = createActionButton("Ubah", btnPrimary);
        btnHapus = createActionButton("Hapus", btnDanger);
        btnClear = createActionButton("Clear", btnSecondary);

        btnSimpan.addActionListener(e -> {
            if (!txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ini data lama masbro! Klik 'Ubah' kalau mau ngedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtNama.getText().trim().isEmpty() || txtPosisi.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Waduh, Nama dan Posisi nggak boleh kosong masbro! Isi dulu ya.", "Kolom Belum Lengkap", JOptionPane.WARNING_MESSAGE);
                return; 
            }
            try {
                String json = "{\"nama_karyawan\":\"" + txtNama.getText() + "\", \"posisi\":\"" + txtPosisi.getText() + "\"}";
                int code = sendRequest("POST", "karyawan", json);
                if (code == 201 || code == 200) {
                    JOptionPane.showMessageDialog(this, "Data Berhasil Masuk Database!");
                    clearForm(); loadData();
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnUbah.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel dulu yang mau diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String json = "{\"id\":" + txtId.getText() + ", \"nama_karyawan\":\"" + txtNama.getText() + "\", \"posisi\":\"" + txtPosisi.getText() + "\"}";
                int code = sendRequest("UPSERT", "karyawan", json);
                if (code == 201 || code == 200 || code == 204) {
                    JOptionPane.showMessageDialog(this, "Data Berhasil Diubah!");
                    clearForm(); loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah data! Kode error: " + code);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnHapus.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            boolean yakin = showCustomConfirm("Konfirmasi Hapus", "Yakin mau hapus data ini?");
            if (yakin) {
                try {
                    sendRequest("DELETE", "karyawan?id=eq." + txtId.getText(), null);
                    loadData(); clearForm();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        btnClear.addActionListener(e -> clearForm());

        panelTombol.add(btnSimpan); panelTombol.add(btnUbah); panelTombol.add(btnHapus); panelTombol.add(btnClear);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);
        panelCRUD.add(panelAtas, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nama Karyawan", "Posisi"}, 0);
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

        tabelKaryawan.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabelKaryawan.getSelectedRow();
                if (row != -1) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtNama.setText(tableModel.getValueAt(row, 1).toString());
                    txtPosisi.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabelKaryawan);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); 
        panelCRUD.add(scrollPane, BorderLayout.CENTER);
    }

    // ==========================================
    // SIDEBAR & COMPONENT HELPER
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

        JButton btnDash = createNavButton("   Dashboard");
        JButton btnData = createNavButton("   Data Karyawan");
        
        btnDash.addActionListener(e -> cardLayout.show(panelContent, "MENU_DASHBOARD"));
        btnData.addActionListener(e -> cardLayout.show(panelContent, "MENU_KARYAWAN"));
        
        panelMenu.add(btnDash); panelMenu.add(btnData);
        panelSidebar.add(panelMenu, BorderLayout.CENTER);
        
        JButton btnLogout = createNavButton("   Logout");
        btnLogout.setForeground(new Color(255, 150, 150)); 
        btnLogout.addActionListener(e -> {
            boolean yakinKeluar = showCustomConfirm("Konfirmasi Keluar", "Yakin mau keluar masbro?");
            if (yakinKeluar) {
                new LoginForm().setVisible(true); 
                this.dispose(); 
            }
        });
        panelSidebar.add(btnLogout, BorderLayout.SOUTH); 
        
        mainPanel.add(panelSidebar, BorderLayout.WEST);
    }

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
    
    private boolean showCustomConfirm(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true); dialog.setSize(380, 170); dialog.setLocationRelativeTo(this);
        
        JPanel p = new JPanel(new BorderLayout()); 
        p.setBorder(BorderFactory.createLineBorder(bgSidebar, 2)); p.setBackground(Color.WHITE);
        
        JPanel pHeader = new JPanel(new BorderLayout()); pHeader.setBackground(bgSidebar); pHeader.setPreferredSize(new Dimension(380, 40));
        JLabel lblTitle = new JLabel("   " + title); lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15)); lblTitle.setForeground(Color.WHITE);
        pHeader.add(lblTitle, BorderLayout.CENTER); p.add(pHeader, BorderLayout.NORTH);
        
        JLabel m = new JLabel(message, SwingConstants.CENTER); m.setFont(new Font("Segoe UI", Font.PLAIN, 16)); m.setForeground(textDark);
        
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