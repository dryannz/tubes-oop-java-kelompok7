package app;

import model.*;
import gui.*;

public class mainGUI {
    public static void main(String[] args) {
        // Inisialisasi sistem
        sistemPendaftaran sistem = new sistemPendaftaran("SYS001");
        
        // Tambah kursus ke sistem
        sistem.tambahKursus(new kursusBerbayar("K001", "Java Programming", 3, 500000));
        sistem.tambahKursus(new kursusBerbayar("K002", "Python Data Science", 3, 750000));
        sistem.tambahKursus(new kursusGratis("K003", "Web Development Dasar", 3, 0));
        sistem.tambahKursus(new kursusGratis("K004", "UI/UX Design", 3, 0));
        
        // Jalankan GUI di EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            new formPendaftaran(sistem);
        });
    }
}