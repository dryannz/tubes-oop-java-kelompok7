package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import model.*;

public class formPendaftaran extends JFrame {
    private JTextField inputNama, inputEmail, inputTelepon;
    private JComboBox<String> comboKursus;
    private JComboBox<String> comboMetodeBayar;
    private JTextArea outputArea;
    private JLabel labelBiaya;
    private sistemPendaftaran sistem;

    public formPendaftaran(sistemPendaftaran sistem) {
        this.sistem = sistem;
        initUI();
    }

    private void initUI() {
        setTitle("Pendaftaran Kursus Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // === PANEL INPUT ===
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("Form Pendaftaran"));
        
        // Komponen input
        inputNama = new JTextField();
        inputEmail = new JTextField();
        inputTelepon = new JTextField();
        
        comboKursus = new JComboBox<>(sistem.getDaftarNamaKursus());
        
        labelBiaya = new JLabel("Rp 0");
        labelBiaya.setForeground(new Color(0, 100, 0));
        labelBiaya.setFont(new Font("Arial", Font.BOLD, 12));
        
        String[] metodeAwal = {"Pilih kursus terlebih dahulu"};
        comboMetodeBayar = new JComboBox<>(metodeAwal);
        comboMetodeBayar.setEnabled(false);
        
        JButton btnDaftar = new JButton("Daftar Sekarang");
        btnDaftar.setBackground(new Color(70, 130, 180));
        btnDaftar.setForeground(Color.WHITE);
        JButton btnReset = new JButton("Reset");
        
        // Tambah komponen ke panel input
        inputPanel.add(new JLabel("Nama Lengkap:"));
        inputPanel.add(inputNama);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(inputEmail);
        inputPanel.add(new JLabel("Telepon:"));
        inputPanel.add(inputTelepon);
        inputPanel.add(new JLabel("Pilih Kursus:"));
        inputPanel.add(comboKursus);
        inputPanel.add(new JLabel("Biaya Kursus:"));
        inputPanel.add(labelBiaya);
        inputPanel.add(new JLabel("Metode Pembayaran:"));
        inputPanel.add(comboMetodeBayar);
        inputPanel.add(btnReset);
        inputPanel.add(btnDaftar);
        
        // === PANEL OUTPUT ===
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new TitledBorder("Hasil Pendaftaran"));
        
        outputArea = new JTextArea(12, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        
        // === PANEL TOMBOL OUTPUT ===
        JPanel outputButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        
        JButton btnClear = new JButton("Clear Output");
        btnClear.setBackground(new Color(220, 220, 220));
        
        JButton btnTampilkanKursus = new JButton("Tampilkan Kursus Tersedia");
        btnTampilkanKursus.setBackground(new Color(50, 150, 100));
        btnTampilkanKursus.setForeground(Color.WHITE);
        
        JButton btnTampilkanSiswa = new JButton("Tampilkan Data Siswa");
        btnTampilkanSiswa.setBackground(new Color(150, 100, 200));
        btnTampilkanSiswa.setForeground(Color.WHITE);
        
        outputButtonPanel.add(btnClear);
        outputButtonPanel.add(btnTampilkanKursus);
        outputButtonPanel.add(btnTampilkanSiswa);
        
        outputPanel.add(outputButtonPanel, BorderLayout.SOUTH);
        
        // === GABUNG PANEL ===
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        
        // === ACTION LISTENERS ===
        comboKursus.addActionListener(e -> updateMetodePembayaran());
        
        btnReset.addActionListener(e -> {
            inputNama.setText("");
            inputEmail.setText("");
            inputTelepon.setText("");
            comboKursus.setSelectedIndex(0);
            updateMetodePembayaran();
        });
        
        btnDaftar.addActionListener(e -> prosesPendaftaran());
        
        btnClear.addActionListener(e -> outputArea.setText(""));
        
        btnTampilkanKursus.addActionListener(e -> tampilkanKursusTersedia());
        
        btnTampilkanSiswa.addActionListener(e -> tampilkanDataSiswa());
        
        // Inisialisasi awal
        updateMetodePembayaran();
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void updateMetodePembayaran() {
        String selected = (String) comboKursus.getSelectedItem();
        if (selected == null) return;
        
        String kodeKursus = "";
        if (selected.contains("(")) {
            kodeKursus = selected.substring(selected.indexOf('(') + 1, selected.indexOf(')'));
        }
        
        kursus kursusDipilih = sistem.cariKursus(kodeKursus.trim());
        
        if (kursusDipilih != null) {
            double biaya = kursusDipilih.getBiaya();
            labelBiaya.setText("Rp " + String.format("%,.0f", biaya));
            
            if (biaya > 0) {
                String[] metodeBayar = {"Transfer Bank", "Kartu Kredit/Debit", "E-Wallet", "QRIS", "Virtual Account"};
                comboMetodeBayar.setModel(new DefaultComboBoxModel<>(metodeBayar));
                comboMetodeBayar.setEnabled(true);
                comboMetodeBayar.setSelectedIndex(0);
            } else {
                String[] metodeGratis = {"GRATIS"};
                comboMetodeBayar.setModel(new DefaultComboBoxModel<>(metodeGratis));
                comboMetodeBayar.setEnabled(false);
            }
        }
    }
    
    private void prosesPendaftaran() {
        String nama = inputNama.getText().trim();
        String email = inputEmail.getText().trim();
        String telepon = inputTelepon.getText().trim();
        
        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
            tambahOutput("ERROR: Semua field data diri harus diisi!");
            JOptionPane.showMessageDialog(this, "Semua field data diri harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String selectedKursus = (String) comboKursus.getSelectedItem();
        if (selectedKursus == null) {
            tambahOutput("ERROR: Pilih kursus terlebih dahulu!");
            return;
        }
        
        String selectedMetode = (String) comboMetodeBayar.getSelectedItem();
        if (selectedMetode == null || selectedMetode.equals("Pilih kursus terlebih dahulu")) {
            tambahOutput("ERROR: Pilih metode pembayaran!");
            return;
        }
        
        String kodeKursus = "";
        String namaKursus = selectedKursus;
        if (selectedKursus.contains("(")) {
            kodeKursus = selectedKursus.substring(selectedKursus.indexOf('(') + 1, selectedKursus.indexOf(')'));
            namaKursus = selectedKursus.substring(0, selectedKursus.indexOf('(')).trim();
        }
        
        try {
            kursus kursusDipilih = sistem.cariKursus(kodeKursus.trim());
            if (kursusDipilih == null) {
                tambahOutput("ERROR: Kursus " + kodeKursus + " tidak ditemukan!");
                return;
            }
            
            // Tampilkan data input
            tambahOutput("=".repeat(60));
            tambahOutput("PENDAFTARAN BARU - " + new java.util.Date());
            tambahOutput("=".repeat(60));
            tambahOutput("DATA SISWA:");
            tambahOutput("  Nama    : " + nama);
            tambahOutput("  Email   : " + email);
            tambahOutput("  Telepon : " + telepon);
            
            tambahOutput("\nDATA KURSUS:");
            tambahOutput("  Kursus  : " + namaKursus);
            tambahOutput("  Kode    : " + kodeKursus);
            tambahOutput("  Biaya   : Rp " + String.format("%,.0f", kursusDipilih.getBiaya()));
            tambahOutput("  Metode  : " + selectedMetode);
            
            String idSiswaBaru = "S" + String.format("%03d", sistem.getDaftarSiswa().size() + 1);
            
            tambahOutput("\nPROSES:");
            tambahOutput("  ✓ Membuat akun siswa: " + idSiswaBaru);
            tambahOutput("  ✓ Memproses pembayaran: " + selectedMetode);
            
            siswa siswaBaru = new siswa(idSiswaBaru, nama, email, telepon);
            
            pembayaran trx = sistem.enrollSiswa(idSiswaBaru, kodeKursus.trim(), selectedMetode);
            
            tambahOutput("\nHASIL TRANSAKSI:");
            tambahOutput("  ID Transaksi : " + trx.getIdTransaksi());
            tambahOutput("  Status       : " + trx.getStatus());
            tambahOutput("  Tanggal      : " + trx.getTanggal());
            
            tambahOutput("\n" + "=".repeat(60));
            if (trx.getStatus().equals("SUKSES")) {
                tambahOutput("✓ PENDAFTARAN BERHASIL!");
                tambahOutput("  ID Siswa: " + idSiswaBaru);
            } else {
                tambahOutput("⚠ MENUNGGU PEMBAYARAN");
            }
            tambahOutput("=".repeat(60) + "\n\n");
            
            // Reset telepon field
            inputTelepon.setText("");
            
        } catch (Exception e) {
            tambahOutput("ERROR: " + e.getMessage() + "\n");
        }
    }
    
    private void tampilkanKursusTersedia() {
        tambahOutput("\n" + "=".repeat(60));
        tambahOutput("DAFTAR KURSUS YANG TERSEDIA");
        tambahOutput("=".repeat(60));
        
        int nomor = 1;
        for (kursus k : sistem.getDaftarKursus()) {
            tambahOutput(String.format("%2d. %-30s (Kode: %s)", nomor++, k.getNama(), k.getKodeKursus()));
            tambahOutput(String.format("    Biaya: Rp %,.0f", k.getBiaya()));
            tambahOutput(String.format("    Kuota: %s/%s siswa", 
                k.getJumlahSiswaTerdaftar(), k.getKuotaMaksimal()));
            tambahOutput("    ──────────────────────────────────────────");
        }
        tambahOutput("=".repeat(60) + "\n");
    }
    
    private void tampilkanDataSiswa() {
        tambahOutput("\n" + "=".repeat(60));
        tambahOutput("DATA SISWA TERDAFTAR DI SISTEM");
        tambahOutput("=".repeat(60));
        
        int nomor = 1;
        for (siswa s : sistem.getDaftarSiswa()) {
            tambahOutput(String.format("%2d. ID: %-8s | Nama: %-20s", nomor++, s.getIdSiswa(), s.getNama()));
            tambahOutput(String.format("    Email: %-25s | Telp: %s", s.getEmail(), s.getNomorTelepon()));
            
            // Tampilkan kursus yang diambil
            if (s.getDaftarKursus().isEmpty()) {
                tambahOutput("    Kursus: (belum mengambil kursus)");
            } else {
                tambahOutput("    Kursus yang diambil:");
                for (String kodeKursus : s.getDaftarKursus()) {
                    kursus k = sistem.cariKursus(kodeKursus);
                    if (k != null) {
                        tambahOutput("      • " + k.getNama() + " (" + k.getKodeKursus() + ")");
                    }
                }
            }
            tambahOutput("    ──────────────────────────────────────────");
        }
        tambahOutput("=".repeat(60) + "\n");
    }
    
    private void tambahOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
}