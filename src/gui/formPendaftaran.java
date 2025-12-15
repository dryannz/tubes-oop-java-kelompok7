package gui;

import model.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Date;

public class formPendaftaran extends JFrame implements ActionListener {

    private JTextField inputIdSiswa;
    private JTextField inputStatus;
    private JButton tombolDaftar;
    private JPanel formPanel;

    private JComboBox<String> comboKursus;
    private JComboBox<String> comboMetodeBayar;

    private JTextArea areaStatus;
    private JScrollPane scrollStatus;

    private sistemPendaftaran sistem;

    public formPendaftaran(sistemPendaftaran sistem) {
        this.sistem = sistem;

        setTitle("Form Pendaftaran Kursus Online");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ================= PANEL FORM =================
        formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("ID Siswa:"));
        inputIdSiswa = new JTextField("S-101");
        formPanel.add(inputIdSiswa);

        formPanel.add(new JLabel("Status:"));
        inputStatus = new JTextField("Siap mendaftar...");
        inputStatus.setEditable(false);
        formPanel.add(inputStatus);

        formPanel.add(new JLabel("Pilih Kursus:"));
        comboKursus = new JComboBox<>(sistem.getDaftarNamaKursus());
        formPanel.add(comboKursus);
        comboKursus.addActionListener(e -> updateMetodePembayaran());

        formPanel.add(new JLabel("Metode Pembayaran:"));
        comboMetodeBayar = new JComboBox<>(
                new String[]{"", "Transfer Bank", "Kartu Kredit", "E-Wallet", "GRATIS"}
        );
        formPanel.add(comboMetodeBayar);

        tombolDaftar = new JButton("Daftar Sekarang");
        tombolDaftar.addActionListener(this);

        formPanel.add(new JLabel(""));
        formPanel.add(tombolDaftar);

        add(formPanel, BorderLayout.NORTH);

        // ================= LOG AREA =================
        areaStatus = new JTextArea("Log Sistem:\n", 15, 40);
        areaStatus.setEditable(false);
        scrollStatus = new JScrollPane(areaStatus);
        scrollStatus.setBorder(BorderFactory.createTitledBorder("Riwayat Transaksi"));

        add(scrollStatus, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == tombolDaftar) {
            prosesPendaftaran();
        }
    }

    private void prosesPendaftaran() {
        String idSiswa = inputIdSiswa.getText().trim();
        String kursusDipilih = (String) comboKursus.getSelectedItem();
        String metodeBayar = (String) comboMetodeBayar.getSelectedItem();

        if (idSiswa.isEmpty() || kursusDipilih == null) {
            JOptionPane.showMessageDialog(this,
                    "ID Siswa dan Kursus wajib diisi!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String kodeKursus;
        try {
            kodeKursus = kursusDipilih.substring(
                    kursusDipilih.indexOf("(") + 1,
                    kursusDipilih.indexOf(")")
            );
        } catch (Exception e) {
            appendLog("❌ Format kursus tidak valid");
            return;
        }

        try {
            pembayaran p = sistem.enrollSiswa(idSiswa, kodeKursus, metodeBayar);

            inputStatus.setText(p.getStatus() + " | Rp " + p.getJumlahBayar());

            appendLog(String.format(
                    "[%tT] ✅ BERHASIL | TRX: %s | %s | Rp %.2f",
                    new Date(),
                    p.getIdTransaksi(),
                    p.getStatus(),
                    p.getJumlahBayar()
            ));

        } catch (kuotaPenuhException ex) {
            inputStatus.setText("GAGAL - KUOTA PENUH");
            appendLog("❌ Kuota penuh: " + ex.getKodeKursusYangGagal());

        } catch (IllegalArgumentException ex) {
            inputStatus.setText("GAGAL");
            appendLog("❌ " + ex.getMessage());

        } catch (Exception ex) {
            inputStatus.setText("ERROR SISTEM");
            appendLog("❌ Error: " + ex.getMessage());
        }
    }

    private void appendLog(String msg) {
        areaStatus.append(msg + "\n");
        areaStatus.setCaretPosition(areaStatus.getDocument().getLength());
    }

    private void updateMetodePembayaran() {
    String kursusDipilih = (String) comboKursus.getSelectedItem();
    if (kursusDipilih == null) return;

    String kodeKursus = kursusDipilih.substring(
            kursusDipilih.indexOf("(") + 1,
            kursusDipilih.indexOf(")")
    );

    kursus k = sistem.cariKursus(kodeKursus);
    if (k == null) return;

    if (k.getBiaya() == 0) {
        // KURSUS GRATIS
        comboMetodeBayar.setSelectedItem("GRATIS");
        comboMetodeBayar.setEnabled(false);
        inputStatus.setText("Kursus GRATIS");
    } else {
        // KURSUS BERBAYAR
        comboMetodeBayar.setEnabled(true);
        comboMetodeBayar.setSelectedIndex(0);
        inputStatus.setText("Biaya: Rp " + k.getBiaya());
    }
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {

        sistemPendaftaran sistem = new sistemPendaftaran("S001");
        sistem.tambahKursus(new kursusBerbayar(
                "K001",
                "Java OOP",
                25,         
                500000
        ));

        sistem.tambahKursus(new kursusGratis(
        "K002",
        "Proses Bisnis",
        15
        ));

        sistem.tambahKursus(new kursusBerbayar(
        "K003",
        "Basis Data",
        20,
        300000
        ));

        new formPendaftaran(sistem);
    });
}
}