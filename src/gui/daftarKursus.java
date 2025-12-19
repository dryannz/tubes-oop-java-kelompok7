package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

public class daftarKursus extends JFrame {

    private sistemPendaftaran sistem;
    private JTable table;
    private DefaultTableModel tableModel;

    public daftarKursus(sistemPendaftaran sistem) {
        this.sistem = sistem;
        initGUI();
        loadData();
        setVisible(true);
    }

    private void initGUI() {
        setTitle("Daftar Siswa & Kursus");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== Header =====
        JLabel title = new JLabel("DATA SISWA TERDAFTAR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(70, 130, 180));

        // ===== Tabel =====
        String[] kolom = {
            "No", "ID Siswa", "Nama", "Email", "Telepon", "Kursus Diambil"
        };

        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(table);

        // ===== Tombol =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> refreshData());

        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.setBackground(new Color(155, 89, 182));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.addActionListener(e -> new dataSiswa(sistem));

        JButton btnTutup = new JButton("Tutup");
        btnTutup.setBackground(new Color(231, 76, 60));
        btnTutup.setForeground(Color.WHITE);
        btnTutup.addActionListener(e -> dispose());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDetail);
        buttonPanel.add(btnTutup);

        // ===== Layout =====
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadData() {
        tableModel.setRowCount(0);

        List<siswa> daftarSiswa = sistem.getDaftarSiswa();
        int no = 1;

        for (siswa s : daftarSiswa) {
            String kursusStr = formatKursus(s);

            tableModel.addRow(new Object[]{
                no++,
                s.getIdSiswa(),
                s.getNama(),
                s.getEmail(),
                s.getNomorTelepon() != null ? s.getNomorTelepon() : "-",
                kursusStr
            });
        }
    }

    private String formatKursus(siswa s) {
        if (s.getDaftarKursus() == null || s.getDaftarKursus().isEmpty()) {
            return "-";
        }

        StringBuilder hasil = new StringBuilder();
        for (String kode : s.getDaftarKursus()) {
            kursus k = sistem.cariKursus(kode);
            if (k != null) {
                if (hasil.length() > 0) hasil.append(", ");
                hasil.append(k.getNama());
            }
        }
        return hasil.toString();
    }

    public void refreshData() {
        loadData();
    }
}
