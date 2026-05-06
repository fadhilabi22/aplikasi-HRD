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
    import helper.DatabaseConnection;

    public class Karyawan extends JFrame {
        private JPanel panelContent, panelCRUD, panelDashboard;
        private CardLayout cardLayout;
        private JTextField txtId, txtNama, txtPosisi;
        private JButton btnSimpan, btnUbah, btnHapus, btnClear;
        private JTable tabelKaryawan;
        private DefaultTableModel tableModel;
        private JLabel lblTotalKaryawan;

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

            loadData(); // Tarik data pas aplikasi start
        }

        // --- UTILITY: KIRIM DATA KE SUPABASE ---
        private int sendRequest(String method, String endpoint, String json) throws Exception {
            URL url = new URI(DatabaseConnection.URL_API + endpoint).toURL();
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
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

        // --- LOGIC: TAMPIL DATA (READ) ---
        private void loadData() {
            try {
                String response = DatabaseConnection.fetchData("karyawan?select=*&order=id.asc");
                tableModel.setRowCount(0);

                if (!response.equals("[]")) {
                    response = response.replace("[", "").replace("]", "");
                    String[] items = response.split("\\},\\{");
                    for (String item : items) {
                        String id = item.split("\"id\":")[1].split(",")[0];
                        String nama = item.split("\"nama_karyawan\":\"")[1].split("\"")[0];
                        String posisi = item.split("\"posisi\":\"")[1].split("\"")[0];
                        tableModel.addRow(new Object[]{id, nama, posisi});
                    }
                }
                lblTotalKaryawan.setText(String.valueOf(tableModel.getRowCount()));
            } catch (Exception e) {
                System.err.println("Gagal Load: " + e.getMessage());
            }
        }

        private void initCRUDPage() {
            panelCRUD = new JPanel(new BorderLayout(15, 15));
            panelCRUD.setBackground(bgContent);
            panelCRUD.setBorder(new EmptyBorder(25, 30, 25, 30));

            // HEADER & INPUT
            JPanel panelAtas = new JPanel(new BorderLayout());
            panelAtas.setBackground(bgContent);
            JLabel lblJudul = new JLabel("Kelola Data Karyawan");
            lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblJudul.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            panelAtas.add(lblJudul, BorderLayout.NORTH);

            JPanel panelInput = new JPanel(new GridBagLayout());
            panelInput.setBackground(bgContent);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 10, 8, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = 0; panelInput.add(createLabel("ID Karyawan", new Font("Segoe UI", Font.BOLD, 14)), gbc);
            gbc.gridx = 1; gbc.gridy = 0; txtId = createTextField(new Font("Segoe UI", Font.PLAIN, 15)); txtId.setEditable(false); panelInput.add(txtId, gbc);

            gbc.gridx = 0; gbc.gridy = 1; panelInput.add(createLabel("Nama Lengkap", new Font("Segoe UI", Font.BOLD, 14)), gbc);
            gbc.gridx = 1; gbc.gridy = 1; txtNama = createTextField(new Font("Segoe UI", Font.PLAIN, 15)); panelInput.add(txtNama, gbc);

            gbc.gridx = 0; gbc.gridy = 2; panelInput.add(createLabel("Posisi / Jabatan", new Font("Segoe UI", Font.BOLD, 14)), gbc);
            gbc.gridx = 1; gbc.gridy = 2; txtPosisi = createTextField(new Font("Segoe UI", Font.PLAIN, 15)); panelInput.add(txtPosisi, gbc);
            panelAtas.add(panelInput, BorderLayout.CENTER);

            // TOMBOL AKSI
            JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            panelTombol.setBackground(bgContent);
            btnSimpan = createActionButton("Simpan", btnPrimary);
            btnUbah = createActionButton("Ubah", btnPrimary);
            btnHapus = createActionButton("Hapus", btnDanger);
            btnClear = createActionButton("Clear", btnSecondary);

            // --- ACTION SIMPAN ---
            btnSimpan.addActionListener(e -> {
                if (txtNama.getText().isEmpty() || txtPosisi.getText().isEmpty()) return;
                try {
                    String json = "{\"nama_karyawan\":\"" + txtNama.getText() + "\", \"posisi\":\"" + txtPosisi.getText() + "\"}";
                    if (sendRequest("POST", "karyawan", json) == 201) {
                        JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
                        loadData(); clearForm();
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            });

            // --- ACTION UBAH ---
            btnUbah.addActionListener(e -> {
                if (txtId.getText().isEmpty()) return;
                try {
                    String json = "{\"nama_karyawan\":\"" + txtNama.getText() + "\", \"posisi\":\"" + txtPosisi.getText() + "\"}";
                    int code = sendRequest("PATCH", "karyawan?id=eq." + txtId.getText(), json);
                    if (code == 200 || code == 204) {
                        JOptionPane.showMessageDialog(this, "Data Berhasil Diubah!");
                        loadData();
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            });

            // --- ACTION HAPUS ---
            btnHapus.addActionListener(e -> {
                if (txtId.getText().isEmpty()) return;
                if (showCustomConfirm("Konfirmasi", "Yakin hapus data ini masbro?")) {
                    try {
                        if (sendRequest("DELETE", "karyawan?id=eq." + txtId.getText(), null) <= 204) {
                            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus!");
                            loadData(); clearForm();
                        }
                    } catch (Exception ex) { ex.printStackTrace(); }
                }
            });

            btnClear.addActionListener(e -> clearForm());
            panelTombol.add(btnSimpan); panelTombol.add(btnUbah); panelTombol.add(btnHapus); panelTombol.add(btnClear);
            panelAtas.add(panelTombol, BorderLayout.SOUTH);
            panelCRUD.add(panelAtas, BorderLayout.NORTH);

            // TABEL SETUP
            tableModel = new DefaultTableModel(new String[]{"ID", "Nama Karyawan", "Posisi"}, 0);
            tabelKaryawan = new JTable(tableModel); // Memasang model ke tabel
            tabelKaryawan.setRowHeight(35);
            tabelKaryawan.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int row = tabelKaryawan.getSelectedRow();
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtNama.setText(tableModel.getValueAt(row, 1).toString());
                    txtPosisi.setText(tableModel.getValueAt(row, 2).toString());
                }
            });

            JScrollPane scrollPane = new JScrollPane(tabelKaryawan);
            panelCRUD.add(scrollPane, BorderLayout.CENTER);
        }

        // --- UI HELPERS (DASHBOARD) ---
        private void initDashboardPage() {
            panelDashboard = new JPanel(new BorderLayout());
            panelDashboard.setBackground(bgContent);
            panelDashboard.setBorder(new EmptyBorder(30, 40, 30, 40));
            JLabel lblTitle = new JLabel("Ringkasan Perusahaan");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
            panelDashboard.add(lblTitle, BorderLayout.NORTH);

            JPanel panelCards = new JPanel(new GridLayout(1, 3, 25, 0));
            panelCards.setBackground(bgContent);
            panelCards.setBorder(new EmptyBorder(30, 0, 0, 0));

            JPanel card1 = createStatCard("Total Karyawan", "0", new Color(52, 152, 219));
            lblTotalKaryawan = (JLabel) card1.getComponent(1); // Nangkep label angka

            panelCards.add(card1);
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
            card.add(t, BorderLayout.NORTH); card.add(v, BorderLayout.CENTER);
            return card;
        }

        private void initSidebar(JPanel mainPanel) {
            JPanel panelSidebar = new JPanel(new BorderLayout());
            panelSidebar.setBackground(bgSidebar);
            panelSidebar.setPreferredSize(new Dimension(220, 0));
            JLabel lblLogo = new JLabel("HRD SYSTEM", SwingConstants.CENTER);
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22)); lblLogo.setForeground(Color.WHITE);
            lblLogo.setBorder(new EmptyBorder(30, 10, 30, 10));
            panelSidebar.add(lblLogo, BorderLayout.NORTH);

            JPanel panelMenu = new JPanel(new GridLayout(10, 1, 0, 5));
            panelMenu.setBackground(bgSidebar); panelMenu.setBorder(new EmptyBorder(10, 10, 10, 10));

            JButton navDashboard = createNavButton("   Dashboard");
            JButton navKaryawan = createNavButton("   Data Karyawan");
            JButton btnLogout = createNavButton("   Logout");

            navDashboard.addActionListener(e -> cardLayout.show(panelContent, "MENU_DASHBOARD"));
            navKaryawan.addActionListener(e -> cardLayout.show(panelContent, "MENU_KARYAWAN"));
            btnLogout.addActionListener(e -> {
                if(showCustomConfirm("Logout", "Yakin mau keluar masbro?")) {
                    new LoginForm().setVisible(true); this.dispose();
                }
            });

            panelMenu.add(navDashboard); panelMenu.add(navKaryawan);
            panelSidebar.add(panelMenu, BorderLayout.CENTER);
            btnLogout.setForeground(new Color(255, 150, 150));
            panelSidebar.add(btnLogout, BorderLayout.SOUTH);
            mainPanel.add(panelSidebar, BorderLayout.WEST);
        }

        private JButton createNavButton(String text) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15)); btn.setForeground(Color.WHITE);
            btn.setBackground(bgSidebar); btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setFocusPainted(false); btn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(50, 80, 110)); }
                public void mouseExited(MouseEvent e) { btn.setBackground(bgSidebar); }
            });
            return btn;
        }

        private JButton createActionButton(String text, Color bgColor) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); btn.setBackground(bgColor); btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false); btn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
            return btn;
        }

        private JLabel createLabel(String text, Font font) { JLabel l = new JLabel(text); l.setFont(font); return l; }
        private JTextField createTextField(Font font) {
            JTextField tf = new JTextField(25); tf.setFont(font);
            tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1), BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            return tf;
        }
        private void clearForm() { txtId.setText(""); txtNama.setText(""); txtPosisi.setText(""); tabelKaryawan.clearSelection(); }
        private boolean showCustomConfirm(String title, String message) {
            int res = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
            return res == JOptionPane.YES_OPTION;
        }
    }