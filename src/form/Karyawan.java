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

    // Warna Tema
    private Color bgSidebar = new Color(30, 61, 89);
    private Color bgContent = new Color(245, 247, 250);
    private Color btnPrimary = new Color(255, 110, 64);
    private Color btnDanger = new Color(220, 53, 69);
    private Color btnSecondary = new Color(108, 117, 125);

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
        
        // JALANKAN LOAD DATA PAS START
        loadData(); 
    }

    private int sendRequest(String method, String endpoint, String json) throws Exception {
        URL url = new URI(DatabaseConnection.URL_API + endpoint).toURL();
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("apikey", DatabaseConnection.API_KEY);
        conn.setRequestProperty("Authorization", "Bearer " + DatabaseConnection.API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=representation");

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
            // Ambil data dari Supabase
            String response = DatabaseConnection.fetchData("karyawan?select=*&order=id.asc");
            tableModel.setRowCount(0);

            if (response != null && !response.equals("[]")) {
                // Parsing manual sederhana (karena lu pake JSON mentah)
                response = response.replace("[", "").replace("]", "");
                String[] items = response.split("\\},\\{");
                for (String item : items) {
                    String id = item.split("\"id\":")[1].split(",")[0];
                    String nama = item.split("\"nama_karyawan\":\"")[1].split("\"")[0];
                    String posisi = item.split("\"posisi\":\"")[1].split("\"")[0];
                    tableModel.addRow(new Object[]{id, nama, posisi});
                }
            }
            // SINKRONKAN ANGKA DASHBOARD DENGAN DATABASE
            lblTotalKaryawan.setText(String.valueOf(tableModel.getRowCount()));
        } catch (Exception e) {
            System.err.println("Gagal sinkron database: " + e.getMessage());
        }
    }

    private void initCRUDPage() {
        panelCRUD = new JPanel(new BorderLayout(15, 15));
        panelCRUD.setBackground(bgContent);
        panelCRUD.setBorder(new EmptyBorder(25, 30, 25, 30));

        // INPUT PANEL
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setBackground(bgContent);
        
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBackground(bgContent);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelInput.add(new JLabel("ID Karyawan"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtId = new JTextField(20); txtId.setEditable(false); panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelInput.add(new JLabel("Nama Lengkap"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtNama = new JTextField(20); panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelInput.add(new JLabel("Posisi / Jabatan"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtPosisi = new JTextField(20); panelInput.add(txtPosisi, gbc);
        panelAtas.add(panelInput, BorderLayout.CENTER);

        // BUTTONS
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelTombol.setBackground(bgContent);
        btnSimpan = createActionButton("Simpan", btnPrimary);
        btnUbah = createActionButton("Ubah", btnPrimary);
        btnHapus = createActionButton("Hapus", btnDanger);
        btnClear = createActionButton("Clear", btnSecondary);

        // --- ACTION SIMPAN BENERAN (BUKAN SIMULASI) ---
        btnSimpan.addActionListener(e -> {
            if (txtNama.getText().isEmpty()) return;
            try {
                String json = "{\"nama_karyawan\":\"" + txtNama.getText() + "\", \"posisi\":\"" + txtPosisi.getText() + "\"}";
                int code = sendRequest("POST", "karyawan", json);
                if (code == 201 || code == 200) {
                    JOptionPane.showMessageDialog(this, "Data Berhasil Masuk Database!");
                    clearForm(); loadData();
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // --- ACTION HAPUS ---
        btnHapus.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            int conf = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?");
            if (conf == JOptionPane.YES_OPTION) {
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

        // TABLE
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama Karyawan", "Posisi"}, 0);
        tabelKaryawan = new JTable(tableModel);
        tabelKaryawan.setRowHeight(30);
        tabelKaryawan.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabelKaryawan.getSelectedRow();
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtNama.setText(tableModel.getValueAt(row, 1).toString());
                txtPosisi.setText(tableModel.getValueAt(row, 2).toString());
            }
        });
        panelCRUD.add(new JScrollPane(tabelKaryawan), BorderLayout.CENTER);
    }

    private void initDashboardPage() {
        panelDashboard = new JPanel(new BorderLayout());
        panelDashboard.setBackground(bgContent);
        panelDashboard.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel panelCards = new JPanel(new GridLayout(1, 3, 25, 0));
        panelCards.setBackground(bgContent);

        // KARTU TOTAL KARYAWAN
        JPanel card1 = new JPanel(new BorderLayout());
        card1.setBackground(Color.WHITE);
        card1.setBorder(new EmptyBorder(20,20,20,20));
        JLabel t1 = new JLabel("Total Karyawan");
        lblTotalKaryawan = new JLabel("0"); // Start dari 0
        lblTotalKaryawan.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTotalKaryawan.setForeground(new Color(52, 152, 219));
        card1.add(t1, BorderLayout.NORTH);
        card1.add(lblTotalKaryawan, BorderLayout.CENTER);

        panelCards.add(card1);
        panelCards.add(new JLabel("Card Divisi Lainnya..."));
        panelDashboard.add(panelCards, BorderLayout.CENTER);
    }

    private void initSidebar(JPanel mainPanel) {
        JPanel panelSidebar = new JPanel(new GridLayout(10, 1));
        panelSidebar.setBackground(bgSidebar);
        panelSidebar.setPreferredSize(new Dimension(200, 0));
        
        JButton btnDash = new JButton("Dashboard");
        JButton btnData = new JButton("Data Karyawan");
        
        btnDash.addActionListener(e -> cardLayout.show(panelContent, "MENU_DASHBOARD"));
        btnData.addActionListener(e -> cardLayout.show(panelContent, "MENU_KARYAWAN"));
        
        panelSidebar.add(btnDash); panelSidebar.add(btnData);
        mainPanel.add(panelSidebar, BorderLayout.WEST);
    }

    private JButton createActionButton(String text, Color bg) {
        JButton b = new JButton(text); b.setBackground(bg); b.setForeground(Color.WHITE);
        return b;
    }

    private void clearForm() { txtId.setText(""); txtNama.setText(""); txtPosisi.setText(""); }
}