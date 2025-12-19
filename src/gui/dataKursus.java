package gui;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class dataKursus extends JFrame {
    private sistemPendaftaran sistem;
    private JTable tableSiswa;
    private DefaultTableModel tableModel;
    
    public dataKursus(sistemPendaftaran sistem) {
        this.sistem = sistem;
        setupGUI();
        loadData();
    }
    
    private void setupGUI() {
        setTitle("Data Siswa Terdaftar");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JLabel title = new JLabel("DATA SISWA TERDAFTAR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(70, 130, 180));
        
        // Tabel
        String[] columns = {"ID Siswa", "Nama", "Email", "Telepon", "Jumlah Kursus", "Kursus Diikuti"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableSiswa = new JTable(tableModel);
        tableSiswa.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableSiswa);
        
        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> refreshData());
        
        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.setBackground(new Color(155, 89, 182));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.addActionListener(e -> showDetail());
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDetail);
        
        // Gabung komponen
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    public void refreshData() {
        loadData();
        JOptionPane.showMessageDialog(this, 
            "Data berhasil diperbarui!", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        
        for (siswa s : sistem.getDaftarSiswa()) {
            // Format kursus yang diikuti
            StringBuilder kursusDiikuti = new StringBuilder();
            for (String kodeKursus : s.getDaftarKursus()) {
                kursus kursusObj = sistem.cariKursus(kodeKursus);
                if (kursusObj != null) {
                    kursusDiikuti.append(kursusObj.getNama()).append(", ");
                }
            }
            
            // Hapus koma terakhir
            String kursusStr = kursusDiikuti.toString();
            if (kursusStr.length() > 2) {
                kursusStr = kursusStr.substring(0, kursusStr.length() - 2);
            }
            
            // Tambah ke tabel
            tableModel.addRow(new Object[]{
                s.getIdSiswa(),
                s.getNama(),
                s.getEmail(),
                s.getNomorTelepon(),
                s.getDaftarKursus().size(),
                kursusStr
            });
        }
    }
    
    private void showDetail() {
        int selectedRow = tableSiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih siswa terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idSiswa = (String) tableModel.getValueAt(selectedRow, 0);
        siswa siswa = sistem.cariSiswa(idSiswa);
        
        if (siswa != null) {
            showDetailSiswa(siswa);
        }
    }
    
    private void showDetailSiswa(siswa siswa) {
        JDialog detailDialog = new JDialog(this, "Detail Siswa", true);
        detailDialog.setSize(500, 400);
        detailDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JLabel title = new JLabel("DETAIL SISWA: " + siswa.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Data
        JPanel dataPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        dataPanel.setBorder(BorderFactory.createTitledBorder("Informasi Siswa"));
        
        dataPanel.add(new JLabel("ID Siswa:"));
        dataPanel.add(new JLabel(siswa.getIdSiswa()));
        dataPanel.add(new JLabel("Nama:"));
        dataPanel.add(new JLabel(siswa.getNama()));
        dataPanel.add(new JLabel("Email:"));
        dataPanel.add(new JLabel(siswa.getEmail()));
        dataPanel.add(new JLabel("Telepon:"));
        dataPanel.add(new JLabel(siswa.getNomorTelepon()));
        dataPanel.add(new JLabel("Jumlah Kursus:"));
        dataPanel.add(new JLabel(String.valueOf(siswa.getDaftarKursus().size())));
        
        // Riwayat
        JTextArea riwayatArea = new JTextArea();
        riwayatArea.setEditable(false);
        riwayatArea.setRows(8);
        riwayatArea.setText("RIWAYAT PENDAFTARAN:\n");
        for (String riwayat : siswa.getLihatRiwayatPendaftaran()) {
            riwayatArea.append("- " + riwayat + "\n");
        }
        
        JScrollPane scrollRiwayat = new JScrollPane(riwayatArea);
        scrollRiwayat.setBorder(BorderFactory.createTitledBorder("Riwayat"));
        
        // Tombol
        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> detailDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(scrollRiwayat, BorderLayout.SOUTH);
        
        detailDialog.add(mainPanel);
        detailDialog.setVisible(true);
    }
}