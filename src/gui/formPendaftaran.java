package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import model.*;

public class formPendaftaran extends JFrame {
    private sistemPendaftaran sistem;
    private daftarKursus frameDataSiswa;

    private JTextField inputNama, inputEmail, inputTelepon;
    private JComboBox<String> comboKursus;
    private JComboBox<String> comboMetode; 
    private JLabel labelBiaya, labelSistemInfo;
    
    public formPendaftaran(sistemPendaftaran sistem) {
        this.sistem = sistem;
        setupGUI();
        setVisible(true);
    }
    
    private void setupGUI() {
        setTitle("Pendaftaran Kursus Online");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("PENDAFTARAN KURSUS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(70, 130, 180));
        
        // Info sistem di pojok kanan atas
        labelSistemInfo = new JLabel("Sistem: " + sistem.getidSistem());
        labelSistemInfo.setFont(new Font("Arial", Font.ITALIC, 10));
        labelSistemInfo.setForeground(Color.GRAY);
        labelSistemInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(labelSistemInfo, BorderLayout.EAST);
        
        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Form Pendaftaran"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        inputNama = new JTextField(20);
        inputEmail = new JTextField(20);
        inputTelepon = new JTextField(20);
        
        // Inisialisasi comboKursus dengan data kuota dari backend
        comboKursus = new JComboBox<>(sistem.getDaftarKursusDenganKuota());
        
        // Tambahkan custom renderer untuk warna
        comboKursus.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value != null) {
                    String text = value.toString();
                    
                    // Parse kode kursus untuk cek kuota
                    if (text.contains("(") && text.contains(")")) {
                        String kode = text.substring(
                            text.indexOf('(') + 1,
                            text.indexOf(')')
                        ).trim();
                        
                        kursus k = sistem.cariKursus(kode);
                        if (k != null && !k.cekKetersediaan()) {
                            // Jika kuota penuh, beri warna merah
                            setForeground(Color.RED);
                            setText(text + " [PENUH]");
                        } else if (isSelected) {
                            setForeground(Color.WHITE);
                            setBackground(new Color(70, 130, 180));
                        } else {
                            setForeground(Color.BLACK);
                            setBackground(Color.WHITE);
                        }
                    }
                }
                return this;
            }
        });
        
        comboKursus.addActionListener(e -> updateBiayaDanMetode());
        
        labelBiaya = new JLabel("Rp 0");
        labelBiaya.setForeground(Color.RED);
        labelBiaya.setFont(new Font("Arial", Font.BOLD, 12));
        
        comboMetode = new JComboBox<>(new String[]{"", "Transfer Bank", "Kartu Kredit", "E-Wallet"});

        // Tambah komponen ke form dengan GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1;
        formPanel.add(inputNama, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(inputEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Telepon:"), gbc);
        gbc.gridx = 1;
        formPanel.add(inputTelepon, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Pilih Kursus:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboKursus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Biaya Kursus:"), gbc);
        gbc.gridx = 1;
        formPanel.add(labelBiaya, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Metode Bayar:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboMetode, gbc);
        
        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(this::handleReset);
        
        JButton btnDaftar = new JButton("DAFTAR SEKARANG");
        btnDaftar.setBackground(new Color(46, 204, 113));
        btnDaftar.setForeground(Color.WHITE);
        btnDaftar.addActionListener(this::handleDaftar);
        
        JButton btnLihatData = new JButton("Lihat Data");
        btnLihatData.setBackground(new Color(155, 89, 182));
        btnLihatData.setForeground(Color.WHITE);
        btnLihatData.addActionListener(this::handleLihatData);
        
        buttonPanel.add(btnReset);
        buttonPanel.add(btnDaftar);
        buttonPanel.add(btnLihatData);
        
        // Panel info kuota
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnInfoKuota = new JButton("Info Kuota Kursus");
        btnInfoKuota.setBackground(new Color(52, 152, 219));
        btnInfoKuota.setForeground(Color.WHITE);
        btnInfoKuota.addActionListener(e -> showInfoKuota());
        infoPanel.add(btnInfoKuota);
        
        // Gabung semua
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(infoPanel, BorderLayout.SOUTH);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        updateBiayaDanMetode();
    }
    
    private void updateBiayaDanMetode() {
        String selected = (String) comboKursus.getSelectedItem();
        if (selected == null || selected.isEmpty()) return;
        
        // Parse kode kursus dari string yang baru
        // Format: "JAVA PROGRAMMING (K001) | 0/3 Terdaftar"
        String kode;
        try {
            kode = selected.substring(
                selected.indexOf('(') + 1,
                selected.indexOf(')')
            ).trim();
        } catch (StringIndexOutOfBoundsException e) {
            // Fallback jika format tidak sesuai
            // Coba ambil kode dengan cara lain
            if (selected.contains("K")) {
                int start = selected.indexOf("K");
                kode = selected.substring(start, Math.min(start + 4, selected.length()));
            } else {
                return;
            }
        }
        
        kursus k = sistem.cariKursus(kode);
        if (k == null) return;
        
        double biaya = k.getBiaya();
        labelBiaya.setText("Rp " + String.format("%,.0f", biaya));
        
        // Update informasi kuota di tooltip
        String kuotaInfo = String.format("Kuota: %d/%d siswa - %s",
            k.getJumlahSiswaTerdaftar(),
            k.getKuotaMaksimal(),
            k.cekKetersediaan() ? "Masih tersedia" : "PENUH");
        comboKursus.setToolTipText(kuotaInfo);
        
        if (biaya == 0) {
            // Kursus gratis → biarkan MODEL yang set GRATIS & SUKSES
            comboMetode.setEnabled(false);
            comboMetode.setSelectedItem("GRATIS");
            comboMetode.setToolTipText("Kursus Gratis");
        } else {
            // Kursus berbayar → biarkan user & MODEL yang menentukan status
            comboMetode.setEnabled(true);
            comboMetode.setToolTipText("Pilih Metode Pembayaran");
        }
    }
    
    // Method untuk refresh comboBox dengan data terbaru dari backend
    private void refreshKursusComboBox() {
        // Simpan item yang sedang dipilih
        String selected = (String) comboKursus.getSelectedItem();
        
        // Dapatkan data terbaru dari backend
        String[] daftarTerbaru = sistem.getDaftarKursusDenganKuota();
        
        // Clear dan isi ulang comboBox
        comboKursus.removeAllItems();
        for (String kursus : daftarTerbaru) {
            comboKursus.addItem(kursus);
        }
        
        // Coba set kembali ke item yang sama (jika masih ada)
        if (selected != null) {
            for (int i = 0; i < comboKursus.getItemCount(); i++) {
                if (comboKursus.getItemAt(i).toString().contains(selected)) {
                    comboKursus.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        // Jika tidak ada yang dipilih, pilih item pertama
        if (comboKursus.getSelectedIndex() == -1 && comboKursus.getItemCount() > 0) {
            comboKursus.setSelectedIndex(0);
        }
        
        // Update biaya dan metode berdasarkan pilihan baru
        updateBiayaDanMetode();
    }
    
    private void handleReset(ActionEvent e) {
        inputNama.setText("");
        inputEmail.setText("");
        inputTelepon.setText("");
        
        // Refresh comboBox untuk update kuota terbaru
        refreshKursusComboBox();
        
        if (comboKursus.getItemCount() > 0) {
            comboKursus.setSelectedIndex(0);
        }
        
        comboMetode.setSelectedIndex(0);
        updateBiayaDanMetode();
    }
    
    private void handleDaftar(ActionEvent e) {
        // 1. Validasi input
        String nama = inputNama.getText().trim();
        String email = inputEmail.getText().trim();
        String telepon = inputTelepon.getText().trim();

        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Harap lengkapi semua data!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 2. Ambil kursus
        String selectedKursus = (String) comboKursus.getSelectedItem();
        if (selectedKursus == null) {
            JOptionPane.showMessageDialog(this, "Pilih kursus terlebih dahulu!");
            return;
        }

        // Parse kode kursus dari format baru
        String kodeKursus;
        try {
            kodeKursus = selectedKursus.substring(
                selectedKursus.indexOf('(') + 1,
                selectedKursus.indexOf(')')
            ).trim();
        } catch (StringIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Format kursus tidak valid!");
            return;
        }

        String metodeBayar = (String) comboMetode.getSelectedItem();

        kursus kursusObj = sistem.cariKursus(kodeKursus);
        if (kursusObj != null && kursusObj.getBiaya() == 0) {
            metodeBayar = "GRATIS";
        }

        try {
            siswa siswa = null;

            for (siswa s : sistem.getDaftarSiswa()) {
                if (s.getEmail().equalsIgnoreCase(email)) {
                    // NAMA BEDA = TOLAK
                    if (!s.getNama().equalsIgnoreCase(nama)) {
                        throw new IllegalArgumentException(
                            "Email sudah terdaftar dengan nama berbeda!"
                        );
                    }

                    // EMAIL & NAMA SAMA = SISWA LAMA
                    siswa = s;
                    break;
                }

                // NAMA SAMA TAPI EMAIL BEDA = TOLAK
                if (s.getNama().equalsIgnoreCase(nama)) {
                    throw new IllegalArgumentException(
                        "Nama sudah terdaftar dengan email berbeda!"
                    );
                }
            }

            String idSiswa;

            // kalau siswa belum ada → buat baru
            if (siswa == null) {
                idSiswa = "S" + String.format(
                    "%03d", sistem.getDaftarSiswa().size() + 1
                );

                siswa = new siswa(
                    idSiswa,
                    nama,
                    email,
                    telepon
                );

                sistem.tambahSiswa(siswa);
            } else {
                // kalau sudah ada → pakai ID lama
                idSiswa = siswa.getIdSiswa();
            }

            // BARU PROSES ENROLL
            pembayaran transaksi = sistem.enrollSiswa(
                idSiswa,
                kodeKursus,
                metodeBayar
            );

            // TAMPILKAN HASIL
            showConfirmationDialog(siswa, kursusObj, transaksi);
            
            // REFRESH COMBOBOX SETELAH PENDAFTARAN BERHASIL
            refreshKursusComboBox();
            
            handleReset(e);

        } catch (kuotaPenuhException kpe) {
            handleKuotaPenuhException(kpe, kursusObj.getNama());

        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(
                this,
                iae.getMessage(),
                "Pendaftaran Gagal",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleKuotaPenuhException(kuotaPenuhException kpe, String namaKursus) {
        kursus kursusPenuh = sistem.cariKursus(kpe.getKodeKursusYangGagal());
        String message = String.format(
            "<html><b>Kursus %s sudah penuh!</b><br><br>" +
            "Kuota maksimal: %s siswa<br>" +
            "Siswa terdaftar: %d siswa<br><br>" +
            "Silakan pilih kursus lain atau lihat data terdaftar.</html>",
            namaKursus,
            kursusPenuh != null ? kursusPenuh.getKuotaMaksimal() : "3",
            kursusPenuh != null ? kursusPenuh.getJumlahSiswaTerdaftar() : 0
        );
        
        JOptionPane.showMessageDialog(this,
            message,
            "Kuota Penuh",
            JOptionPane.WARNING_MESSAGE);
    }
    
    private void showConfirmationDialog(siswa siswa, kursus kursus, pembayaran transaksi) {
        JDialog dialog = new JDialog(this, "Konfirmasi Pendaftaran", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel(transaksi.getStatus(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        
        JLabel sistemLabel = new JLabel("Sistem: " + sistem.getidSistem());
        sistemLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        sistemLabel.setForeground(new Color(220, 220, 220));
        
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(sistemLabel, BorderLayout.SOUTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Data Siswa
        contentPanel.add(createSectionLabel("DATA SISWA"));
        contentPanel.add(createInfoLabel("ID Siswa", siswa.getIdSiswa()));
        contentPanel.add(createInfoLabel("Nama", siswa.getNama()));
        contentPanel.add(createInfoLabel("Email", siswa.getEmail()));
        contentPanel.add(createInfoLabel("Telepon", siswa.getNomorTelepon()));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Data Kursus
        contentPanel.add(createSectionLabel("DATA KURSUS"));
        contentPanel.add(createInfoLabel("Kursus", kursus.getNama()));
        contentPanel.add(createInfoLabel("Kode", kursus.getKodeKursus()));
        contentPanel.add(createInfoLabel("Biaya", "Rp " + String.format("%,.0f", kursus.getBiaya())));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Data Transaksi
        contentPanel.add(createSectionLabel("DATA TRANSAKSI"));
        contentPanel.add(createInfoLabel("ID Transaksi", transaksi.getIdTransaksi()));
        contentPanel.add(createInfoLabel("Metode", transaksi.getMetodePembayaran()));
        contentPanel.add(createInfoLabel("Status", transaksi.getStatus()));
        
        // Status warna
        JLabel statusLabel = new JLabel();
        String status = transaksi.getStatus();

        statusLabel.setText("Status: " + status);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));

        if (status.equals("SUKSES")) {
            statusLabel.setForeground(new Color(39, 174, 96));
        } else {
            statusLabel.setForeground(new Color(243, 156, 18));
        }
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(statusLabel);
        
        // Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnOK = new JButton("OK");
        btnOK.setBackground(new Color(52, 152, 219));
        btnOK.setForeground(Color.WHITE);
        btnOK.setPreferredSize(new Dimension(100, 30));
        btnOK.addActionListener(e -> dialog.dispose());
        
        if (transaksi.getStatus().equals("PENDING")) {
            JButton btnBayarUlang = new JButton("Bayar Ulang");
            btnBayarUlang.setBackground(new Color(241, 196, 15));
            btnBayarUlang.setForeground(Color.BLACK);

            btnBayarUlang.addActionListener(e -> {
                dialog.dispose();
                // arahkan user ke form lagi (tanpa ubah model)
                comboMetode.setEnabled(true);
                comboMetode.setSelectedIndex(1); // Transfer Bank
            });

            buttonPanel.add(btnBayarUlang);
        }

        JButton btnLihatRiwayat = new JButton("Lihat Riwayat");
        btnLihatRiwayat.setBackground(new Color(155, 89, 182));
        btnLihatRiwayat.setForeground(Color.WHITE);
        btnLihatRiwayat.addActionListener(e -> {
            dialog.dispose();
            showRiwayatSiswa(siswa);
        });
        
        buttonPanel.add(btnOK);
        buttonPanel.add(btnLihatRiwayat);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(44, 62, 80));
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JPanel createInfoLabel(String key, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel keyLabel = new JLabel(key + ": ");
        keyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        keyLabel.setPreferredSize(new Dimension(100, 20));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(keyLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private void handleLihatData(ActionEvent e) {
        if (frameDataSiswa == null || !frameDataSiswa.isVisible()) {
            frameDataSiswa = new daftarKursus(sistem);
        } else {
            frameDataSiswa.refreshData();
            frameDataSiswa.toFront();
        }
    }
    
    private void showRiwayatSiswa(siswa siswa) {
        JDialog dialog = new JDialog(this, "Riwayat Siswa", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Riwayat: " + siswa.getNama(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        StringBuilder riwayat = new StringBuilder();
        for (String r : siswa.getLihatRiwayatPendaftaran()) {
            riwayat.append("• ").append(r).append("\n");
        }
        
        if (riwayat.length() == 0) {
            riwayat.append("Belum ada riwayat pendaftaran.");
        }
        
        textArea.setText(riwayat.toString());
        
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private void showInfoKuota() {
        StringBuilder info = new StringBuilder();
        info.append("INFORMASI KUOTA KURSUS\n");
        info.append("Sistem: ").append(sistem.getidSistem()).append("\n\n");
        
        for (kursus k : sistem.getDaftarKursus()) {
            info.append(String.format("%s (%s):\n", k.getNama(), k.getKodeKursus()));
            info.append(String.format("  Kuota: %s/%s siswa\n", 
                k.getJumlahSiswaTerdaftar(), k.getKuotaMaksimal()));
            info.append(String.format("  Biaya: Rp %,.0f\n", k.getBiaya()));
            info.append("  Status: ").append(k.cekKetersediaan() ? "Masih tersedia" : "Penuh").append("\n\n");
        }
        
        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Info Kuota Kursus", JOptionPane.INFORMATION_MESSAGE);
    }
}