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
    // Deklarasi Komponen
    private JTextField txtId, txtNama, txtPosisi;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable tabelKaryawan;
    private DefaultTableModel tableModel;

    // Palette Warna Tema
    private Color bgUtama = new Color(30, 61, 89);          // Navy / Biru Gelap
    private Color warnaTeks = Color.WHITE;                  // Teks Putih
    private Color btnPrimary = new Color(255, 110, 64);     // Coral / Orange (Simpan & Ubah)
    private Color btnDanger = new Color(220, 53, 69);       // Merah (Hapus)
    private Color btnSecondary = new Color(108, 117, 125);  // Abu-abu (Clear)

    public Karyawan() {
        // 1. Pengaturan Dasar Frame
        setTitle("Kelola Data Karyawan - HRD");
        setSize(750, 550); // Ukuran lebih lega
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel Utama pembungkus semua komponen
        JPanel panelUtama = new JPanel(new BorderLayout(15, 15));
        panelUtama.setBackground(bgUtama);
        panelUtama.setBorder(new EmptyBorder(20, 30, 20, 30)); // Margin Frame

        // --- BAGIAN ATAS (Header & Form Input) ---
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setBackground(bgUtama);
        
        // Judul Form
        JLabel lblJudul = new JLabel("DATA KARYAWAN", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblJudul.setForeground(warnaTeks);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelAtas.add(lblJudul, BorderLayout.NORTH);

        // Form Input (Pake GridBagLayout)
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBackground(bgUtama);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); // Jarak antar inputan
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Segoe UI", Font.PLAIN, 15);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 15);

        // Baris 1: ID Karyawan
        gbc.gridx = 0; gbc.gridy = 0;
        panelInput.add(createLabel("ID Karyawan :", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtId = createTextField(fontInput);
        txtId.setEditable(false);
        txtId.setBackground(new Color(200, 200, 200)); // Warna abu-abu (disabled)
        panelInput.add(txtId, gbc);

        // Baris 2: Nama Karyawan
        gbc.gridx = 0; gbc.gridy = 1;
        panelInput.add(createLabel("Nama Karyawan :", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNama = createTextField(fontInput);
        panelInput.add(txtNama, gbc);

        // Baris 3: Posisi
        gbc.gridx = 0; gbc.gridy = 2;
        panelInput.add(createLabel("Posisi / Jabatan :", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtPosisi = createTextField(fontInput);
        panelInput.add(txtPosisi, gbc);

        panelAtas.add(panelInput, BorderLayout.CENTER);

        // --- BAGIAN TENGAH (Tombol Aksi) ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelTombol.setBackground(bgUtama);
        panelTombol.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        btnSimpan = createButton("SIMPAN", btnPrimary);
        btnUbah = createButton("UBAH", btnPrimary);
        btnHapus = createButton("HAPUS", btnDanger);
        btnClear = createButton("CLEAR FORM", btnSecondary);

        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnClear);
        
        panelAtas.add(panelTombol, BorderLayout.SOUTH);

        // Masukin ke Panel Utama
        panelUtama.add(panelAtas, BorderLayout.NORTH);

        // --- BAGIAN BAWAH (Tabel Data) ---
        String[] kolom = {"ID", "Nama Karyawan", "Posisi"};
        tableModel = new DefaultTableModel(kolom, 0);
        tabelKaryawan = new JTable(tableModel);
        
        // Kustomisasi Estetika Tabel
        tabelKaryawan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelKaryawan.setRowHeight(30);
        tabelKaryawan.setSelectionBackground(new Color(255, 200, 180)); // Warna highlight baris tabel
        tabelKaryawan.setSelectionForeground(Color.BLACK);
        tabelKaryawan.setShowVerticalLines(false); // Ilangin garis vertikal biar modern
        tabelKaryawan.setGridColor(new Color(230, 230, 230));

        // Kustomisasi Header Tabel
        JTableHeader headerTabel = tabelKaryawan.getTableHeader();
        headerTabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        headerTabel.setBackground(btnPrimary); // Warna header Coral
        headerTabel.setForeground(Color.WHITE);
        headerTabel.setPreferredSize(new Dimension(100, 35)); // Tinggi header
        
        JScrollPane scrollPane = new JScrollPane(tabelKaryawan);
        scrollPane.getViewport().setBackground(Color.WHITE); // Background tempat tabel
        scrollPane.setBorder(BorderFactory.createLineBorder(btnPrimary, 2)); // Border luar tabel

        panelUtama.add(scrollPane, BorderLayout.CENTER);

        // --- DUMMY DATA ---
        tableModel.addRow(new Object[]{"1", "Budi Santoso", "HR Manager"});
        tableModel.addRow(new Object[]{"2", "Siti Aminah", "Staff IT"});
        tableModel.addRow(new Object[]{"3", "Andi Darmawan", "Finance"});

        // Pasang panel utama ke Frame
        setContentPane(panelUtama);

        // ==========================================
        // ACTION LISTENERS (Sama kayak sebelumnya)
        // ==========================================
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

        btnClear.addActionListener(e -> clearForm());

        btnSimpan.addActionListener(e -> {
            if (validasiInput()) {
                JOptionPane.showMessageDialog(this, "Simulasi Menyimpan ke Supabase...");
                clearForm();
            }
        });

        btnUbah.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel dulu!");
            } else if (validasiInput()) {
                JOptionPane.showMessageDialog(this, "Simulasi Update ke Supabase...");
                clearForm();
            }
        });

        btnHapus.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel dulu!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Simulasi Hapus dari Supabase...");
                    clearForm();
                }
            }
        });
    }

    // --- METHOD HELPER BUAT BIKIN UI AESTHETIC ---
    
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(warnaTeks);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField textField = new JTextField(20);
        textField.setFont(font);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(7, 10, 7, 10) // Padding text dalam
        ));
        return textField;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding tombol
        
        // Efek Hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter()); // Lebih terang pas di-hover
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor); // Balik normal
            }
        });
        return button;
    }

    private boolean validasiInput() {
        if (txtNama.getText().isEmpty() || txtPosisi.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Posisi harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtId.setText("");
        txtNama.setText("");
        txtPosisi.setText("");
        tabelKaryawan.clearSelection();
    }
}
