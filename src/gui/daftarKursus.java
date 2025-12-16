package gui;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class daftarKursus extends JFrame {
    private sistemPendaftaran sistem;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public daftarKursus(sistemPendaftaran sistem) {
        this.sistem = sistem;
        setupGUI();
        loadData();
        setVisible(true);
    }
    
    private void setupGUI() {
        setTitle("Data Siswa Terdaftar");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JLabel title = new JLabel("DATA SISWA TERDAFTAR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(70, 130, 180));
        
        // Tabel dengan kolom yang benar
        String[] columns = {"No", "ID", "Nama", "Email", "Telepon", "Kursus Diambil"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> loadData());
        
        JButton btnTutup = new JButton("Tutup");
        btnTutup.setBackground(new Color(231, 76, 60));
        btnTutup.setForeground(Color.WHITE);
        btnTutup.addActionListener(e -> dispose());
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnTutup);
        
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    // Method untuk load data dengan FIX mapping kolom
    public void loadData() {
        tableModel.setRowCount(0); // Clear table
        
        List<siswa> daftarSiswa = sistem.getDaftarSiswa();
        
        for (int i = 0; i < daftarSiswa.size(); i++) {
            siswa s = daftarSiswa.get(i);
            
            // Debug log ke console
            System.out.println("[DEBUG] Loading siswa: " + 
                "ID=" + s.getIdSiswa() + ", " +
                "Nama=" + s.getNama() + ", " +
                "Email=" + s.getEmail() + ", " +
                "Telepon=" + s.getNomorTelepon());
            
            // Ambil daftar kursus siswa
            List<String> kodeKursusList = s.getDaftarKursus();
            StringBuilder kursusStr = new StringBuilder();
            
            if (kodeKursusList != null && !kodeKursusList.isEmpty()) {
                for (String kode : kodeKursusList) {
                    kursus k = sistem.cariKursus(kode);
                    if (k != null) {
                        if (kursusStr.length() > 0) kursusStr.append(", ");
                        kursusStr.append(k.getNama());
                    }
                }
            } else {
                kursusStr.append("-");
            }
            
            // FIX: Pastikan urutan kolom sesuai
            tableModel.addRow(new Object[]{
                i + 1,                    // No
                s.getIdSiswa(),                // ID
                s.getNama(),              // Nama
                s.getEmail(),             // Email
                s.getNomorTelepon() != null ? s.getNomorTelepon() : "-", // Telepon
                kursusStr.toString()      // Kursus Diambil
            });
        }
    }
    public void refreshData() {
    loadData();
}
}