package app;

import gui.*;
import javax.swing.*;
import model.*;

public class mainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Buat sistem
            sistemPendaftaran sistem = new sistemPendaftaran("SYS001");
            
            // Tambah beberapa kursus
            sistem.tambahKursus(new kursusBerbayar("K001", "Java Programming", 10, 500000));
            sistem.tambahKursus(new kursusGratis("K002", "HTML Dasar", 20, 0));
            sistem.tambahKursus(new kursusBerbayar("K003", "Database SQL", 15, 750000));
            sistem.tambahKursus(new kursusGratis("K004", "Python Pemula", 25, 0));
            
            // Tampilkan form (form kosong untuk siswa baru)
            new formPendaftaran(sistem);
        });
    }
}