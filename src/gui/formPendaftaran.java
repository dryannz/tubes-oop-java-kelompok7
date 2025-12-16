package gui;

import model.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class formPendaftaran extends JFrame {
    private sistemPendaftaran sistem;
    private daftarKursus frameDataSiswa;
    
    private JTextField inputNama, inputEmail, inputTelepon;
    private JComboBox<String> comboKursus;
    private JComboBox<String> comboMetode; 
    private JLabel labelBiaya;
    
    public formPendaftaran(sistemPendaftaran sistem) {
        this.sistem = sistem;
        setupGUI();
        setVisible(true);
    }
    
    private void setupGUI() {
        setTitle("Pendaftaran Kursus Online");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JLabel title = new JLabel("PENDAFTARAN KURSUS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(70, 130, 180));
        
        // Panel form
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(new TitledBorder("Form Pendaftaran"));
        
        inputNama = new JTextField();
        inputEmail = new JTextField();
        inputTelepon = new JTextField();
        
        comboKursus = new JComboBox<>(sistem.getDaftarNamaKursus());
        comboKursus.addActionListener(e -> updateBiaya());
        
        labelBiaya = new JLabel("Rp 0");
        labelBiaya.setForeground(Color.RED);
        labelBiaya.setFont(new Font("Arial", Font.BOLD, 12));
        
        // METODE BAYAR - DIPINDAH KE VARIABLE INSTANCE
        comboMetode = new JComboBox<>(new String[]{"Transfer Bank", "Kartu Kredit", "E-Wallet"});
        
        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnReset = new JButton("Reset");
        JButton btnDaftar = new JButton("DAFTAR SEKARANG");
        btnDaftar.setBackground(new Color(46, 204, 113));
        btnDaftar.setForeground(Color.WHITE);
        btnDaftar.addActionListener(e -> prosesDaftar());
        
        JButton btnLihatData = new JButton("Lihat Data Terdaftar");
        btnLihatData.setBackground(new Color(155, 89, 182));
        btnLihatData.setForeground(Color.WHITE);
        btnLihatData.addActionListener(e -> tampilkanDataSiswa());
        
        // Tambah ke form
        formPanel.add(new JLabel("Nama Lengkap:"));
        formPanel.add(inputNama);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(inputEmail);
        formPanel.add(new JLabel("Telepon:"));
        formPanel.add(inputTelepon);
        formPanel.add(new JLabel("Pilih Kursus:"));
        formPanel.add(comboKursus);
        formPanel.add(new JLabel("Biaya Kursus:"));
        formPanel.add(labelBiaya);
        formPanel.add(new JLabel("Metode Bayar:"));
        formPanel.add(comboMetode);
        
        // Button panel
        buttonPanel.add(btnReset);
        buttonPanel.add(btnDaftar);
        buttonPanel.add(btnLihatData);
        
        // Gabung semua
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        updateBiaya(); // Panggil sekali di awal untuk set status awal
    }
    
    private void updateBiaya() {
        String selected = (String) comboKursus.getSelectedItem();
        if (selected == null) return;
        
        String kode = selected.substring(selected.indexOf('(')+1, selected.indexOf(')'));
        kursus k = sistem.cariKursus(kode.trim());
        if (k != null) {
            double biaya = k.getBiaya();
            labelBiaya.setText("Rp " + String.format("%,.0f", biaya));
            
            // LOGIKA BARU: Nonaktifkan comboMetode jika biaya = 0
            if (biaya == 0) {
                comboMetode.setEnabled(false);
                comboMetode.setSelectedItem("Transfer Bank"); // Set default value
            } else {
                comboMetode.setEnabled(true);
            }
        }
    }
    
    private void prosesDaftar() {
        String nama = inputNama.getText().trim();
        String email = inputEmail.getText().trim();
        String telepon = inputTelepon.getText().trim();
        
        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi semua data!");
            return;
        }
        
        String selectedKursus = (String) comboKursus.getSelectedItem();
        String kodeKursus = selectedKursus.substring(
            selectedKursus.indexOf('(')+1, 
            selectedKursus.indexOf(')')
        );
        String namaKursus = selectedKursus.substring(0, selectedKursus.indexOf('(')).trim();
        
        try {
            String idSiswa = "S" + String.format("%03d", sistem.getDaftarSiswa().size() + 1);
            siswa siswaBaru = new siswa(idSiswa, nama, email, telepon);
            
            // Simpan ke sistem (simplified)
            sistem.getDaftarSiswa().add(siswaBaru);
            kursus kursusDipilih = sistem.cariKursus(kodeKursus.trim());
            
            if (kursusDipilih != null) {
                siswaBaru.getDaftarKursus().add(kodeKursus.trim());
                
                // Ambil metode bayar dari comboBox
                String metodeBayar = (String) comboMetode.getSelectedItem();
                
                // TAMPILKAN POPUP KONFIRMASI
                showConfirmationPopup(nama, email, telepon, namaKursus, kodeKursus, 
                                     kursusDipilih.getBiaya(), metodeBayar, idSiswa);
                
                // Reset form
                inputNama.setText("");
                inputEmail.setText("");
                inputTelepon.setText("");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void showConfirmationPopup(String nama, String email, String telepon,
                                      String namaKursus, String kodeKursus, 
                                      double biaya, String metode, String idSiswa) {
        
        JDialog popup = new JDialog(this, "Konfirmasi Pendaftaran", true);
        popup.setSize(400, 350);
        popup.setLocationRelativeTo(this);
        popup.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(46, 204, 113));
        JLabel title = new JLabel("DAFTAR SEKARANG");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Konten
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Data
        content.add(new JLabel("HASIL DAN OUTPUT"));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(new JLabel("Nama: " + nama));
        content.add(new JLabel("Email: " + email));
        content.add(new JLabel("Telepon: " + telepon));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Data Kursus
        content.add(new JLabel("DATA KURSUS:"));
        content.add(new JLabel("Kursus: " + namaKursus));
        content.add(new JLabel("Kode: " + kodeKursus));
        content.add(new JLabel("Biaya: Rp " + String.format("%,.0f", biaya)));
        
        // Tampilkan metode bayar atau "GRATIS" jika biaya = 0
        if (biaya == 0) {
            content.add(new JLabel("Metode: GRATIS (Tidak perlu pembayaran)"));
        } else {
            content.add(new JLabel("Metode: " + metode));
        }
        
        // ID Siswa
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(new JLabel("ID Pendaftaran: " + idSiswa));
        
        // Tombol
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(e -> popup.dispose());
        
        popup.add(header, BorderLayout.NORTH);
        popup.add(content, BorderLayout.CENTER);
        popup.add(btnOK, BorderLayout.SOUTH);
        popup.setVisible(true);
    }
    
   private void tampilkanDataSiswa() {
    if (frameDataSiswa == null) {
        frameDataSiswa = new daftarKursus(sistem);
    } else {
        // PERBAIKAN: Refresh data setiap kali frame dibuka
        frameDataSiswa.refreshData();
        frameDataSiswa.setVisible(true);
    }
}
}