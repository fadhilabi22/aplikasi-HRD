package form;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URL;
import java.io.OutputStream;
import java.util.HashSet;
import helper.DatabaseConnection;
import entity.KaryawanEntity;
import com.google.gson.Gson;
import java.util.Map;
import java.util.HashMap;

public class Karyawan extends JFrame {
    private JPanel panelContent, panelCRUD, panelDashboard, panelChartContainer;
    private CardLayout cardLayout;
    private JTextField txtId, txtNama, txtPosisi;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable tabelKaryawan;
    private DefaultTableModel tableModel;
    private JLabel lblTotalKaryawan, lblTotalDivisi;

    // --- PALET WARNA SUPER ELEGANT (UPDATED) ---
    private Color bgMain = new Color(241, 245, 249);       // Slate 50 (Background lebih adem)
    private Color bgSidebarTop = new Color(15, 23, 42);    // Slate 900 (Deep Midnight)
    private Color bgSidebarBottom = new Color(30, 41, 59); // Slate 800 (Gradient Smooth)
    private Color accentPrimary = new Color(79, 70, 229);  // Indigo 600 (Aksen tombol)
    private Color accentSuccess = new Color(16, 185, 129); // Emerald 500 (Aksen sukses)
    private Color accentDanger = new Color(225, 29, 72);   // Rose 600 (Merah lebih modern)
    private Color textDark = new Color(15, 23, 42);        // Slate 900 (Teks utama tajam)
    private Color textLight = new Color(100, 116, 139);    // Slate 500 (Teks sekunder clean)
    private Color navHoverColor = new Color(51, 65, 85);   // Slate 700 (Warna saat menu disentuh)

    public Karyawan() {
        setTitle("HRMS Premium - PT. Inti Sejahtera");
        setSize(1100, 700); // Dibikin agak lebar biar lega
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Anti-aliasing buat font biar halus
        System.setProperty("awt.useSystemAAFontSettings", "on");

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(bgMain);

        initSidebar(panelUtama);
        
        cardLayout = new CardLayout();
        panelContent = new JPanel(cardLayout);
        panelContent.setOpaque(false);

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
    // LOGIC BACKEND (SAMA SEPERTI SEBELUMNYA)
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
            try (OutputStream os = conn.getOutputStream()) { os.write(json.getBytes()); }
        }
        return conn.getResponseCode();
    }

    private void loadData() {
    try {
        String response = DatabaseConnection.fetchData("karyawan?select=*&order=id.asc");
        tableModel.setRowCount(0);
        
        // Buat Map untuk menampung data grafik
        Map<String, Integer> deptData = new HashMap<>();

        if (response != null && !response.equals("[]")) {
            Gson gson = new Gson();
            KaryawanEntity[] dataKaryawan = gson.fromJson(response, KaryawanEntity[].class);
            for (KaryawanEntity k : dataKaryawan) {
                tableModel.addRow(new Object[]{k.getId(), k.getNama_karyawan(), k.getPosisi()});
                
                // --- LOGIKA HITUNG DATA CHART ---
                String posisi = (k.getPosisi() == null || k.getPosisi().isEmpty()) ? "Unknown" : k.getPosisi();
                deptData.put(posisi, deptData.getOrDefault(posisi, 0) + 1);
            }
        }

        // Update Label
        lblTotalKaryawan.setText(String.valueOf(tableModel.getRowCount()));
        lblTotalDivisi.setText(String.valueOf(deptData.size()));

        // --- PANGGIL FUNGSI CHART DI SINI MASBRO ---
        updateChart(deptData);

    } catch (Exception e) { 
        System.err.println("Gagal sinkron: " + e.getMessage()); 
    }
}

    // ==========================================
    // UI DASHBOARD (CLEAN & MINIMALIST)
    // ==========================================
    private void initDashboardPage() {
    panelDashboard = new JPanel(new BorderLayout(0, 30));
    panelDashboard.setBackground(bgMain);
    panelDashboard.setBorder(new EmptyBorder(40, 50, 40, 50));
    
    JLabel lblTitle = new JLabel("Global Overview");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
    lblTitle.setForeground(textDark);
    panelDashboard.add(lblTitle, BorderLayout.NORTH);

    JPanel panelCenter = new JPanel(new BorderLayout(0, 35));
    panelCenter.setOpaque(false);

    // Row 1: Cards
    JPanel panelCards = new JPanel(new GridLayout(1, 2, 35, 0)); 
    panelCards.setOpaque(false);
    panelCards.add(createModernCard("Active Employees", lblTotalKaryawan = new JLabel("0"), accentPrimary));
    panelCards.add(createModernCard("Department Count", lblTotalDivisi = new JLabel("0"), accentSuccess));
    panelCenter.add(panelCards, BorderLayout.NORTH);

    // Row 2: CUSTOM CHART (Tanpa Library)
    // Row 2: CUSTOM CHART
    panelChartContainer = new JPanel(new BorderLayout());
    panelChartContainer.setOpaque(false);
    // Beri margin agar tidak menempel ke pinggir
    panelChartContainer.setBorder(new EmptyBorder(20, 0, 0, 0)); 
    
    panelCenter.add(panelChartContainer, BorderLayout.CENTER);
    panelDashboard.add(panelCenter, BorderLayout.CENTER);
}
    private void updateChart(Map<String, Integer> data) {
    panelChartContainer.removeAll();
    
    // Kita bikin JPanel custom yang nge-gambar grafik batang
    JPanel canvas = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (data.isEmpty()) return;

            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int barWidth = (width - (2 * padding)) / Math.max(data.size(), 1) - 20;
            int maxVal = data.values().stream().max(Integer::compare).orElse(1);

            int x = padding + 10;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                // Itung tinggi batang relatif terhadap nilai maksimal
                int barHeight = (int) ((double) entry.getValue() / maxVal * (height - 100));
                
                // Gambar Batang (Warna Indigo biar konsisten)
                g2.setColor(accentPrimary);
                g2.fillRoundRect(x, height - padding - barHeight, barWidth, barHeight, 10, 10);

                // Gambar Teks Nilai di Atas Batang
                g2.setColor(textDark);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString(entry.getValue().toString(), x + (barWidth / 2) - 5, height - padding - barHeight - 10);

                // Gambar Nama Label (Posisi) di Bawah Batang
                g2.setColor(textLight);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                String label = entry.getKey().length() > 10 ? entry.getKey().substring(0, 10) + ".." : entry.getKey();
                g2.drawString(label, x, height - padding + 20);

                x += barWidth + 20;
            }
        }
    };

    canvas.setBackground(Color.WHITE);
    // Tambahkan baris ini biar panelnya punya tinggi masbro
    canvas.setPreferredSize(new Dimension(800, 300)); 
    canvas.setBorder(new CompoundBorder(
        new LineBorder(new Color(229, 231, 235), 1, true),
        new EmptyBorder(20, 20, 20, 20)
    ));

    panelChartContainer.removeAll(); // Hapus yang lama
    panelChartContainer.add(canvas, BorderLayout.CENTER);
    panelChartContainer.revalidate();
    panelChartContainer.repaint();
}

    // ==========================================
    // UI CRUD (ELEGANT & CLEAN)
    // ==========================================
    private void initCRUDPage() {
        panelCRUD = new JPanel(new BorderLayout(25, 25));
        panelCRUD.setBackground(bgMain);
        panelCRUD.setBorder(new EmptyBorder(35, 45, 35, 45));

        // --- Panel Input (Gaya Floating Card) ---
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setBackground(Color.WHITE);
        panelAtas.setBorder(new CompoundBorder(
            new LineBorder(new Color(229, 231, 235), 1, true),
            new EmptyBorder(30, 35, 30, 35)
        ));
        
        JLabel lblHeader = new JLabel("Employee Management");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(textDark);
        panelAtas.add(lblHeader, BorderLayout.NORTH);

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 15); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelInput.add(createLabel("ID", new Font("Segoe UI", Font.BOLD, 13)), gbc);
        gbc.gridx = 1; txtId = createTextField(); txtId.setEditable(false); txtId.setBackground(new Color(249, 250, 251)); panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelInput.add(createLabel("Nama Panjang", new Font("Segoe UI", Font.BOLD, 13)), gbc);
        gbc.gridx = 1; txtNama = createTextField(); panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelInput.add(createLabel("Jabatan", new Font("Segoe UI", Font.BOLD, 13)), gbc);
        gbc.gridx = 1; txtPosisi = createTextField(); panelInput.add(txtPosisi, gbc);
        
        panelAtas.add(panelInput, BorderLayout.CENTER);

        // Tombol Action
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        panelTombol.setOpaque(false);
        
        btnSimpan = createActionButton("Simpan", accentPrimary);
        btnUbah = createActionButton("Ubah", accentPrimary);
        btnHapus = createActionButton("Hapus", accentDanger);
        btnClear = createActionButton("Clear", new Color(156, 163, 175));

        // Logic Actions (Sudah pake Entity + GSON)
        btnSimpan.addActionListener(e -> {
            if (!txtId.getText().isEmpty()) return;
            if (txtNama.getText().trim().isEmpty()) return;
            try {
                KaryawanEntity kar = new KaryawanEntity();
                kar.setNama_karyawan(txtNama.getText().trim());
                kar.setPosisi(txtPosisi.getText().trim());
                String json = new Gson().toJson(kar);
                if (sendRequest("POST", "karyawan", json) == 201) {
                    showToast("Success: Data Saved!"); clearForm(); loadData();
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnUbah.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            try {
                KaryawanEntity kar = new KaryawanEntity();
                kar.setId(Integer.parseInt(txtId.getText()));
                kar.setNama_karyawan(txtNama.getText().trim());
                kar.setPosisi(txtPosisi.getText().trim());
                if (sendRequest("UPSERT", "karyawan", new Gson().toJson(kar)) < 300) {
                    showToast("Success: Info Updated!"); clearForm(); loadData();
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnHapus.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            if (showCustomConfirm("Peringatan", "Ingin menghapus data ini?")) {
                try { sendRequest("DELETE", "karyawan?id=eq." + txtId.getText(), null); loadData(); clearForm(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        btnClear.addActionListener(e -> clearForm());

        panelTombol.add(btnSimpan); panelTombol.add(btnUbah); panelTombol.add(btnHapus); panelTombol.add(btnClear);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);
        panelCRUD.add(panelAtas, BorderLayout.NORTH);

        // --- Table Section (Rounded & Clean) ---
        tableModel = new DefaultTableModel(new String[]{"ID", "Full Name", "Position"}, 0);
        tabelKaryawan = new JTable(tableModel);
        setupModernTable();
        
        JScrollPane scrollPane = new JScrollPane(tabelKaryawan);
        scrollPane.setBorder(new LineBorder(new Color(229, 231, 235), 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panelCRUD.add(scrollPane, BorderLayout.CENTER);
    }

    // ==========================================
    // SIDEBAR WITH GRADIENT VIBE
    // ==========================================
    private void initSidebar(JPanel mainPanel) {
        JPanel panelSidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); 
                Graphics2D g2d = (Graphics2D) g;
                
                // Aktifin smoothing biar gradasinya halus
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // KUNCINYA DI SINI: Kita panggil variabel bgSidebarTop dan bgSidebarBottom yang baru
                GradientPaint gp = new GradientPaint(0, 0, bgSidebarTop, 0, getHeight(), bgSidebarBottom);
                g2d.setPaint(gp); 
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelSidebar.setLayout(new BorderLayout());
        panelSidebar.setPreferredSize(new Dimension(250, 0));
        
        panelSidebar.setBorder(new MatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 10)));

        JLabel lblLogo = new JLabel("Dashboard HR", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBorder(new EmptyBorder(60, 0, 60, 0));
        panelSidebar.add(lblLogo, BorderLayout.NORTH);

        JPanel panelMenu = new JPanel(new GridLayout(8, 1, 0, 10));
        panelMenu.setOpaque(false);
        panelMenu.setBorder(new EmptyBorder(0, 15, 0, 15));

        JButton btnDash = createNavButton("   Dashboard");
        JButton btnData = createNavButton("   Directory");
        
        btnDash.addActionListener(e -> cardLayout.show(panelContent, "MENU_DASHBOARD"));
        btnData.addActionListener(e -> cardLayout.show(panelContent, "MENU_KARYAWAN"));
        
        panelMenu.add(btnDash); panelMenu.add(btnData);
        panelSidebar.add(panelMenu, BorderLayout.CENTER);
        
        JButton btnLogout = createNavButton("   Logout");
        btnLogout.setForeground(new Color(251, 113, 133));
        btnLogout.addActionListener(e -> {
            // Kita panggil method custom yang udah kita rakit di bawah
            boolean yakinKeluar = showCustomConfirm("Konfirmasi Keluar", "Anda yakin mau keluar dari sistem, masbro?");
            if (yakinKeluar) {
                new LoginForm().setVisible(true); 
                this.dispose(); 
            }
        });
        panelSidebar.add(btnLogout, BorderLayout.SOUTH);
        
        mainPanel.add(panelSidebar, BorderLayout.WEST);
    }

    // ==========================================
    // COMPONENT BUILDERS (THE "SECRET SAUCE")
    // ==========================================
    private JPanel createModernCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(229, 231, 235), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(textLight);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 54));
        valueLabel.setForeground(accent);
        
        card.add(t, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void setupModernTable() {
        tabelKaryawan.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabelKaryawan.setRowHeight(50);
        tabelKaryawan.setShowGrid(false);
        tabelKaryawan.setIntercellSpacing(new Dimension(0, 0));
        tabelKaryawan.setSelectionBackground(new Color(238, 242, 255));
        tabelKaryawan.setSelectionForeground(accentPrimary);
        
        JTableHeader h = tabelKaryawan.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(new Color(249, 250, 251));
        h.setForeground(textLight);
        h.setPreferredSize(new Dimension(0, 50));
        h.setBorder(new MatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));

        tabelKaryawan.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tabelKaryawan.getSelectedRow();
                txtId.setText(tableModel.getValueAt(r, 0).toString());
                txtNama.setText(tableModel.getValueAt(r, 1).toString());
                txtPosisi.setText(tableModel.getValueAt(r, 2).toString());
            }
        });
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Pake warna putih agak abu biar elegant (Slate 300)
        btn.setForeground(new Color(203, 213, 225)); 
        
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false); 
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Tambahin margin biar teks gak nempel banget ke pinggir
        btn.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                btn.setForeground(Color.WHITE); // Pas di-hover baru jadi Putih Terang
                btn.setBackground(navHoverColor);
                btn.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent e) { 
                btn.setForeground(new Color(203, 213, 225)); 
                btn.setContentAreaFilled(false);
            }
        });
        return btn;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 42));
        // Rounded corners effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField(25);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tf.setBorder(new CompoundBorder(
            new LineBorder(new Color(209, 213, 219), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        return tf;
    }

    private JLabel createLabel(String t, Font f) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(textDark); return l;
    }

    private void showToast(String msg) {
        JDialog dialog = new JDialog(this, "Informasi", true);
        dialog.setUndecorated(true); // Biar bingkai Windows ilang
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);

        Color greenSuccess = new Color(46, 204, 113);
        
        JPanel p = new JPanel(new BorderLayout()); 
        p.setBackground(Color.WHITE); 
        p.setBorder(BorderFactory.createLineBorder(greenSuccess, 2));

        // Header Notifikasi
        JPanel pH = new JPanel(new BorderLayout()); 
        pH.setBackground(greenSuccess); 
        pH.setPreferredSize(new Dimension(350, 35));
        JLabel lH = new JLabel("   BERHASIL", SwingConstants.LEFT); 
        lH.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        lH.setForeground(Color.WHITE);
        pH.add(lH, BorderLayout.CENTER);

        // Pesan Notifikasi
        JLabel lM = new JLabel(msg, SwingConstants.CENTER); 
        lM.setFont(new Font("Segoe UI", Font.PLAIN, 15)); 
        lM.setForeground(textDark);

        // Tombol Lanjutkan
        JPanel pB = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15)); 
        pB.setBackground(Color.WHITE);
        JButton bO = createActionButton("Lanjutkan", accentSuccess); 
        bO.setPreferredSize(new Dimension(120, 35));
        bO.addActionListener(al -> dialog.dispose()); 
        pB.add(bO);

        p.add(pH, BorderLayout.NORTH); 
        p.add(lM, BorderLayout.CENTER); 
        p.add(pB, BorderLayout.SOUTH);

        dialog.setContentPane(p); 
        dialog.setVisible(true);
    }

    private void clearForm() { txtId.setText(""); txtNama.setText(""); txtPosisi.setText(""); tabelKaryawan.clearSelection(); }

    private boolean showCustomConfirm(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true); // Biar bar judul Windows yang abu-abu ilang
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        
        // Panel Background Utama
        JPanel p = new JPanel(new BorderLayout()); 
        p.setBorder(BorderFactory.createLineBorder(bgSidebarTop, 2)); 
        p.setBackground(Color.WHITE);
        
        // Header Alert (Warna Slate Navy)
        JPanel pHeader = new JPanel(new BorderLayout()); 
        pHeader.setBackground(bgSidebarTop); 
        pHeader.setPreferredSize(new Dimension(400, 40));
        JLabel lblT = new JLabel("   " + title); 
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        lblT.setForeground(Color.WHITE);
        pHeader.add(lblT, BorderLayout.CENTER); 
        p.add(pHeader, BorderLayout.NORTH);
        
        // Pesan (Text Dark)
        JLabel m = new JLabel(message, SwingConstants.CENTER); 
        m.setFont(new Font("Segoe UI", Font.PLAIN, 15)); 
        m.setForeground(textDark);
        
        // Panel Tombol (Indigo & Gray)
        JPanel b = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20)); 
        b.setBackground(Color.WHITE);
        
        JButton y = createActionButton("Ya, Keluar", accentDanger); 
        JButton n = createActionButton("Batal", new Color(156, 163, 175));
        
        final boolean[] res = {false};
        y.addActionListener(e -> { res[0] = true; dialog.dispose(); });
        n.addActionListener(e -> { res[0] = false; dialog.dispose(); });
        
        b.add(y); b.add(n); 
        p.add(m, BorderLayout.CENTER); 
        p.add(b, BorderLayout.SOUTH);
        
        dialog.setContentPane(p); 
        dialog.setVisible(true);
        return res[0];
    }
}