package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

public class dataSiswa extends JFrame {
    private sistemPendaftaran sistem;
    private JTable tableSiswa;
    private DefaultTableModel tableModel;

    // === TAMBAHAN ===
    private JTextField txtCari;
    private List<siswa> dataAsli;

    public dataSiswa(sistemPendaftaran sistem) {
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("DATA SISWA TERDAFTAR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(70, 130, 180));

        // PANEL SEARCH
        JPanel cariPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cariPanel.add(new JLabel("Cari (ID / Nama):"));

        txtCari = new JTextField(20);
        JButton btnCari = new JButton("Cari");
        btnCari.setBackground(new Color(52, 152, 219));
        btnCari.setForeground(Color.WHITE);
        btnCari.setFocusPainted(false);

        btnCari.addActionListener(e -> cariSiswa());

        cariPanel.add(txtCari);
        cariPanel.add(btnCari);

        // === TABEL ===
        String[] columns = {
            "ID Siswa", "Nama", "Email", "Telepon", "Jumlah Kursus", "Kursus Diikuti"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tableSiswa = new JTable(tableModel);
        tableSiswa.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableSiswa);

        // === TOMBOL ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> {
            txtCari.setText("");
            tampilkanKeTabel(dataAsli);
        });

        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.setBackground(new Color(155, 89, 182));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setFocusPainted(false);
        btnDetail.addActionListener(e -> showDetail());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDetail);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(cariPanel, BorderLayout.BEFORE_FIRST_LINE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadData() {
        dataAsli = sistem.getDaftarSiswa();
        tampilkanKeTabel(dataAsli);
    }

    // === TAMPILKAN DATA KE TABEL ===
    private void tampilkanKeTabel(List<siswa> list) {
        tableModel.setRowCount(0);

        for (siswa s : list) {
            StringBuilder kursusDiikuti = new StringBuilder();

            for (String kode : s.getDaftarKursus()) {
                kursus k = sistem.cariKursus(kode);
                if (k != null) {
                    kursusDiikuti.append(k.getNama()).append(", ");
                }
            }

            String kursusStr = kursusDiikuti.toString();
            if (kursusStr.length() > 2) {
                kursusStr = kursusStr.substring(0, kursusStr.length() - 2);
            }

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

    // === FITUR CARI ===
    private void cariSiswa() {
        String keyword = txtCari.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            tampilkanKeTabel(dataAsli);
            return;
        }

        List<siswa> hasil = new ArrayList<>();

        for (siswa s : dataAsli) {
            if (s.getIdSiswa().toLowerCase().contains(keyword)
                || s.getNama().toLowerCase().contains(keyword)) {
                hasil.add(s);
            }
        }

        if (hasil.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Siswa tidak ditemukan!",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
        }

        tampilkanKeTabel(hasil);
    }

    // === DETAIL (TIDAK DIUBAH) ===
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

    // === TAMPILAN DETAIL TETAP ===
    private void showDetailSiswa(siswa siswa) {
        JDialog detailDialog = new JDialog(this, "Detail Siswa", true);
        detailDialog.setSize(500, 400);
        detailDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("DETAIL SISWA: " + siswa.getNama(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

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

        JTextArea riwayatArea = new JTextArea();
        riwayatArea.setEditable(false);
        riwayatArea.setText("RIWAYAT PENDAFTARAN:\n");

        for (String r : siswa.getLihatRiwayatPendaftaran()) {
            riwayatArea.append("- " + r + "\n");
        }

        JScrollPane scrollRiwayat = new JScrollPane(riwayatArea);
        scrollRiwayat.setBorder(BorderFactory.createTitledBorder("Riwayat"));

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
